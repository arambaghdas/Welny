package com.call.welny;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoneCloseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_close);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_arrow_back)
    public void onBack() {
        super.onBackPressed();
    }

}
