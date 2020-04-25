package com.massage.welny.presenter;

import android.app.Activity;
import com.massage.welny.R;
import com.massage.welny.model.NetworkUpdateUserInfoRequest;
import com.massage.welny.util.CheckNetwork;
import com.massage.welny.views.RequestUpdateUserInfoView;
import com.massage.welny.views.UpdateAccountView;

public class UpdateUserInfoPresenter implements RequestUpdateUserInfoView {

    private Activity activity;
    private UpdateAccountView updateAccountView;
    private NetworkUpdateUserInfoRequest networkRequest;

    public UpdateUserInfoPresenter(Activity activity, UpdateAccountView updateAccountView) {
        this.activity = activity;
        this.updateAccountView = updateAccountView;
        this.networkRequest = new NetworkUpdateUserInfoRequest(this, activity);
    }

    public void sendUpdateUserInfoRequest(String name, String surname, String email) {
        if (CheckNetwork.isNetworkConnected(activity)) {
            networkRequest.sendUpdateUserInfoRequest(activity, name, surname, email);
            updateAccountView.showProgressBar();
            updateAccountView.setMessage("");
        } else {
            updateAccountView.showToastMessage(activity.getString(R.string.no_network));
        }
    }

    @Override
    public void onUpdateUserInfoSuccessResponse(String message) {
        updateAccountView.setMessage(activity.getString(R.string.update_account));
        updateAccountView.showSuccessResponse(message);
        updateAccountView.hideProgressBar();
    }

    @Override
    public void onUpdateFailUserInfoResponse(String message) {
        updateAccountView.setMessage(activity.getString(R.string.update_account));
        updateAccountView.showToastMessage(message);
        updateAccountView.hideProgressBar();
    }

    @Override
    public void onSessionExpireResponse(String message) {
        updateAccountView.showToastMessage(message);
        updateAccountView.hideProgressBar();
        updateAccountView.sessionExpire();
    }
}
