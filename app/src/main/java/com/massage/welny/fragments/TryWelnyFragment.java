package com.massage.welny.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.massage.welny.R;

@SuppressLint("ValidFragment")
public class TryWelnyFragment extends Fragment implements View.OnClickListener {

    private TextView textViewOrder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.try_welny, container, false);

        textViewOrder = view.findViewById(R.id.tv_order);
        textViewOrder.setFocusable(true);
        textViewOrder.setOnClickListener(this);

        TextView textViewHeader2 = view.findViewById(R.id.tv_header2);
        TextView textViewHeader3 = view.findViewById(R.id.tv_header3);

        SpannableStringBuilder str2 = new SpannableStringBuilder(getString(R.string.try_welny_header1));
        str2.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 114, 118,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewHeader2.setText(str2);

        SpannableStringBuilder str3 = new SpannableStringBuilder(getString(R.string.try_welny_header2));
        str3.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 18, 22,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewHeader3.setText(str3);

        return view;
    }

    @Override
    public void onClick(View view) {
        final Fragment second = new MassageTypesFragment();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layout, second);
        fragmentTransaction.commitAllowingStateLoss();
    }
}