package com.massage.welny.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.massage.welny.R;
import com.massage.welny.WelnyActivity;
import com.massage.welny.presenter.GetUserInfoPresenter;
import com.massage.welny.presenter.RegisterPresenter;
import com.massage.welny.util.Analytics;
import com.massage.welny.views.RegisterView;
import com.massage.welny.views.UserInfoView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RegisterActivity extends AppCompatActivity implements RegisterView, UserInfoView {

    @BindView(R.id.tv_registration_error) TextView tvRegistrationError;
    @BindView(R.id.tv_registration_complete) TextView tvRegistrationComplete;
    @BindView(R.id.ed_name) EditText edName;
    @BindView(R.id.ed_sr_name) EditText edSrName;
    @BindView(R.id.ed_email) EditText edEmail;
    @BindView(R.id.pb_loading) ProgressBar pbLoading;
    @BindView(R.id.rl_arrow_back) RelativeLayout rlArrowBack;

    private String phone, loginCode;
    private RegisterPresenter presenter;
    private GetUserInfoPresenter getUserInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        phone = getIntent().getStringExtra("phone");
        loginCode = getIntent().getStringExtra("loginCode");
        edName.requestFocus();

        presenter = new RegisterPresenter(this, this);
        getUserInfoPresenter = new GetUserInfoPresenter(this, this);
        initObservable();
    }

    @OnClick(R.id.rl_arrow_back)
    public void onBack() {
        super.onBackPressed();
    }

    @SuppressLint("CheckResult")
    private void initObservable() {
        RxTextView
                .textChanges(edName)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(input -> {
                    presenter.validateInput(input,
                            edSrName.getText().toString(), edEmail.getText().toString());
                });

        RxTextView
                .textChanges(edSrName)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(input -> {
                    presenter.validateInput(edName.getText().toString(),
                            input, edEmail.getText().toString());
                });

        RxTextView
                .textChanges(edEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(input -> {
                    presenter.validateInput(edName.getText().toString(),
                            edSrName.getText().toString(), input);
                    presenter.hideErrorView();
                });

        edEmail.setOnFocusChangeListener((arg0, hasfocus) -> {
            if (!hasfocus) {
                checkRegistrationInput();
            }
        });

        RxView.clicks(tvRegistrationComplete)
                .filter(o -> checkRegistrationInput())
                .subscribe(this::performRegistration);
    }

    public void performRegistration(Object o) {
        presenter.performRegistration(this, phone, loginCode, edName.getText().toString(),
                edSrName.getText().toString(), edEmail.getText().toString());
    }

    public boolean checkRegistrationInput() {
        return presenter.checkRegistrationInput(edName.getText().toString(),
                        edSrName.getText().toString(),  edEmail.getText().toString());
    }

    @OnClick(R.id.fy_top_view)
    public void openLogs() {
        //Intent intent = new Intent(this, LogsActivity.class);
        //startActivity(intent);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGetUserInfoSuccessResponse() {
        Analytics.sendRegistrationCompletedEvent(this);
        Intent intent = new Intent(this, WelnyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onSignUpSuccessResponse() {
        getUserInfoPresenter.sendGetUserRequest();
    }

    @Override
    public void setMessage(String message) {
        tvRegistrationComplete.setText(message);
    }

    @Override
    public void showProgressBar() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        tvRegistrationError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorView() {
        tvRegistrationError.setVisibility(View.GONE);
    }

    @Override
    public void showErrorResponse(String message) {
        tvRegistrationError.setText(message);
    }

    @Override
    public void enableRegistration() {
        tvRegistrationComplete.setEnabled(true);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvRegistrationComplete.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_enable_round_corner));
        } else {
            tvRegistrationComplete.setBackground( getResources().getDrawable(R.drawable.bg_enable_round_corner));
        }
    }

    @Override
    public void disableRegistration() {
        tvRegistrationComplete.setEnabled(false);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvRegistrationComplete.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_disable_round_corner));
        } else {
            tvRegistrationComplete.setBackground( getResources().getDrawable(R.drawable.bg_disable_round_corner));
        }
    }

}
