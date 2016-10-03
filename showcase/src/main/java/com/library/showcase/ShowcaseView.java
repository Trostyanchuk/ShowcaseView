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
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class ShowcaseView extends View implements View.OnTouchListener, View.OnClickListener {

    private int circlesNumber = Defaults.CIRCLES_NUMBER_DEFAULT;
    private boolean animate = Defaults.ANIMATE;

    private int focusCircleWidth;
    private int bgCircleWidth;

    private int bgCirclesColor;
    private int bgColor;

    private float bgCirclesAlpha = Defaults.MAX_ALPHA;
    private float bgCirclesStartAlpha = Defaults.MAX_ALPHA;
    private float bgAlpha = Defaults.MAX_ALPHA;

    private Paint clearPaint;
    private Paint circlesLinePaint;
    private Paint bgPaint;
    private Paint fontPaint;

    private List<Circle> circles;
    private int growth;

    int canvasWidth;
    int canvasHeight;
    int canvasPositionX;
    int canvasPositionY;

    int viewPositionX;
    int viewPositionY;
    int viewWidth;
    int viewHeight;

    float cX = -1;
    float cY = -1;
    int circleStartRadius;
    int viewFocusRadius;

    private ShowcaseEventListener listener;

    public ShowcaseView(Context context) {
        super(context);
        init();
    }

    public ShowcaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShowcaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        focusCircleWidth = (int) getContext().getResources().getDimension(R.dimen.circle_min_height);
        bgCircleWidth = (int) getContext().getResources().getDimension(R.dimen.circle_stroke_width);
        viewFocusRadius = focusCircleWidth;

        bgCirclesColor = ContextCompat.getColor(getContext(), R.color.circles_default);
        bgColor = ContextCompat.getColor(getContext(), R.color.bg_default);

        circlesLinePaint = new Paint();
        circlesLinePaint.setColor(bgCirclesColor);
        circlesLinePaint.setAlpha((int) (bgCirclesAlpha * 255));
        circlesLinePaint.setAntiAlias(true);
        circlesLinePaint.setStyle(Paint.Style.STROKE);
        circlesLinePaint.setStrokeWidth(bgCircleWidth);

        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setAlpha((int) (bgAlpha * 255));

        clearPaint = new Paint();
        clearPaint.setColor(Color.TRANSPARENT);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        clearPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                getLocationOnScreen(location);
                canvasPositionX = location[0];
                canvasPositionY = location[1];
                canvasWidth = getMeasuredWidth();
                canvasHeight = getMeasuredHeight();

                circles = new ArrayList<>();
                growth = (canvasWidth - viewPositionX) / circlesNumber;
                int circleRectStartSize = focusCircleWidth + growth;
                int radius = circleRectStartSize;
                circleStartRadius = radius;
                for (int i = 0; i < circlesNumber; i++) {
                    radiusArray[i] = radius;
                    radiusArrayInitial[i] = radius;
                    alphaArray[i] = bgCirclesAlpha - i * 0.05f;

                    circles.add(new Circle(radius, bgCirclesAlpha));

                    circleRectStartSize += growth;
                    radius = circleRectStartSize;
                }

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void addTargetView(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                viewWidth = view.getMeasuredWidth();
                viewHeight = view.getMeasuredHeight();
                viewPositionX = location[0];
                viewPositionY = location[1];

                viewFocusRadius = viewWidth / 2 + bgCircleWidth / 2;
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    int[] radiusArray = new int[circlesNumber];
    int[] radiusArrayInitial = new int[circlesNumber];
    float[] alphaArray = new float[circlesNumber];

    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cX = getCenterX();
        float cY = getCenterY();

        if (bitmap == null || bitmapCanvas == null) {
            bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
        }

        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        bitmapCanvas.drawPaint(bgPaint);
        bitmapCanvas.drawCircle(cX, cY, viewFocusRadius, clearPaint);

        for (int index = 0; index < circlesNumber; index++) {

            Circle circle = circles.get(index);

            circlesLinePaint.setShader(new RadialGradient(cX, cY, circle.getRadius(), bgCirclesColor, Color.TRANSPARENT, Shader.TileMode.CLAMP));
            circlesLinePaint.setAlpha(alphaArray[index] <= 0 ? 0 : (int) (alphaArray[index] * 255));

            bitmapCanvas.drawCircle(cX, cY, circle.getRadius(), circlesLinePaint);

            circle.increaseRadius(5);
            circle.decreaseAlpha(0.02f / (index + 1));

            if (index == 0 && circle.getRadius() - circleStartRadius >= growth) {
                circles.add(0, new Circle(circleStartRadius, bgCirclesStartAlpha));
                circles.remove(circlesNumber);
            }
        }
        canvas.drawBitmap(bitmap, 0, 0, null);

        if (animate) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 25);
        }
    }

    private float getCenterX() {
        return cX == -1 ? viewPositionX + viewWidth / 2 : cX;
    }

    private float getCenterY() {
        return cY == -1 ? viewPositionY + viewHeight / 2 - canvasPositionY : cY;
    }


    ///---------------------------///////

    /**
     * @param number
     * @return
     */
    public ShowcaseView setCirclesNumber(int number) {
        this.circlesNumber = number < Defaults.CIRCLES_NUMBER_MIN ? Defaults.CIRCLES_NUMBER_MIN :
                number > Defaults.CIRCLES_NUMBER_MAX ? Defaults.CIRCLES_NUMBER_MAX :
                        number;
        return this;
    }

    /**
     * @param color
     * @return
     */
    public ShowcaseView setCirclesColor(int color) {
        this.bgCirclesColor = color;
        return this;
    }

    /**
     * @param color
     * @return
     */
    public ShowcaseView setViewBgColor(int color) {
        this.bgColor = color;
        return this;
    }

    /**
     * @param focusCircleWidth
     * @return
     */
    public ShowcaseView setFocusCircleWidth(int focusCircleWidth) {
        this.focusCircleWidth = focusCircleWidth;
        return this;
    }

    /**
     * @param strokeWidth
     * @return
     */
    public ShowcaseView setCircleStrokeWidth(int strokeWidth) {
        this.bgCircleWidth = strokeWidth;
        return this;
    }

    /**
     * @param bgCirclesAlpha
     * @return
     */
    public ShowcaseView setCirclesAlpha(float bgCirclesAlpha) {
        this.bgCirclesAlpha = bgCirclesAlpha > Defaults.MAX_ALPHA ? Defaults.MAX_ALPHA :
                bgCirclesAlpha < Defaults.MIN_ALPHA ? Defaults.MIN_ALPHA : bgCirclesAlpha;
        return this;
    }

    /**
     * @param backgroundAlpha
     * @return
     */
    public ShowcaseView setBackgroundAlpha(float backgroundAlpha) {
        this.bgAlpha = backgroundAlpha > Defaults.MAX_ALPHA ? Defaults.MAX_ALPHA :
                backgroundAlpha < Defaults.MIN_ALPHA ? Defaults.MIN_ALPHA : backgroundAlpha;
        return this;
    }

    /**
     * @param listener
     * @return
     */
    public ShowcaseView setShowcaseEventListener(ShowcaseEventListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     *
     */
    public void startAnimation() {
        animate = true;
        invalidate();
    }

    /**
     *
     */
    public void stopAnimation() {
        animate = false;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        
        return false;
    }
}
