package com.call.welny.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.call.welny.R;
import com.call.welny.WelnyActivity;
import com.call.welny.log.LogsActivity;
import com.call.welny.presenter.GetUserInfoPresenter;
import com.call.welny.presenter.RegisterPresenter;
import com.call.welny.views.RegisterView;
import com.call.welny.views.UserInfoView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class VerifyCodeActivity extends AppCompatActivity implements RegisterView, UserInfoView {

    @BindView(R.id.tv_get_code) TextView tvGetCode;
    @BindView(R.id.tv_verify_code_descr) TextView tvVerifyCodeDescr;
    @BindView(R.id.tv_phone_response) TextView tvPhoneResponse;
    @BindView(R.id.pb_loading) ProgressBar pbLoading;
    @BindView(R.id.ed_phone_code) EditText edPhoneCode;
    @BindView(R.id.rl_arrow_back) RelativeLayout rlArrowBack;
    @BindView(R.id.fy_top_view) FrameLayout frameLayout;

    private RegisterPresenter presenter;
    private GetUserInfoPresenter getUserInfoPresenter;
    private String phone;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        ButterKnife.bind(this);
        firstTime = true;

        edPhoneCode.requestFocus();
        phone = getIntent().getStringExtra("phone");
        Spanned descr = Html.fromHtml("Отправляя код вы полностью принимаете условия " +
                "<a href='https://welny.ru/terms/mobile.html'>пользовательского соглашения</a>" + " и " +
                "<a href='https://welny.ru/terms/privacy.html'>политики конфиденциальности</a>" +
                ", а также даете согласие на обработку персональных данных");
        tvVerifyCodeDescr.setMovementMethod(LinkMovementMethod.getInstance());
        tvVerifyCodeDescr.setText(descr);
        initObservable();

        presenter = new RegisterPresenter(this, this);
        presenter.performGetCode(phone);
        getUserInfoPresenter = new GetUserInfoPresenter(this, this);
    }

    @SuppressLint("CheckResult")
    private void initObservable() {
        RxTextView
                .textChanges(edPhoneCode)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(input -> {
                    if (!firstTime) {
                        presenter.checkCode(this, phone, input);
                    } else {
                        firstTime = false;
                    }
                });
    }

    @OnClick(R.id.rl_arrow_back)
    public void onBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.fy_top_view)
    public void openLogs() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_get_code)
    public void getCode() {
        presenter.performVerifyGetCode(phone);
    }

    @Override
    public void onTimerFinish() {
        enableRegistration();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGetUserInfoSuccessResponse() {
        Intent intent = new Intent(this, WelnyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void setMessage(String message) {
        tvGetCode.setText(message);
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
        tvPhoneResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorView() {
        tvPhoneResponse.setVisibility(View.GONE);
    }

    @Override
    public void setRegistrationState(int state) {
        tvGetCode.setVisibility(state);
    }

    @Override
    public void showErrorResponse(String message) {
        tvPhoneResponse.setText(message);
    }

    @Override
    public void showGetCodeSuccessResponse(String message) {
        tvPhoneResponse.setText(message);
    }

    @Override
    public void onSignUpSuccessResponse() {
        getUserInfoPresenter.sendGetUserRequest();
    }

    @Override
    public void showVerifyCodeSuccessResponse(String message) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("loginCode", edPhoneCode.getText().toString());
        startActivity(intent);
    }

    @Override
    public void enableRegistration() {
        tvGetCode.setEnabled(true);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvGetCode.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_enable_round_corner));
        } else {
            tvGetCode.setBackground( getResources().getDrawable(R.drawable.bg_enable_round_corner));
        }
    }

    @Override
    public void disableRegistration() {
        tvGetCode.setEnabled(false);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvGetCode.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_disable_round_corner));
        } else {
            tvGetCode.setBackground( getResources().getDrawable(R.drawable.bg_disable_round_corner));
        }
    }


}
