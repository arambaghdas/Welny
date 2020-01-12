package com.call.welny.presenter;

import android.app.Activity;
import android.util.Log;

import com.call.welny.R;
import com.call.welny.views.BaseView;
public class VerifyCodePresenter {

    private Activity activity;
    private BaseView baseView;
    private boolean firstTime = true;

    public VerifyCodePresenter(Activity activity, BaseView baseView) {
        this.activity = activity;
        this.baseView = baseView;
    }

    public void verifyPhoneNumber(String number) {
        if (firstTime) {
            firstTime = false;
            baseView.disableRegistration();
        } else {
            checkPhoneNumber(getPhoneNumber(number));
        }
    }

    private void checkPhoneNumber(String number) {
        Log.v("numberWithCode", "numberWithCode: " + number);
        if (!number.isEmpty()) {
            if (number.length() > 3 && !(number.startsWith("+79"))) {
                baseView.showErrorView();
                baseView.showErrorResponse(activity.getString(R.string.should_start_with_seven_nine));
                baseView.disableRegistration();
            } else if (!number.matches("[0-9+]+")) {
                baseView.showErrorView();
                baseView.showErrorResponse(activity.getString(R.string.should_contain_number));
                baseView.disableRegistration();
            } else if (number.length() < 12) {
                baseView.disableRegistration();
                baseView.showErrorResponse("");
                baseView.hideErrorView();
            } else {
                baseView.showErrorResponse("");
                baseView.hideErrorView();
                baseView.enableRegistration();
            }
        } else {
            baseView.disableRegistration();
            baseView.showErrorResponse("");
            baseView.hideErrorView();
        }
    }

    public String getPhoneNumber(String number) {
        return "+7" + number;
    }
}
