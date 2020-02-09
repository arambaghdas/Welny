package com.call.welny.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.call.welny.R;
import com.call.welny.WebViewActivity;
import com.call.welny.util.Analytics;
import com.call.welny.util.Links;

@SuppressLint("ValidFragment")
public class MassageTypesFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.massage_types, container, false);

        ImageView imageViewType1 = view.findViewById(R.id.iv_massage_type1);
        ImageView imageViewType2 = view.findViewById(R.id.iv_massage_type2);
        ImageView imageViewType3 = view.findViewById(R.id.iv_massage_type3);

        imageViewType1.setOnClickListener(v -> {
            Analytics.sendMainSingleEvent();
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            Bundle b1 = new Bundle();
            b1.putString("link", Links.ORDER_MASSAGE_URL);
            b1.putBoolean("auth", true);
            intent.putExtras(b1);
            startActivity(intent);
        });

        imageViewType2.setOnClickListener(v -> {
            Analytics.sendMainDoubleEvent();
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            Bundle b1 = new Bundle();
            b1.putString("link", Links.ORDER_MASSAGE_URL);
            b1.putBoolean("auth", true);
            intent.putExtras(b1);
            startActivity(intent);
        });

        imageViewType3.setOnClickListener(v -> {
            Analytics.sendMainInviteEvent();
            final Fragment second = new InviteFriendFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.layout, second);
            fragmentTransaction.commit();
        });

        return view;
    }
}