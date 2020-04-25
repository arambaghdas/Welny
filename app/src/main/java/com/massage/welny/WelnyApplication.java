
package com.massage.welny;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.yandex.metrica.AppMetricaDeviceIDListener;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import im.getsocial.sdk.GetSocial;

import static com.massage.welny.util.Constants.APP_METRIC_KEY;

public class WelnyApplication extends Application {

    private static WelnyApplication instance;

    public WelnyApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder(APP_METRIC_KEY);
        YandexMetrica.activate(getApplicationContext(), configBuilder.build());

        YandexMetrica.enableActivityAutoTracking(this);
        Log.v("YandexMetrica", "onCreate");

        YandexMetrica.requestAppMetricaDeviceID(new AppMetricaDeviceIDListener() {
            @Override
            public void onLoaded(@Nullable String s) {
               Log.v("YandexMetrica", "s: " + s);
            }

            @Override
            public void onError(@NonNull Reason reason) {
                Log.v("YandexMetrica", "onError: " + reason.name());
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    public static WelnyApplication getInstance() {
        return instance;
    }

}
