package com.massage.welny.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.massage.welny.R;
import com.massage.welny.model.NetworkRequest;
import com.massage.welny.util.CheckNetwork;
import com.massage.welny.views.BaseView;
import com.massage.welny.views.RegisterView;
import com.massage.welny.views.RequestView;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import static android.util.Patterns.EMAIL_ADDRESS;

public class RegisterPresenter implements RequestView {

    private Activity activity;
    private BaseView baseView;
    private NetworkRequest networkRequest;
    private static Disposable timer;
    private static int secondsTimer;

    public RegisterPresenter(Activity activity, BaseView baseView) {
        this.activity = activity;
        this.baseView = baseView;
        this.networkRequest = new NetworkRequest(this, activity);
    }

    public void performGetCodeAgain(String number) {
        sendGetCodeRequest(number);
    }

    public void performGetCode(String number) {
        if (timer == null) {
            if (CheckNetwork.isNetworkConnected(activity)) {
                startVerifyPinTimer();
                networkRequest.getCodeRequest(activity, number);
            } else {
                baseView.showToastMessage(activity.getString(R.string.no_network));
            }
        } else {
            baseView.showErrorView();
            baseView.showErrorResponse(activity.getString(R.string.request_code_error_message));
            baseView.disableRegistration();
            ((RegisterView) baseView).setRegistrationState(View.VISIBLE);
            startPinTimer();
        }
    }

    private void sendGetCodeRequest(String number) {
        if (CheckNetwork.isNetworkConnected(activity)) {
            startVerifyPinTimer();
            networkRequest.getCodeRequest(activity, number);
        } else {
            baseView.showToastMessage(activity.getString(R.string.no_network));
        }
    }

    public void checkCode(Context mContext, String phone, String code) {
        if (code.length() == 5) {
            verifyCode(mContext, phone, code);
        } else {
            baseView.showErrorResponse("");
        }
    }

    private void verifyCode(Context mContext, String phone, String code) {
        if (CheckNetwork.isNetworkConnected(activity)) {
            networkRequest.verifyCodeRequest(mContext, phone, code);
        } else {
            baseView.showToastMessage(activity.getString(R.string.no_network));
        }
    }

    ///////////////////////// Verify code response ///////////////////////////////////////////////////////

    @Override
    public void onVerifyCodeSuccessResponse(String message) {
        ((RegisterView) baseView).hideProgressBar();
        baseView.hideErrorView();
        ((RegisterView) baseView).showVerifyCodeSuccessResponse(message);
    }

    @Override
    public void onVerifyCodeFailResponse(String message) {
        ((RegisterView) baseView).hideProgressBar();
        baseView.showErrorView();
        baseView.showErrorResponse(message);
    }

    @Override
    public void onVerifyCodeSignUpResponse() {
        ((RegisterView) baseView).hideProgressBar();
        baseView.hideErrorView();
        ((RegisterView) baseView).onSignUpSuccessResponse();
    }

    ///////////////////////// Get code response ///////////////////////////////////////////////////////

    @Override
    public void onGetCodeSuccessResponse(String message) {
        baseView.showErrorView();
        ((RegisterView) baseView).hideProgressBar();
        ((RegisterView) baseView).showGetCodeSuccessResponse(message);
    }

    @Override
    public void onGetCodeFailResponse(String message, int code) {
        ((RegisterView) baseView).hideProgressBar();
        baseView.showErrorView();
        baseView.showErrorResponse(message);
        if (code == 20) {
            baseView.showErrorView();
            baseView.showErrorResponse(activity.getString(R.string.request_code_error_message));
            baseView.disableRegistration();
            ((RegisterView) baseView).setRegistrationState(View.VISIBLE);
            startPinTimer();
        } else {
            baseView.setMessage(activity.getString(R.string.get_code_again));
        }
    }

    private void startVerifyPinTimer() {
        if (timer == null) {
            baseView.disableRegistration();
            ((RegisterView) baseView).setRegistrationState(View.VISIBLE);
            startPinTimer();
        }
    }

    private void startPinTimer() {
        if (timer != null) {
            timer.dispose();
        } else {
            secondsTimer = 60;
        }

        String message = activity.getString(R.string.get_code_again)
                + " (:" + String.valueOf(secondsTimer)  + ")";
        baseView.setMessage(message);
        timer = Observable.just(true)
                .delay(1, TimeUnit.SECONDS)
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    secondsTimer--;
                    String message1 = activity.getString(R.string.get_code_again)
                            + " (:" + String.valueOf(secondsTimer)  + ")";
                    baseView.setMessage(message1);
                    if (secondsTimer < 1) {
                        baseView.hideErrorView();
                        baseView.setMessage(activity.getString(R.string.get_code_again));
                        timer.dispose();
                        timer = null;
                        ((RegisterView) baseView).onTimerFinish();
                    }
                });
    }

    //////////////////////////////// Registration //////////////////////////////////////////////

    public boolean checkRegistrationInput(String name, String srName, String email) {
        if (!name.isEmpty() && !srName.isEmpty() && !email.isEmpty()) {
            if (EMAIL_ADDRESS.matcher(email).matches()) {
                baseView.enableRegistration();
                baseView.showErrorResponse("");
                baseView.hideErrorView();
                return true;
            } else {
                baseView.disableRegistration();
                baseView.showErrorResponse(activity.getString(R.string.incorrect_email_format));
                baseView.showErrorView();
                return false;
            }
        } else {
            if (!email.isEmpty() && !EMAIL_ADDRESS.matcher(email).matches()) {
                baseView.disableRegistration();
                baseView.showErrorResponse(activity.getString(R.string.incorrect_email_format));
                baseView.showErrorView();
                return false;
            } else {
                baseView.disableRegistration();
                baseView.showErrorResponse("");
                baseView.hideErrorView();
                return true;
            }
        }
    }

    public void validateInput(String name, String srName, String email) {
        if (!name.isEmpty() && !srName.isEmpty() && !email.isEmpty()) {
            baseView.enableRegistration();
        } else {
            baseView.disableRegistration();
        }
    }

    public void hideErrorView() {
        baseView.showErrorResponse("");
        baseView.hideErrorView();
    }

    public void performRegistration(Context mContext, String phone, String loginCode, String name, String srName, String email) {
        if (CheckNetwork.isNetworkConnected(activity)) {
            baseView.disableRegistration();
            ((RegisterView) baseView).showProgressBar();
            networkRequest.registrationRequest(mContext, phone, loginCode, name, srName, email);
        } else {
            baseView.showToastMessage(activity.getString(R.string.no_network));
        }
    }

    @Override
    public void onRegistrationSuccessResponse() {
        baseView.enableRegistration();
        ((RegisterView) baseView).hideProgressBar();
        baseView.hideErrorView();
        baseView.setMessage(activity.getString(R.string.registration_complete));
        ((RegisterView) baseView).onSignUpSuccessResponse();
    }

    @Override
    public void onRegistrationFailResponse(String message) {
        ((RegisterView) baseView).hideProgressBar();
        baseView.showErrorView();
        baseView.showErrorResponse(message);
        baseView.disableRegistration();
    }
}
