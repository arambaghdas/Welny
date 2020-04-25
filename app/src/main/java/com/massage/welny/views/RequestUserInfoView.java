package com.massage.welny.views;

public interface RequestUserInfoView {
    void onGetUserSuccessResponse();
    void onGetUserFailResponse(String message);
    void onLogOutSuccessResponse(String message);
    void onLogOutFailResponse(String message);
    void onSessionExpireResponse(String message);
}
