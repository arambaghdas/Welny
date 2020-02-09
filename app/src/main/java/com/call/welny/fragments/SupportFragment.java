package com.call.welny.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.call.welny.R;
import com.call.welny.WebViewActivity;
import com.call.welny.ZoneCloseActivity;
import com.call.welny.util.Analytics;
import com.call.welny.util.Links;

import static com.call.welny.util.Links.PHONE_NUMBER;

@SuppressLint("ValidFragment")
public class SupportFragment extends Fragment {

    TextView textViewVersion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.support_welny, container, false);

        textViewVersion = view.findViewById(R.id.tv_version);

        TextView textViewCallToOperator = view.findViewById(R.id.tv_call_to_operator);
        textViewCallToOperator.setOnClickListener(v -> {
            Analytics.sendSupportCallEvent();
            Uri u = Uri.parse("tel:" + PHONE_NUMBER);
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            try {
                startActivity(i);
            } catch (SecurityException s) {
                Toast.makeText(getActivity(), s.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        });

        TextView textViewZoneClose = view.findViewById(R.id.tv_zone_close);
        textViewZoneClose.setOnClickListener(v -> {
            Analytics.sendSupportGeoEvent();
            Intent intent = new Intent(getActivity(), ZoneCloseActivity.class);
            startActivity(intent);

        });

        TextView textViewAboutWelny = view.findViewById(R.id.tv_about_welny);
        textViewAboutWelny.setOnClickListener(v -> {
            Analytics.sendSupportAboutEvent();
            openWebView(Links.URL_ABOUT, getString(R.string.about_welny));
        });

        TextView textViewAgreement = view.findViewById(R.id.tv_agreement);
        textViewAgreement.setOnClickListener(v -> {
            Analytics.sendSupportTermsEvent();
            openWebView(Links.URL_AGREEMENT, getString(R.string.agreement));
        });

        TextView textViewConfidential = view.findViewById(R.id.tv_confidential);
        textViewConfidential.setOnClickListener(v -> {
            Analytics.sendSupportNdaEvent();
            openWebView(Links.URL_CONFIDENTIAL, getString(R.string.confidential));
        });

        TextView textViewFaq = view.findViewById(R.id.tv_faq);
        textViewFaq.setOnClickListener(v -> {
            Analytics.sendSupportFaqEvent();
            openWebView(Links.URL_FAQ, getString(R.string.faq));
        });

        setApplicationVersion();

        return view;
    }

    private void openWebView(String link, String title) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        Bundle b = new Bundle();
        b.putString("link", link);
        b.putString("title", title);
        b.putBoolean("auth", false);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void setApplicationVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionName = pInfo.versionName;
            String versionStr = getActivity().getString(R.string.version) + " " + versionName;
            textViewVersion.setText(versionStr);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}