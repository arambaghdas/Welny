package com.call.welny.views;

public interface UserInfoView {
    void showToastMessage(String message);
    void showGetUserInfoSuccessResponse();
    default void showGetUserInfoFailResponse() {}
    default void performLogOut() { }
}
