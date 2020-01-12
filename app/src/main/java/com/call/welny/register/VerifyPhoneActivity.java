package com.call.welny.register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.call.welny.R;
import com.call.welny.log.LogsActivity;
import com.call.welny.presenter.VerifyCodePresenter;
import com.call.welny.views.BaseView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.santalu.maskedittext.MaskEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class VerifyPhoneActivity extends AppCompatActivity implements BaseView {

    private VerifyCodePresenter presenter;
    @BindView(R.id.tv_get_code) TextView tvGetCode;
    @BindView(R.id.tv_phone_number_error) TextView tvPhoneNumberError;
    @BindView(R.id.ed_phone) MaskEditText edPhone;
    @BindView(R.id.rl_top_view) RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);
        ButterKnife.bind(this);

        edPhone = findViewById(R.id.ed_phone);
        edPhone.requestFocus();
        presenter = new VerifyCodePresenter(this, this);
        initObservable();
        initTouchListener();
    }

    @SuppressLint("CheckResult")
    private void initObservable() {
        RxTextView
                .textChanges(edPhone)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(input -> {
                    presenter.verifyPhoneNumber(edPhone.getRawText());
                });
    }

    @OnClick(R.id.rl_top_view)
    public void openLogs() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_get_code)
    public void getCode() {
        Intent intent = new Intent(this, VerifyCodeActivity.class);
        intent.putExtra("phone", presenter.getPhoneNumber(edPhone.getRawText()));
        startActivity(intent);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMessage(String message) {
        tvGetCode.setText(message);
    }

    @Override
    public void showErrorView() {
        tvPhoneNumberError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorView() {
        tvPhoneNumberError.setVisibility(View.GONE);
    }

    @Override
    public void showErrorResponse(String message) {
        tvPhoneNumberError.setText(message);
    }

    @Override
    public void enableRegistration() {
        tvGetCode.setEnabled(true);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_enable_round_corner));
        } else {
            tvGetCode.setBackground(getResources().getDrawable(R.drawable.bg_enable_round_corner));
        }
    }

    @Override
    public void disableRegistration() {
        tvGetCode.setEnabled(false);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_disable_round_corner));
        } else {
            tvGetCode.setBackground(getResources().getDrawable(R.drawable.bg_disable_round_corner));
        }
    }

    private void initTouchListener() {
        edPhone.setOnTouchListener((v, event) -> {
            edPhone.onTouchEvent(event);
            MaskEditText editText = (MaskEditText) v;
            String text = editText.getText().toString();
            if (text != null) {
                editText.setSelection(text.length());
            }
            return true;
        });
    }


}
