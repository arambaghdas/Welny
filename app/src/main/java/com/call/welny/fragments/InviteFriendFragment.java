package com.call.welny.fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.call.welny.R;
import com.call.welny.util.Analytics;
import com.call.welny.util.Links;
import com.call.welny.util.Preferences;

import java.util.Objects;

@SuppressLint("ValidFragment")
public class InviteFriendFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_friend, container, false);

        TextView textViewDescr = view.findViewById(R.id.tv_header4);
        SpannableStringBuilder str = new SpannableStringBuilder(getString(R.string.invite_friend_header2));
        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 57, 60,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewDescr.setText(str);

        TextView textViewPromoCode = view.findViewById(R.id.tv_header3);
        TextView textViewInviteFriend = view.findViewById(R.id.tv_invite_friend);
        String promoCode = Preferences.getPromoCode(getActivity());
        textViewInviteFriend.setOnClickListener(v -> {
            Analytics.sendInviteAndroidEvent();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            String promoCodeStr = getString(R.string.promo_code_invite_friend_title) +
                    "(" + Links.BASE_URL_SHARE + ")." + "\n" +
                    getString(R.string.promo_code_invite_friend) +
                    "\n" + promoCode;
            //share.putExtra(Intent.EXTRA_SUBJECT, promoCodeStr);
            share.putExtra(Intent.EXTRA_TEXT, promoCodeStr);
            startActivity(Intent.createChooser(share, getString(R.string.share_promo_code)));
        });

        textViewPromoCode.setText(promoCode);

        LinearLayout linerLayoutPromoCode = view.findViewById(R.id.ly_header);
        linerLayoutPromoCode.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.promo_code), promoCode);
            if (clipboard != null) {
                Analytics.sendPromoCodeEvent();
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getActivity(), getString(R.string.promo_code_share), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        return view;
    }
}