package com.massage.welny.views;


public interface RequestUpdateUserInfoView {
    void onUpdateUserInfoSuccessResponse(String message);
    void onUpdateFailUserInfoResponse(String message);
    void onSessionExpireResponse(String message);
}
