package com.library.showcase;

/**
 * Notify about all events related to user interaction with showcase view
 */
public interface ShowcaseEventListener {

    void onShowcaseDismiss();

    void onShowcaseShown();

    void onAcceptanceBtnClick();
}
