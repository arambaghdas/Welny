package com.call.welny.views;

public interface RegisterView extends BaseView {
    default void showGetCodeSuccessResponse(String message) { }
    default void showVerifyCodeSuccessResponse(String message) { }
    default void onTimerFinish() { }
    default void onSignUpSuccessResponse() {}
    default void setRegistrationState(int state) {}
    void showProgressBar();
    void hideProgressBar();
}
