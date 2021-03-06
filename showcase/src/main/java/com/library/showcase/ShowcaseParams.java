package com.library.showcase;

import android.graphics.Typeface;

import com.library.showcase.constants.Defaults;

public class ShowcaseParams {

    private String buttonText;
    private String messageText;

    private float buttonTextSize;
    private float messageTextSize;

    private int buttonTextColorRes = -1;
    private int messageTextColorRes = -1;

    private Typeface buttonTypeface;
    private Typeface messageTypeface;

    private int[] buttonMargins;
    private int[] messageMargins;

    private boolean showCircles = Defaults.SHOW_CIRCLES;
    private boolean animate = Defaults.ANIMATE;

    private int circlesNumber;
    private int focusCircleSize;
    private int circleWidth;

    private int backgroundColorRes = -1;
    private int circlesColorRes = -1;

    private float circlesAlpha = -1;
    private float backgroundAlpha = -1;

    String getButtonText() {
        return buttonText;
    }

    /**
     * Button to dismiss view. Default value is 'GOT IT'
     *
     * @param buttonText string will be displayed as button to dismiss
     * @return instance
     */
    public ShowcaseParams setButtonText(String buttonText) {
        this.buttonText = buttonText;
        return this;
    }

    String getMessageText() {
        return messageText;
    }

    /**
     * Explanation text for focused view and any action with it
     *
     * @param messageText string will be displayed as explanation
     * @return instance
     */
    public ShowcaseParams setMessageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    float getButtonTextSize() {
        return buttonTextSize;
    }

    /**
     * Dismiss button text size. Default value is 15 dp, converted to px due to screen resolution
     *
     * @param buttonTextSize text size in px
     * @return instance
     */
    public ShowcaseParams setButtonTextSize(float buttonTextSize) {
        this.buttonTextSize = buttonTextSize;
        return this;
    }

    float getMessageTextSize() {
        return messageTextSize;
    }

    /**
     * Explanation message text size. Default value is 15 dp, converted to px due to screen resolution
     *
     * @param messageTextSize text size in px
     * @return instance
     */
    public ShowcaseParams setMessageTextSize(float messageTextSize) {
        this.messageTextSize = messageTextSize;
        return this;
    }

    int getButtonTextColorRes() {
        return buttonTextColorRes;
    }

    /**
     * @param buttonTextColorRes color resource ID for dismiss button text
     * @return instance
     */
    public ShowcaseParams setButtonTextColorRes(int buttonTextColorRes) {
        this.buttonTextColorRes = buttonTextColorRes;
        return this;
    }

    int getMessageTextColorRes() {
        return messageTextColorRes;
    }

    /**
     * @param messageTextColorRes color resource ID for explanation message
     * @return instance
     */
    public ShowcaseParams setMessageTextColorRes(int messageTextColorRes) {
        this.messageTextColorRes = messageTextColorRes;
        return this;
    }

    Typeface getButtonTypeface() {
        return buttonTypeface;
    }

    /**
     * @param buttonTypeface Typeface instance for dismiss button, default value is {@link Typeface#DEFAULT}
     * @return instance
     */
    public ShowcaseParams setButtonTypeface(Typeface buttonTypeface) {
        this.buttonTypeface = buttonTypeface;
        return this;
    }

    Typeface getMessageTypeface() {
        return messageTypeface;
    }

    /**
     * @param messageTypeface Typeface instance for explanation message, default value is {@link Typeface#DEFAULT}
     * @return instance
     */
    public ShowcaseParams setMessageTypeface(Typeface messageTypeface) {
        this.messageTypeface = messageTypeface;
        return this;
    }

    int[] getButtonMargins() {
        return buttonMargins;
    }

    /**
     * @param buttonMargins layout margins for explanation message. Default value is
     *                      [50dp, 50dp, 50dp, 50dp]. Should be array with 4 elements
     * @return instance
     */
    public ShowcaseParams setButtonMargins(int[] buttonMargins) {
        this.buttonMargins = buttonMargins;
        return this;
    }

    int[] getMessageMargins() {
        return messageMargins;
    }

    /**
     * @param messageMargins layout margins for explanation message. Default value is
     *                       [50dp, 50dp, 50dp, 50dp]. Should be array with 4 elements
     * @return instance
     */
    public ShowcaseParams setMessageMargins(int[] messageMargins) {
        this.messageMargins = messageMargins;
        return this;
    }

    boolean isShowCircles() {
        return showCircles;
    }

    /**
     * @param showCircles if true - circles will be drawn, will not otherwise
     * @return instance
     */
    public ShowcaseParams setShowCircles(boolean showCircles) {
        this.showCircles = showCircles;
        return this;
    }

    boolean isAnimate() {
        return animate;
    }

    /**
     * @param animate if true - circles will be drawn with animation, will not otherwise
     * @return instance
     */
    public ShowcaseParams setAnimate(boolean animate) {
        this.animate = animate;
        return this;
    }

    int getFocusCircleSize() {
        return focusCircleSize;
    }

    /**
     * @param focusCircleSize size of transparent circle for
     * @return instance
     */
    public ShowcaseParams setFocusCircleSize(int focusCircleSize) {
        this.focusCircleSize = focusCircleSize;
        return this;
    }

    int getCircleWidth() {
        return circleWidth;
    }

    /**
     * @param circleWidth circle stroke width
     * @return instance
     */
    public ShowcaseParams setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
        return this;
    }

    int getBackgroundColorRes() {
        return backgroundColorRes;
    }

    /**
     * @param backgroundColorRes color resource for background view, should be R.color.{sample_res}
     * @return instance
     */
    public ShowcaseParams setBackgroundColorRes(int backgroundColorRes) {
        this.backgroundColorRes = backgroundColorRes;
        return this;
    }

    int getCirclesColorRes() {
        return circlesColorRes;
    }

    /**
     * @param circlesColorRes color resource for circles, should be R.color.{sample_res}
     * @return instance
     */
    public ShowcaseParams setCirclesColorRes(int circlesColorRes) {
        this.circlesColorRes = circlesColorRes;
        return this;
    }

    float getCirclesAlpha() {
        return circlesAlpha;
    }

    /**
     * @param circlesAlpha
     * @return
     */
    public ShowcaseParams setCirclesAlpha(float circlesAlpha) {
        this.circlesAlpha = circlesAlpha > Defaults.MAX_ALPHA ? Defaults.MAX_ALPHA :
                circlesAlpha < Defaults.MIN_ALPHA ? Defaults.MIN_ALPHA : circlesAlpha;
        return this;
    }

    float getBackgroundAlpha() {
        return backgroundAlpha;
    }

    /**
     * @param backgroundAlpha background view alpha
     * @return instance
     */
    public ShowcaseParams setBackgroundAlpha(float backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha > Defaults.MAX_ALPHA ? Defaults.MAX_ALPHA :
                backgroundAlpha < Defaults.MIN_ALPHA ? Defaults.MIN_ALPHA : backgroundAlpha;
        return this;
    }

    int getCirclesNumber() {
        return circlesNumber;
    }

    /**
     * Set number of circles for showcase in range of {@link Defaults#CIRCLES_NUMBER_MIN}
     * and {@link Defaults#CIRCLES_NUMBER_MAX}
     * <p>
     * If not set, the value is {@link Defaults#CIRCLES_NUMBER_DEFAULT}
     *
     * @param number circles number
     * @return instance
     */
    public ShowcaseParams setCirclesNumber(int number) {
        this.circlesNumber = number < Defaults.CIRCLES_NUMBER_MIN ? Defaults.CIRCLES_NUMBER_MIN :
                number > Defaults.CIRCLES_NUMBER_MAX ? Defaults.CIRCLES_NUMBER_MAX :
                        number;
        return this;
    }
}
