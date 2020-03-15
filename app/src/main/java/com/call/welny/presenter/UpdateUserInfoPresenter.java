package com.call.welny.presenter;

import android.app.Activity;
import com.call.welny.R;
import com.call.welny.model.NetworkUpdateUserInfoRequest;
import com.call.welny.util.CheckNetwork;
import com.call.welny.views.RequestUpdateUserInfoView;
import com.call.welny.views.UpdateAccountView;

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
}
