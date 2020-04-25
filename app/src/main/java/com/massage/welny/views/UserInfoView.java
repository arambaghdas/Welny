package com.massage.welny.views;

public interface UserInfoView {
    void showToastMessage(String message);
    default void showGetUserInfoSuccessResponse() {}
    default void showGetUserInfoFailResponse() {}
    default void performLogOut() { }
}
