package com.call.welny;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.call.welny.log.FileUtils;
import com.call.welny.presenter.GetUserInfoPresenter;
import com.call.welny.util.Links;
import com.call.welny.util.Preferences;
import com.call.welny.views.UserInfoView;

import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends AppCompatActivity implements UserInfoView {

    private WebView webView;
    private ProgressBar progressBar;
    private String link;
    boolean auth;
    private GetUserInfoPresenter getUserInfoPresenter;

    @BindView(R.id.rl_arrow_back) RelativeLayout rlArrowBack;
    @BindView(R.id.rl_banner) RelativeLayout rlBanner;
    @BindView(R.id.tv_header) TextView tvHeader;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_welny);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            link = b.getString("link");
            auth = b.getBoolean("auth");
            tvHeader.setText(b.getString("title"));

            if (link.equals(Links.ORDER_MASSAGE_URL)) {
                rlBanner.setVisibility(View.GONE);
            } else {
                rlBanner.setVisibility(View.VISIBLE);
            }
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

        webView.setWebChromeClient(new WebChromeClient());

        if (auth) {
            String value = Preferences.getUserSession(this);
            byte[] data = value.getBytes(Charset.forName("UTF-8"));
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);

            String newUrl = link + "/?session=" + base64;
            webView.loadUrl(newUrl);
        } else {
            webView.loadUrl(link);
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("onConsoleMessage", consoleMessage.message());
                FileUtils.writeFileOnInternalStorage(getBaseContext(), consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("onConsoleMessage", "onReceivedSslError: " + error.getUrl());
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url){
                Log.d("onConsoleMessage", "onPageFinished: " + url);
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                FileUtils.writeFileOnInternalStorage(getBaseContext(), url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.d("onConsoleMessage", "shouldOverrideUrlLoading: " + url);
                webView.loadUrl(url);
                return true;
            }
        });

    }

    @OnClick(R.id.rl_arrow_back)
    public void onBack() {
        navigateBack();
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
        progressBar.setVisibility(View.VISIBLE);
        if (auth) {
            getUserInfoPresenter.sendGetUserRequest();
        } else {
            progressBar.setVisibility(View.GONE);
            super.onBackPressed();
        }
    }

    @Override
    public void showToastMessage(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGetUserInfoFailResponse() {
        super.onBackPressed();
    }

    @Override
    public void showGetUserInfoSuccessResponse() {
        progressBar.setVisibility(View.GONE);
        super.onBackPressed();
    }
}
