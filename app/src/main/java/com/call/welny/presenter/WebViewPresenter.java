package com.call.welny.presenter;

import android.app.Activity;
import android.util.Base64;
import com.call.welny.util.Links;
import com.call.welny.util.Preferences;
import com.call.welny.views.WebSiteView;
import java.nio.charset.Charset;

public class WebViewPresenter {

    private Activity activity;
    private WebSiteView webSiteView;

    public WebViewPresenter(Activity activity, WebSiteView webSiteView) {
        this.activity = activity;
        this.webSiteView = webSiteView;
    }

    public void configureBanner(String link) {
        if (link.equals(Links.SINGLE_ORDER_MASSAGE_URL) || link.equals(Links.COUPLE_ORDER_MASSAGE_URL) ) {
            webSiteView.hideBanner();
        } else {
            webSiteView.showBanner();
        }
    }

    public String getUrl(String link, boolean auth) {
        if (auth) {
            String value = Preferences.getUserSession(activity);
            byte[] data = value.getBytes(Charset.forName("UTF-8"));
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            /*
            if (link.equals(Links.SINGLE_ORDER_MASSAGE_URL)) {
                return ORDER_MASSAGE_URL + "/?session=" + base64 + SINGLE_ORDER_MASSAGE_TYPE;
            } else if (link.equals(Links.COUPLE_ORDER_MASSAGE_URL)) {
                return ORDER_MASSAGE_URL + "/?session=" + base64 + COUPLE_ORDER_MASSAGE_TYPE;
            } else {
                return link + "/?session=" + base64;
            }
            */
            return link + "/?session=" + base64;

        } else {
            return link;
        }
    }

}
