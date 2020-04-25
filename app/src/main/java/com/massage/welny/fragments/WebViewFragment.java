package com.massage.welny.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.massage.welny.R;
import com.massage.welny.WelnyActivity;
import com.massage.welny.log.FileUtils;
import com.massage.welny.presenter.GetUserInfoPresenter;
import com.massage.welny.presenter.WebViewPresenter;
import com.massage.welny.util.Analytics;
import com.massage.welny.views.UserInfoView;
import com.massage.welny.views.WebSiteView;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class WebViewFragment extends Fragment implements UserInfoView, WebSiteView {

    @BindView(R.id.rl_banner)
    RelativeLayout rlBanner;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.fy_top_view)
    FrameLayout topView;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;
    private Context mContext;
    private WebViewPresenter webViewPresenter;
    private String link;
    private boolean auth;
    private GetUserInfoPresenter getUserInfoPresenter;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_welny, container, false);
        ButterKnife.bind(this, view);
        mContext = view.getContext();
        webViewPresenter = new WebViewPresenter(getActivity(), this);
        getUserInfoPresenter = new GetUserInfoPresenter(getActivity(), this);
        topView.setVisibility(View.GONE);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            link = bundle.getString("link");
            auth = bundle.getBoolean("auth");
            tvHeader.setText(bundle.getString("title"));
            webViewPresenter.configureBanner(link);
        }
        openWebView();
        return view;
    }

    private void openWebView() {
        WebSettings webSettings = webView.getSettings();
        //enable JavaScript in webview
        webSettings.setJavaScriptEnabled(true);
        //Enable and setup JS localStorage
        webSettings.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new WebAppInterface(mContext), "Android");
        webView.loadUrl(webViewPresenter.getUrl(link, auth));

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("onConsoleMessage", consoleMessage.message());
                FileUtils.writeFileOnInternalStorage(mContext, consoleMessage.message());
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
                hideProgressBar();
                FileUtils.writeFileOnInternalStorage(mContext, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.d("onConsoleMessage", "shouldOverrideUrlLoading: " + url);
                webView.loadUrl(url);
                return true;
            }
        });
    }

    private void hideProgressBar() {
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

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void sessionExpire() {
        WelnyActivity activity = (WelnyActivity) getActivity();
        if (activity != null) {
            activity.sessionExpire();
        }
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void bookingCanceled() {
            Analytics.sendOrderCanceledEvent();
            getUserInfoPresenter.sendGetUserRequest();
        }
    }
}