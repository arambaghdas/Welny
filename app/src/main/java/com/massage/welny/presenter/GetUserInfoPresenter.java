package com.massage.welny.presenter;

import android.app.Activity;

import com.massage.welny.R;
import com.massage.welny.model.NetworkUserInfoRequest;
import com.massage.welny.util.CheckNetwork;
import com.massage.welny.views.RequestUserInfoView;
import com.massage.welny.views.UserInfoView;

public class GetUserInfoPresenter implements RequestUserInfoView {

    private Activity activity;
    private UserInfoView userInfo;
    private NetworkUserInfoRequest networkRequest;

    public GetUserInfoPresenter(Activity activity, UserInfoView userInfo) {
        this.activity = activity;
        this.userInfo = userInfo;
        this.networkRequest = new NetworkUserInfoRequest(this, activity);
    }

    public void sendGetUserRequest() {
        if (CheckNetwork.isNetworkConnected(activity)) {
            networkRequest.getUserInfoRequest(activity);
        } else {
            userInfo.showToastMessage(activity.getString(R.string.no_network));
        }
    }

    public void sendLogOutRequest() {
        if (CheckNetwork.isNetworkConnected(activity)) {
            networkRequest.sendLogOutRequest(activity);
        } else {
            userInfo.showToastMessage(activity.getString(R.string.no_network));
        }
    }

    @Override
    public void onGetUserSuccessResponse() {
        userInfo.showGetUserInfoSuccessResponse();
    }

    @Override
    public void onGetUserFailResponse(String message) {
        userInfo.showToastMessage(message);
    }

    @Override
    public void onLogOutSuccessResponse(String message) {
        userInfo.performLogOut();
    }

    @Override
    public void onLogOutFailResponse(String message) {
         userInfo.showGetUserInfoFailResponse();
         userInfo.showToastMessage(message);
    }

    @Override
    public void onSessionExpireResponse(String message) {
        userInfo.showToastMessage(message);
        userInfo.sessionExpire();
    }
}
