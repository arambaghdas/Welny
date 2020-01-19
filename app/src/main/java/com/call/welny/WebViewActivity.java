package com.call.welny;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.call.welny.util.Links;
import com.call.welny.util.Preferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cookie;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private String link;

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
            tvHeader.setText(b.getString("title"));
        }

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        openWebView();
    }

    private void openWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        String cookie = "session=" + Preferences.getUserSession(this);


        webView.setWebChromeClient(new WebChromeClient());

        CookieSyncManager.createInstance(WebViewActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.setCookie(Links.BASE_URL, cookie);
        CookieSyncManager.getInstance().sync();

        Map<String, String> header = new HashMap<String, String>();
        header.put("Cookie", cookie);

        webView.loadUrl(link, header);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                // Page loading started
                // Do something
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url){
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {

                Log.v("WebViewClientImpl", "url: " + url);

                if(url.indexOf("welny.ru") > -1 ) return false;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
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
        navigateBack();
    }

    private void navigateBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void setCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        removeCookie();
        cookieManager.setCookie(link, "session=" + Preferences.getUserSession(this));
        Log.v("setCookie", "session = " + Preferences.getUserSession(this));

    }

    private void removeCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(value -> {
            });
        } else {
            cookieManager.removeSessionCookie();
        }
    }
}
