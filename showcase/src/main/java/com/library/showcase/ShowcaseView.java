package com.library.showcase;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.showcase.constants.Part;

public class ShowcaseView extends RelativeLayout implements OnViewLocationCalculatedListener, View.OnTouchListener {

    private TextView mButton;
    private TextView mInfoText;
    private CirclesCanvasView mShowcaseCirclesView;

    private ShowcaseParams params;
    private String mButtonDefaultText;
    private float mBtnDefaultTextSize;
    private float mMsgDefaultTextSize;
    private int mDefaultMargin;

    private ShowcaseEventListener mShowcaseEventListener;

    private View.OnClickListener mBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mShowcaseEventListener != null) {
                mShowcaseEventListener.onAcceptanceBtnClick();
            }
            mShowcaseCirclesView.stopAnimation();
            if (getParent() != null && getParent() instanceof ViewGroup) {
                ((ViewGroup) getParent()).removeView(ShowcaseView.this);
            }
        }
    };

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

    private void init() {
        setOnTouchListener(this);

        mButtonDefaultText = getContext().getString(R.string.text_btn_ok);

        mBtnDefaultTextSize = getContext().getResources().getDimension(R.dimen.btn_default_text_size);
        mMsgDefaultTextSize = getContext().getResources().getDimension(R.dimen.msg_default_text_size);

        mShowcaseCirclesView = new CirclesCanvasView(getContext());
        mShowcaseCirclesView.setOnViewLocationCalculatedListener(this);

        mDefaultMargin = (int) getResources().getDimension(R.dimen.default_margin);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mShowcaseCirclesView, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mShowcaseEventListener != null) {
            mShowcaseEventListener.onShowcaseShown();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mShowcaseEventListener != null) {
            mShowcaseEventListener.onShowcaseDismiss();
        }
    }

    /**
     * @param listener set {@link ShowcaseEventListener}
     * @return instance
     */
    public ShowcaseView setShowcaseEventListener(ShowcaseEventListener listener) {
        this.mShowcaseEventListener = listener;
        return this;
    }

    /**
     * @param view View which we'l have focus for. Around this view it'll be calculated
     *             transparent region
     * @return instance
     */
    public ShowcaseView addTargetView(final View view) {
        this.mShowcaseCirclesView.addTargetView(view);
        return this;
    }

    /**
     * @param params User params with UI settings {@link ShowcaseParams}
     * @return instance
     */
    public ShowcaseView setShowcaseParams(final ShowcaseParams params) {
        this.params = params;
        return this;
    }

    /**
     * Start drawing showcase view focused on target in this activity window.
     *
     * @param activity current activity
     */
    public void show(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Activity must not be null!");
        }
        if (getParent() != null && getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(ShowcaseView.this);
        }
        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);
    }

    public void startAnimation() {
        this.mShowcaseCirclesView.startAnimation();
    }

    public void stopAnimation() {
        this.mShowcaseCirclesView.stopAnimation();
    }

    @Override
    public void onViewLocated(Part locationOfFocus) {

        mShowcaseCirclesView.initViewWithParams(params);

        //just randomly chosen
        int textBtnId = 5;
        int textId = 10;

        int btnTextColor = params.getButtonTextColorRes() == -1 ? Color.BLACK :
                ContextCompat.getColor(getContext(), params.getButtonTextColorRes());
        int mgsTextColor = params.getMessageTextColorRes() == -1 ? Color.BLACK :
                ContextCompat.getColor(getContext(), params.getMessageTextColorRes());


        mButton = new TextView(getContext());
        mButton.setText(!TextUtils.isEmpty(params.getButtonText()) ? params.getButtonText() : mButtonDefaultText);
        mButton.setId(textBtnId);
        mButton.setTextColor(btnTextColor);
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, params.getButtonTextSize() > 0 ? params.getButtonTextSize() : mBtnDefaultTextSize);
        mButton.setTypeface(params.getButtonTypeface() != null ? params.getButtonTypeface() : Typeface.DEFAULT_BOLD);
        mButton.setOnClickListener(mBtnClickListener);

        if (!TextUtils.isEmpty(params.getMessageText())) {
            mInfoText = new TextView(getContext());
            mInfoText.setId(textId);
            mInfoText.setText(params.getMessageText());
            mInfoText.setTextColor(mgsTextColor);
            mInfoText.setTextSize(TypedValue.COMPLEX_UNIT_PX, params.getMessageTextSize() > 0 ? params.getMessageTextSize() : mMsgDefaultTextSize);
            mInfoText.setGravity(Gravity.CENTER);
            mInfoText.setTypeface(params.getMessageTypeface() != null ? params.getMessageTypeface() : Typeface.DEFAULT);
        }
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (params.getButtonMargins() != null && params.getButtonMargins().length == 4) {
            buttonParams.setMargins(params.getButtonMargins()[0], params.getButtonMargins()[1],
                    params.getButtonMargins()[2], params.getButtonMargins()[3]);
        } else {
            buttonParams.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin);
        }

        if (params.getMessageMargins() != null && params.getMessageMargins().length == 4) {
            buttonParams.setMargins(params.getMessageMargins()[0], params.getMessageMargins()[1],
                    params.getMessageMargins()[2], params.getMessageMargins()[3]);
        } else {
            textParams.setMargins(mDefaultMargin, mDefaultMargin, mDefaultMargin, mDefaultMargin);
        }

        switch (locationOfFocus) {
            case TOP_LEFT:
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                textParams.addRule(RelativeLayout.ABOVE, textBtnId);
                break;
            case TOP_RIGHT:
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                textParams.addRule(RelativeLayout.ABOVE, textBtnId);
                break;
            case BOTTOM_LEFT:
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                textParams.addRule(RelativeLayout.BELOW, textBtnId);
                break;
            case BOTTOM_RIGHT:
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                textParams.addRule(RelativeLayout.BELOW, textBtnId);
                break;
        }

        mButton.setLayoutParams(buttonParams);
        addView(mButton, buttonParams);

        if (!TextUtils.isEmpty(params.getMessageText())) {
            mInfoText.setLayoutParams(textParams);
            addView(mInfoText, textParams);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ObjectAnimator.ofFloat(mButton, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(400)
                .start();
        return true;
    }
}
