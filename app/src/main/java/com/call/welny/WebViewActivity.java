package com.call.welny;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.call.welny.util.Links;
import com.call.welny.util.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void openWebView() {

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        //webView.setWebViewClient(webViewClient);

        webView.loadUrl(link);

        CookieManager cookieManager = CookieManager.getInstance();
        //cookieManager.setCookie(link, "session=" + Preferences.getUserSession(this));

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
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
