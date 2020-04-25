package com.massage.welny.views;

public interface RequestView {
    void onGetCodeSuccessResponse(String message);
    void onGetCodeFailResponse(String message, int code);
    void onVerifyCodeSuccessResponse(String message);
    void onVerifyCodeFailResponse(String message);
    void onVerifyCodeSignUpResponse();
    void onRegistrationSuccessResponse();
    void onRegistrationFailResponse(String message);
}
