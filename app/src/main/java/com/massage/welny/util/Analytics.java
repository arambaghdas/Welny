package com.massage.welny.util;
import android.content.Context;
import android.util.Log;
import com.yandex.metrica.YandexMetrica;
import com.facebook.appevents.AppEventsLogger;

public class Analytics {

    public static void sendMainSingleEvent(Context mContext) {
        YandexMetrica.reportEvent("main_single");

        AppEventsLogger logger = AppEventsLogger.newLogger(mContext);
        logger.logEvent("Add to basket");

        Log.v("Analytics", "main_single");
    }

    public static void sendGetCodeEvent() {
        YandexMetrica.reportEvent("get_code");
        Log.v("Analytics", "get_code");
    }

    public static void sendRegistrationCompletedEvent(Context mContext) {
        YandexMetrica.reportEvent("registration_completed");

        AppEventsLogger logger = AppEventsLogger.newLogger(mContext);
        logger.logEvent("Complete registration");

        Log.v("Analytics", "registration_completed");
    }

    public static void sendMenuOrdersEvent() {
        YandexMetrica.reportEvent("menu_orders");
        Log.v("Analytics", "menu_orders");
    }

    public static void sendRegistrationInitiatedEvent(Context mContext) {
        YandexMetrica.reportEvent("registration_initiated");

        AppEventsLogger logger = AppEventsLogger.newLogger(mContext);
        logger.logEvent("Submit application");

        Log.v("Analytics", "registration_initiated");
    }

    public static void sendMenuOutEvent() {
        YandexMetrica.reportEvent("menu_out");
        Log.v("Analytics", "menu_out");
    }

    public static void sendMenuWelnyPlusEvent() {
        YandexMetrica.reportEvent("menu_welny+");
        Log.v("Analytics", "menu_welny+");
    }

    public static void sendMainDoubleEvent(Context mContext) {
        YandexMetrica.reportEvent("main_double");

        AppEventsLogger logger = AppEventsLogger.newLogger(mContext);
        logger.logEvent("Add to basket");

        Log.v("Analytics", "main_double");
    }

    public static void sendRegistrationCodeEnteredEvent() {
        YandexMetrica.reportEvent("registration_code_entered");
        Log.v("Analytics", "registration_code_entered");
    }

    public static void sendMenuSupportEvent() {
        YandexMetrica.reportEvent("menu_support");
        Log.v("Analytics", "menu_support");
    }

    public static void sendMenuAccountEvent() {
        YandexMetrica.reportEvent("menu_account");
        Log.v("Analytics", "menu_account");
    }

    public static void sendMainInviteEvent() {
        YandexMetrica.reportEvent("main_invite");
        Log.v("Analytics", "main_invite");
    }

    public static void sendMenuInviteEvent() {
        YandexMetrica.reportEvent("menu_invite");
        Log.v("Analytics", "menu_invite");
    }

    public static void sendSupportCallEvent() {
        YandexMetrica.reportEvent("support_call");
        Log.v("Analytics", "support_call");
    }

    public static void sendAccountUpdateEvent() {
        YandexMetrica.reportEvent("account_update");
        Log.v("Analytics", "account_update");
    }

    public static void sendSupportGeoEvent() {
        YandexMetrica.reportEvent("support_geo");
        Log.v("Analytics", "support_geo");
    }

    public static void sendSupportFaqEvent() {
        YandexMetrica.reportEvent("support_faq");
        Log.v("Analytics", "support_faq");
    }

    public static void sendSupportAboutEvent() {
        YandexMetrica.reportEvent("support_about");
        Log.v("Analytics", "support_about");
    }

    public static void sendSupportTermsEvent() {
        YandexMetrica.reportEvent("support_terms");
        Log.v("Analytics", "support_terms");
    }

    public static void sendGetCodeAgainEvent() {
        YandexMetrica.reportEvent("get_code_again");
        Log.v("Analytics", "get_code_again");
    }

    public static void sendSupportNdaEvent() {
        YandexMetrica.reportEvent("support_nda");
        Log.v("Analytics", "support_nda");
    }

    public static void sendInviteAndroidEvent() {
        YandexMetrica.reportEvent("invite_android");
        Log.v("Analytics", "invite_android");
    }

    public static void sendPromoCodeEvent() {
        YandexMetrica.reportEvent("invite_copy");
        Log.v("Analytics", "invite_copy");
    }

    public static void sendOrderConfirmedEvent(Context mContext) {
        YandexMetrica.reportEvent("order_confirmed");

        AppEventsLogger logger = AppEventsLogger.newLogger(mContext);
        logger.logEvent("Purchase");

        Log.v("Analytics", "order_confirmed");
    }

    public static void sendOrderCanceledEvent() {
        YandexMetrica.reportEvent("order_canceled");
        Log.v("Analytics", "order_canceled");
    }
}
