package com.library.showcase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.library.showcase.constants.Defaults;
import com.library.showcase.constants.Part;

import java.util.ArrayList;
import java.util.List;

class CirclesCanvasView extends View implements View.OnTouchListener {

    private int mCirclesNumber;
    private boolean mAnimate;

    private int mFocusCircleWidth;
    private int mBgCircleWidth;
    private int mBgCirclesColor;
    private int mBgColor;
    private float mBgCirclesAlpha;
    private float mBgCirclesStartAlpha;
    private float mBgAlpha;

    private Paint mClearPaint;
    private Paint mCirclesLinePaint;
    private Paint mBgPaint;

    private List<Circle> mCircles;
    private int mGrowth;

    int mCanvasWidth;
    int mCanvasHeight;
    int mCanvasPositionX;
    int mCanvasPositionY;

    int mViewPositionX;
    int mViewPositionY;
    int mViewWidth;
    int mViewHeight;

    float cX = -1;
    float cY = -1;
    int mCircleStartRadius;
    int[] mRadiusArray;
    int[] mRadiusArrayInitial;
    float[] mAlphaArray;

    private Bitmap mBitmap;
    private Canvas mBitmapCanvas;
    private Handler mHandler;
    private Runnable mAnimationRunnable;

    private ShowcaseParams params;

    private OnViewLocationCalculatedListener onViewLocationCalculatedListener;

    public CirclesCanvasView(Context context) {
        super(context);
        init();
    }

    public CirclesCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CirclesCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CirclesCanvasView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //paint for transparent region
        mClearPaint = new Paint();
        mClearPaint.setColor(Color.TRANSPARENT);
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mClearPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //allows to get canvas size and return allocated part to listeners which should use this
        // info to place other views (buttons, message) due to focus location
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                getLocationOnScreen(location);
                mCanvasPositionX = location[0];
                mCanvasPositionY = location[1];
                mCanvasWidth = getMeasuredWidth();
                mCanvasHeight = getMeasuredHeight();

                returnFocusViewPartLocation();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    CirclesCanvasView addTargetView(final View view) {
        if (view.getViewTreeObserver() == null) {
            view.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left == 0 && top == 0 && right == 0 && bottom == 0)
                        return;
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);

                    mViewWidth = view.getMeasuredWidth();
                    mViewHeight = view.getMeasuredHeight();
                    mViewPositionX = location[0];
                    mViewPositionY = location[1];

                    view.removeOnLayoutChangeListener(this);
                }
            });
        } else {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    mViewWidth = view.getMeasuredWidth();
                    mViewHeight = view.getMeasuredHeight();
                    mViewPositionX = location[0];
                    mViewPositionY = location[1];

                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        return this;
    }

    //find and return part which is allocated by circle focus
    private void returnFocusViewPartLocation() {
        if (onViewLocationCalculatedListener == null) {
            return;
        }
        boolean left = mViewPositionX < mCanvasWidth / 2;
        boolean top = mViewPositionY < mCanvasHeight / 2;

        Part part;
        if (top) {
            part = left ? Part.TOP_LEFT : Part.TOP_RIGHT;
        } else {
            part = left ? Part.BOTTOM_LEFT : Part.BOTTOM_RIGHT;
        }
        //notify listeners
        onViewLocationCalculatedListener.onViewLocated(part);
    }

    public void initViewWithParams(ShowcaseParams params) {

        this.params = params;
        this.mAnimate = params.isAnimate();

        mCirclesNumber = params.getCirclesNumber() != 0 ? params.getCirclesNumber() : Defaults.CIRCLES_NUMBER_DEFAULT;

        mRadiusArray = new int[mCirclesNumber];
        mRadiusArrayInitial = new int[mCirclesNumber];
        mAlphaArray = new float[mCirclesNumber];

        mFocusCircleWidth = params.getFocusCircleSize() != 0 ? params.getFocusCircleSize() :
                (int) getContext().getResources().getDimension(R.dimen.circle_default_height);
        mBgCircleWidth = params.getCircleWidth() != 0 ? params.getCircleWidth() :
                (int) getContext().getResources().getDimension(R.dimen.circle_stroke_width);

        if (params.getCirclesColorRes() != -1) {
            mBgCirclesColor = ContextCompat.getColor(getContext(), params.getCirclesColorRes());
        } else {
            mBgCirclesColor = ContextCompat.getColor(getContext(), R.color.circles_default);
        }
        if (params.getBackgroundColorRes() != -1) {
            mBgColor = ContextCompat.getColor(getContext(), params.getBackgroundColorRes());
        } else {
            mBgColor = ContextCompat.getColor(getContext(), R.color.bg_default);
        }

        mBgCirclesAlpha = params.getCirclesAlpha() >= 0 ? params.getCirclesAlpha() :
                Defaults.MAX_ALPHA;
        mBgCirclesStartAlpha = mBgCirclesAlpha;
        mBgAlpha = params.getBackgroundAlpha() >= 0 ? params.getBackgroundAlpha() :
                Defaults.MAX_ALPHA;

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAlpha((int) (mBgAlpha * 255));

        mCirclesLinePaint = new Paint();
        mCirclesLinePaint.setColor(mBgCirclesColor);
        mCirclesLinePaint.setAlpha((int) (mBgCirclesAlpha * 255));
        mCirclesLinePaint.setAntiAlias(true);
        mCirclesLinePaint.setStyle(Paint.Style.STROKE);
        mCirclesLinePaint.setStrokeWidth(mBgCircleWidth);

        initCircles();
    }

    private void initCircles() {
        mCircles = new ArrayList<>();
        if (mViewPositionX > mCanvasWidth / 2) {
            mGrowth = mViewPositionX / mCirclesNumber;
        } else {
            mGrowth = (mCanvasWidth - mViewPositionX) / mCirclesNumber;
        }
        int circleRectStartSize = mFocusCircleWidth;
        int radius = mFocusCircleWidth + mBgCircleWidth / 2;
        mCircleStartRadius = radius;
        for (int i = 0; i < mCirclesNumber; i++) {
            mRadiusArray[i] = radius;
            mRadiusArrayInitial[i] = radius;
            mAlphaArray[i] = mBgCirclesAlpha - i * 0.06f;

            mCircles.add(new Circle(radius, mBgCirclesAlpha));

            circleRectStartSize += mGrowth;
            radius = circleRectStartSize + mBgCircleWidth / 2;
        }
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cX = getCenterX();
        float cY = getCenterY();

        if (mBitmap == null || mBitmapCanvas == null) {
            mBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
            mBitmapCanvas = new Canvas(mBitmap);
        }

        mBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mBitmapCanvas.drawPaint(mBgPaint);
        mBitmapCanvas.drawCircle(cX, cY, mFocusCircleWidth, mClearPaint);

        if (!params.isShowCircles()) {
            return;
        }
        for (int index = 0; index < mCirclesNumber; index++) {

            Circle circle = mCircles.get(index);

            mCirclesLinePaint.setShader(new RadialGradient(cX, cY, circle.getRadius(), mBgCirclesColor, Color.TRANSPARENT, Shader.TileMode.CLAMP));
            mCirclesLinePaint.setAlpha(mAlphaArray[index] <= 0 ? 0 : (int) (mAlphaArray[index] * 255));

            mBitmapCanvas.drawCircle(cX, cY, circle.getRadius(), mCirclesLinePaint);

            circle.increaseRadius(2);
            circle.decreaseAlpha(0.03f / (index + 1));

            if (index == 0 && circle.getRadius() - mCircleStartRadius >= mGrowth) {
                mCircles.add(0, new Circle(mCircleStartRadius, mBgCirclesStartAlpha - 0.05f));
                mCircles.remove(mCirclesNumber);
                index++;
            }
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);

        if (mAnimate) {
            mHandler = new Handler();
            mHandler.postDelayed(mAnimationRunnable = new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 25);
        }
    }

    private float getCenterX() {
        return cX == -1 ? mViewPositionX + mViewWidth / 2 : cX;
    }

    private float getCenterY() {
        return cY == -1 ? mViewPositionY + mViewHeight / 2 - mCanvasPositionY : cY;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        mBitmapCanvas = null;
        mHandler = null;
    }

    /**
     * @param listener OnViewLocationCalculatedListener to notify when part of view allocated by
     *                 focus circle was calculated
     */
    public void setOnViewLocationCalculatedListener(OnViewLocationCalculatedListener listener) {
        this.onViewLocationCalculatedListener = listener;
    }

    /**
     * Call to start animation after view was created or to restore animation after
     * onResume() for activit or fragment were called
     */
    public void startAnimation() {
        mAnimate = params.isAnimate();
        invalidate();
    }

    /**
     * Call to stop animation while activity or fragment lifecycle was changed.
     * Should call this method onPause()
     */
    public void stopAnimation() {
        mAnimate = false;
        if (mHandler != null) {
            mHandler.removeCallbacks(mAnimationRunnable);
            mAnimationRunnable = null;
            mHandler = null;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }
}
