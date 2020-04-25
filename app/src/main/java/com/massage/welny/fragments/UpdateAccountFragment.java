package com.massage.welny.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.massage.welny.R;
import com.massage.welny.object.GetUserInfo;
import com.massage.welny.presenter.RegisterPresenter;
import com.massage.welny.presenter.UpdateUserInfoPresenter;
import com.massage.welny.util.Analytics;
import com.massage.welny.util.Preferences;
import com.massage.welny.views.UpdateAccountView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.santalu.maskedittext.MaskEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

@SuppressLint("ValidFragment")
public class UpdateAccountFragment extends Fragment implements UpdateAccountView {

    @BindView(R.id.tv_registration_error)
    TextView tvRegistrationError;
    @BindView(R.id.tv_update_account)
    TextView tvUpdateAccount;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_sr_name)
    EditText edSrName;
    @BindView(R.id.tv_full_name)
    TextView tvFullName;
    @BindView(R.id.tv_credit)
    TextView tvCredit;
    @BindView(R.id.ed_phone)
    MaskEditText edPhone;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private RegisterPresenter presenter;
    private UpdateUserInfoPresenter updateUserInfoPresenter;
    private GetUserInfo getUserInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_account, container, false);
        ButterKnife.bind(this, view);
        presenter = new RegisterPresenter(getActivity(), this);
        updateUserInfoPresenter = new UpdateUserInfoPresenter(getActivity(), this);
        initValues();
        initObservable();
        initTouchListener();
        return view;
    }

    private void initValues() {
        Activity activity = getActivity();
        if (activity != null) {
            getUserInfo = Preferences.getUserInfo(activity);
            if (getUserInfo != null) {
                edName.setText(getUserInfo.getName());
                edName.setSelection(getUserInfo.getName().length());
                edSrName.setText(getUserInfo.getSurname());
                edEmail.setText(getUserInfo.getEmail());
                int index = getUserInfo.getPhone().length();
                edPhone.setText(getUserInfo.getPhone().substring(2, index));
                tvFullName.setText(getUserInfo.getFullname());
                String creditStr = getString(R.string.credits).toUpperCase() + " " + getUserInfo.getCredits() + " \u20BD";
                tvCredit.setText(creditStr);
                hideFocus();
            }
        }
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

        RxView.clicks(tvUpdateAccount)
                .filter(o -> checkRegistrationInput())
                .subscribe(this::updateAccount);
    }

    private boolean checkRegistrationInput() {
        return presenter.checkRegistrationInput(edName.getText().toString(),
                edSrName.getText().toString(), edEmail.getText().toString());
    }

    private void updateAccount(Object o) {
        Analytics.sendAccountUpdateEvent();
        updateUserInfoPresenter.sendUpdateUserInfoRequest(edName.getText().toString(),
                edSrName.getText().toString(), edEmail.getText().toString());
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

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMessage(String message) {
        tvUpdateAccount.setText(message);
    }

    @Override
    public void showErrorResponse(String message) {
        hideFocus();
        tvRegistrationError.setText(message);
    }

    @Override
    public void showSuccessResponse(String message) {
        Activity activity = getActivity();
        if (activity != null) {
            getUserInfo = Preferences.getUserInfo(activity);
            if (getUserInfo != null) {
                edName.setText(getUserInfo.getName());
                edName.setSelection(getUserInfo.getName().length());
                edSrName.setText(getUserInfo.getSurname());
                tvFullName.setText(getUserInfo.getFullname());
                hideFocus();
            }
        }
    }

    @Override
    public void enableRegistration() {
        tvUpdateAccount.setEnabled(true);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvUpdateAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_enable_round_corner));
        } else {
            tvUpdateAccount.setBackground(getResources().getDrawable(R.drawable.bg_enable_round_corner));
        }
    }

    @Override
    public void disableRegistration() {
        tvUpdateAccount.setEnabled(false);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tvUpdateAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_disable_round_corner));
        } else {
            tvUpdateAccount.setBackground(getResources().getDrawable(R.drawable.bg_disable_round_corner));
        }
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
    public void showProgressBar() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pbLoading.setVisibility(View.GONE);
    }


    public void hideFocus() {
        edName.clearFocus();
        edSrName.clearFocus();
        edEmail.clearFocus();
    }
}