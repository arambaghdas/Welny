package com.call.welny.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.call.welny.R;
import com.call.welny.object.CustomerPackage;
import com.call.welny.object.GetUserInfo;
import com.call.welny.object.PackageResponse;
import com.call.welny.util.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class OrderWelnyFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_welny, container, false);

        TextView textViewOrder = view.findViewById(R.id.tv_order);
        textViewOrder.setFocusable(true);
        textViewOrder.setOnClickListener(this);

        TextView textViewTitle = view.findViewById(R.id.tv_header3);
        TextView textViewStartDate = view.findViewById(R.id.tv_header5);
        TextView textViewEndDate = view.findViewById(R.id.tv_header7);
        TextView textViewDuration = view.findViewById(R.id.tv_header9);

        GetUserInfo userInfo = Preferences.getUserInfo(getContext());
        if (userInfo != null) {
            CustomerPackage customerPackage = userInfo.getCustomerPackage();
            if (customerPackage != null) {
                textViewStartDate.setText(formatDate(customerPackage.getSubmissionTime()));
                textViewEndDate.setText(formatDate(customerPackage.getExpirationTime()));
                textViewDuration.setText(String.valueOf(customerPackage.getHoursLeft()));
                PackageResponse packageResponse = customerPackage.getPackageResponse();
                if (packageResponse != null) {
                    String title = getString(R.string.welny_plus) + " " +
                            packageResponse.getTitle();
                    textViewTitle.setText(title);
                }
            }
        }

        return view;
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat formatDateTime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
            SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date newDate = formatDateTime.parse(date);
            if (newDate != null) {
                return formatDate.format(newDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onClick(View view) {
        final Fragment second = new MassageTypesFragment();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layout, second);
        fragmentTransaction.commit();
    }
}