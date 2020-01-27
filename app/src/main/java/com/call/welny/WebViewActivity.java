package com.call.welny;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.call.welny.log.FileUtils;
import com.call.welny.object.UserInfo;
import com.call.welny.presenter.GetUserInfoPresenter;
import com.call.welny.util.Links;
import com.call.welny.util.LocalStorage;
import com.call.welny.util.Preferences;
import com.call.welny.views.UserInfoView;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends AppCompatActivity implements UserInfoView {

    private WebView webView;
    private ProgressBar progressBar;
    private String link;
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
            tvHeader.setText(b.getString("title"));
        }

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);

        getUserInfoPresenter = new GetUserInfoPresenter(this, this);
        openWebView();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openWebView() {
        /*
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        */

       // WebSettings websettings = webView.getSettings();
       // websettings.setDomStorageEnabled(true);  // Open DOM storage function
       // websettings.setJavaScriptEnabled(true);
        //websettings.setAppCacheMaxSize(1024*1024*8);
        //String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        //websettings.setAppCachePath(appCachePath);
        //websettings.setAllowFileAccess(true);    // Readable file cache
       /// websettings.setAppCacheEnabled(true);    //Turn on the H5(APPCache) caching function
       // websettings.setSaveFormData(true);

       // websettings.setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");

        //add the JavaScriptInterface so that JavaScript is able to use LocalStorageJavaScriptInterface's methods when calling "LocalStorage"
       // LocalStorageJavaScriptInterface localStorageJavaScriptInterface = new LocalStorageJavaScriptInterface(getApplicationContext());
       // localStorageJavaScriptInterface.setItem("dd", "ddd");

        //webView.addJavascriptInterface(localStorageJavaScriptInterface, "LocalStorage");

        WebSettings webSettings = webView.getSettings();
        //enable JavaScript in webview
        webSettings.setJavaScriptEnabled(true);

        //Enable and setup JS localStorage
        webSettings.setDomStorageEnabled(true);
        //those two lines seem necessary to keep data that were stored even if the app was killed.
        //webSettings.setDatabaseEnabled(true);
       // webSettings.setDatabasePath(getFilesDir().getParentFile().getPath()+"/databases/");


        webSettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);    // Readable file cache
        webSettings.setAppCacheEnabled(true);    //Turn on the H5(APPCache) caching function

        //
        webView.setWebChromeClient(new WebChromeClient());

        /*
        CookieSyncManager.createInstance(WebViewActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.setCookie(link, cookie);
        CookieSyncManager.getInstance().sync();
        */


        /*
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        Map<String, String> header = new HashMap<String, String>();
        header.put("Cookie", cookie);
        */


       // writeData();
       // webView.loadUrl("javascript:init('" + "test" + "')");

       //writeData();


        String value = Preferences.getUserSession(this);
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        String newUrl = link + "/?session=" + base64;

       webView.loadUrl(newUrl);
       //writeData();
        Log.d("onConsoleMessage", "newUrl: " + newUrl); // Prints asd

       // webView.loadUrl(link);
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
                // Page loading started
                // Do something
               // writeData();
                //writeData();
                super.onPageStarted(view, url, favicon);
               // writeData();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("onConsoleMessage", "onReceivedSslError: " + error.getUrl());
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url){
                Log.d("onConsoleMessage", "onPageFinished: " + url);
               // super.onPageFinished(view, url);
                //String cookies = CookieManager.getInstance().getCookie(url);
                //Log.v("WebViewClientImpl", "All the cookies in a string: " + cookies);
                //Log.v("WebViewClientImpl", "url: " + url);
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                FileUtils.writeFileOnInternalStorage(getBaseContext(), url);
              // writeData();


                /*
                webView.evaluateJavascript(js1, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("onConsoleMessage", "onReceiveValue: " + s); // Prints asd
                    }
                });

                */


                /*
                String key = "hello";
                String val = "world";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("localStorage.setItem('"+ key +"','"+ val +"');", null);
                } else {
                    webView.loadUrl("javascript:localStorage.setItem('"+ key +"','"+ val +"');");
                }

                 */


            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {

                Log.d("onConsoleMessage", "shouldOverrideUrlLoading: " + url);
                /*
                Log.v("WebViewClientImpl", "url: " + url);

                if(url.indexOf("welny.ru") > -1 ) return false;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                */
                return true;
            }
        });

    }

    private void injectJS() {
        try {
            webView.loadUrl("javascript:$('a').on('click',function(event){\n" +
                    "   var value = $(this).attr(\"href\");\n" +
                    "   if (typeof value != 'undefined' && " +
                    "value.indexOf('moneta.ru') ==-1 && " +
                    "value.indexOf('.fasapay.') ==-1 && " +
                    "value.indexOf('money.yandex.ru') ==-1 && " +
                    "value.indexOf('fxtm_inapp') == -1 && " +
                    "value.indexOf('my-mobile.trunk.fxtm') == -1 && " +
                    "value.indexOf('my-mobile.forextime.com') == -1 && " +
                    "value.indexOf('.paysec.') == -1 && " +
                    "value.indexOf('.alogateway.') == -1 && " +
                    "value.indexOf('.payvisionservices.') == -1 && " +
                    "value.indexOf('.skrill.') == -1 && " +
                    "value.indexOf('.webmoney.') == -1 && " +
                    "value.indexOf('.perfectmoney.') == -1 && " +
                    "value.indexOf('.bitpay.') == -1 && " +
                    "value.indexOf('.safecharge.') == -1 && " +
                    "value.indexOf('.ecommpay.') == -1 && " +
                    "value.indexOf('.nganluong.') == -1 && " +
                    "value.indexOf('.gzshop318.') == -1 && " +
                    "value.indexOf('.cardpay.') == -1 && " +
                    "value.indexOf('.globepayinc.') == -1 && " +
                    "value.indexOf('.cashu.') == -1 && " +
                    "value.indexOf('.payanyway.') == -1 && " +
                    "value.indexOf('.dixipay.') == -1 && " +
                    "value.indexOf('.scb.co.') == -1 && " +
                    "(value.lastIndexOf('http', 0) === 0 || value.lastIndexOf('//', 0) === 0 )){" +
                    "  \t\t  event.preventDefault();\n" +
                    "  \t\t  Android.openBrowser(value);\n" +
                    "  } " +
                    "});");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            progressBar.setVisibility(View.VISIBLE);
            getUserInfoPresenter.sendGetUserRequest();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void writeData(){
        String key = "welny_session";
        String value = Preferences.getUserSession(this);

        //String value2 = "\"" + value +   "\"";
       // String value2 = "\"" + value +   "\"";
        String value2 = "'" + value +   "'";

        //String value1= "\"{\"name\":\"Ivan\",\"surname\":\"Ivanov\",\"token\":\"0S8Cm24GKwig2rJSR8Qpw9so\",\"type\":\"customers\",\"uid\":3\"}\"";
        //String value1 = "\"\"name\":\"Ivan\",\"surname\":\"Ivanov\",\"token\":\"0S8Cm24GKwig2rJSR8Qpw9so\",\"type\":\"customers\",\"uid\":3\"\"";

        //UserInfo userInfo = Preferences.getUserSession(this);

        // String js = "window.localStorage.setItem('"+ key +"'='"+ value +"')";
        // String js1 = "window.localStorage.setItem(\"" + key + "\"," + value2 + ")";

        //String js1 = "window.localStorage.setItem(\"" + key + "\"," + value2 + ")";

        String js1 = "window.localStorage.setItem('"+ key +"','"+ value +"');";
        Log.d("onConsoleMessage", "value: " + value2); // Prints asd
        Log.d("onConsoleMessage", "js1: " + js1);

        //final String js1 = "window.localStorage.setItem(\"myKey\", 123);";
        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("window.localStorage.setItem('"+ key +"'='"+ value +"');", null);
       } else {
            webView.loadUrl("javascript:localStorage.setItem('"+ key +"'='"+ value +"');");
        }

        */

        webView.evaluateJavascript(js1, null);
        /*
        webView.evaluateJavascript(js1, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                String str = "onReceiveValue: " + s;
                FileUtils.writeFileOnInternalStorage(getBaseContext(), str);
                Log.d("onConsoleMessage", "onReceiveValue: " + s); // Prints asd
            }
        });

         */
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void clearData(){
        final String js1 = "window.localStorage.clear()";
        Log.d("onConsoleMessage", "js1: " + js1); // Prints asd

        webView.evaluateJavascript(js1, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                String str = "onReceiveValue: " + s;
                FileUtils.writeFileOnInternalStorage(getBaseContext(), str);
                Log.d("onConsoleMessage", "onReceiveValue: " + s); // Prints asd
            }
        });
    }

    /*
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

    */

    @Override
    public void showToastMessage(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGetUserInfoSuccessResponse() {
        progressBar.setVisibility(View.GONE);
        super.onBackPressed();
    }

    private class LocalStorageJavaScriptInterface {
        private Context mContext;
        private LocalStorage localStorageDBHelper;
        private SQLiteDatabase database;

        LocalStorageJavaScriptInterface(Context c) {
            mContext = c;
            localStorageDBHelper = LocalStorage.getInstance(mContext);
        }

        /**
         * This method allows to get an item for the given key
         * @param key : the key to look for in the local storage
         * @return the item having the given key
         */
        @JavascriptInterface
        public String getItem(String key)
        {
            String value = null;
            if(key != null)
            {
                database = localStorageDBHelper.getReadableDatabase();
                Cursor cursor = database.query(LocalStorage.LOCALSTORAGE_TABLE_NAME,
                        null,
                        LocalStorage.LOCALSTORAGE_ID + " = ?",
                        new String [] {key},null, null, null);
                if(cursor.moveToFirst())
                {
                    value = cursor.getString(1);
                }
                cursor.close();
                database.close();
            }
            return value;
        }

        /**
         * set the value for the given key, or create the set of datas if the key does not exist already.
         * @param key
         * @param value
         */
        @JavascriptInterface
        public void setItem(String key,String value)
        {
            if(key != null && value != null)
            {
                String oldValue = getItem(key);
                database = localStorageDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(LocalStorage.LOCALSTORAGE_ID, key);
                values.put(LocalStorage.LOCALSTORAGE_VALUE, value);
                if(oldValue != null)
                {
                    database.update(LocalStorage.LOCALSTORAGE_TABLE_NAME, values, LocalStorage.LOCALSTORAGE_ID + "='" + key + "'", null);
                }
                else
                {
                    database.insert(LocalStorage.LOCALSTORAGE_TABLE_NAME, null, values);
                }
                database.close();
            }
        }

        /**
         * removes the item corresponding to the given key
         * @param key
         */
        @JavascriptInterface
        public void removeItem(String key)
        {
            if(key != null)
            {
                database = localStorageDBHelper.getWritableDatabase();
                database.delete(LocalStorage.LOCALSTORAGE_TABLE_NAME, LocalStorage.LOCALSTORAGE_ID + "='" + key + "'", null);
                database.close();
            }
        }

        /**
         * clears all the local storage.
         */
        @JavascriptInterface
        public void clear()
        {
            database = localStorageDBHelper.getWritableDatabase();
            database.delete(LocalStorage.LOCALSTORAGE_TABLE_NAME, null, null);
            database.close();
        }
    }
}
