package com.massage.welny;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.massage.welny.presenter.GetUserInfoPresenter;
import com.massage.welny.presenter.WebViewPresenter;
import com.massage.welny.register.VerifyPhoneActivity;
import com.massage.welny.util.Analytics;
import com.massage.welny.util.Preferences;
import com.massage.welny.views.UserInfoView;
import com.massage.welny.views.WebSiteView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends AppCompatActivity implements UserInfoView, WebSiteView {

    private WebView webView;
    private ProgressBar progressBar;
    private String link;
    boolean auth;
    private GetUserInfoPresenter getUserInfoPresenter;
    private WebViewPresenter webViewPresenter;

    @BindView(R.id.rl_arrow_back) RelativeLayout rlArrowBack;
    @BindView(R.id.rl_banner) RelativeLayout rlBanner;
    @BindView(R.id.tv_header) TextView tvHeader;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_welny);
        ButterKnife.bind(this);

        webViewPresenter = new WebViewPresenter(this, this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            link = bundle.getString("link");
            auth = bundle.getBoolean("auth");
            tvHeader.setText(bundle.getString("title"));
            webViewPresenter.configureBanner(link);
        }

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);

        getUserInfoPresenter = new GetUserInfoPresenter(this, this);
        openWebView();
    }

    private void openWebView() {
        WebSettings webSettings = webView.getSettings();
        //enable JavaScript in webview
        webSettings.setJavaScriptEnabled(true);
        //Enable and setup JS localStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        String newURL = webViewPresenter.getUrl(link, auth);
        webView.loadUrl(newURL);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                hideProgressBar();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick(R.id.rl_arrow_back)
    public void onBack() {
        performOnBack();
    }

    @Override
    public void onBackPressed() {
        performOnBack();
    }

    private void navigateBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            performOnBack();
        }
    }

    private void performOnBack() {
        showProgressBar();
        if (auth) {
            getUserInfoPresenter.sendGetUserRequest();
        } else {
            hideProgressBar();
            super.onBackPressed();
        }
    }

    @Override
    public void showToastMessage(String message) {
        hideProgressBar();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGetUserInfoFailResponse() {
        super.onBackPressed();
    }

    @Override
    public void sessionExpire() {
        Preferences.clearUserSession(this);
        Preferences.setUserInfo(this, null);
        Intent intent = new Intent(this, VerifyPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showGetUserInfoSuccessResponse() {
        hideProgressBar();
        super.onBackPressed();
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showBanner() {
        rlBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBanner() {
        rlBanner.setVisibility(View.GONE);
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void bookingCreated() {
            Analytics.sendOrderConfirmedEvent(mContext);
            getUserInfoPresenter.sendGetUserRequest();
            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();
        }
    }
}
