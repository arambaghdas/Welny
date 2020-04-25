package com.massage.welny.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.massage.welny.R;
import com.massage.welny.WelnyActivity;
import com.massage.welny.util.Preferences;

public class WelcomeActivity extends AppCompatActivity {


    private Handler splashHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        runOnUiThread(() -> {
            splashHandler = new Handler();
            splashHandler.postDelayed(() -> {
                String token = Preferences.getToken(this);
                String uid = Preferences.getUid(this);

                if (!token.isEmpty() && !uid.isEmpty()) {
                    Intent intent = new Intent(this, WelnyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    if (Preferences.shouldShowWelcomeScreen(this)) {
                        Intent intent = new Intent(this, IntroActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, VerifyPhoneActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                finish();
            }, 2000);
        });
    }

}
