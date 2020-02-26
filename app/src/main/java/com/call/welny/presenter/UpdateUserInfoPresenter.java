package com.call.welny.presenter;

import android.app.Activity;

import com.call.welny.R;
import com.call.welny.model.NetworkUpdateUserInfoRequest;
import com.call.welny.model.NetworkUserInfoRequest;
import com.call.welny.object.GetUserInfo;
import com.call.welny.util.CheckNetwork;
import com.call.welny.views.BaseView;
import com.call.welny.views.RequestUpdateUserInfoView;
import com.call.welny.views.RequestUserInfoView;
import com.call.welny.views.UserInfoView;

public class UpdateUserInfoPresenter implements RequestUpdateUserInfoView {

    private Activity activity;
    private BaseView baseView;
    private NetworkUpdateUserInfoRequest networkRequest;

    public UpdateUserInfoPresenter(Activity activity, BaseView baseView) {
        this.activity = activity;
        this.baseView = baseView;
        this.networkRequest = new NetworkUpdateUserInfoRequest(this, activity);
    }

    public void sendUpdateUserInfoRequest(String name, String surname, String email) {
        if (CheckNetwork.isNetworkConnected(activity)) {
            networkRequest.sendUpdateUserInfoRequest(activity, name, surname, email);
        } else {
            baseView.showToastMessage(activity.getString(R.string.no_network));
        }
    }

    @Override
    public void onUpdateUserInfoSuccessResponse(String message) {
        baseView.showSuccessResponse(message);
    }

    @Override
    public void onUpdateFailUserInfoResponse(String message) {
        baseView.showToastMessage(message);
    }
}
