package com.call.welny;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.call.welny.fragments.InviteFriendFragment;
import com.call.welny.fragments.MassageTypesFragment;
import com.call.welny.fragments.OrderWelnyFragment;
import com.call.welny.fragments.SupportFragment;
import com.call.welny.fragments.TryWelnyFragment;
import com.call.welny.fragments.UpdateAccountFragment;
import com.call.welny.log.LogsActivity;
import com.call.welny.object.GetUserInfo;
import com.call.welny.presenter.GetUserInfoPresenter;
import com.call.welny.register.VerifyPhoneActivity;
import com.call.welny.util.Links;
import com.call.welny.util.Preferences;
import com.call.welny.views.UserInfoView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelnyActivity extends AppCompatActivity implements UserInfoView {

    private GetUserInfoPresenter presenter;
    private TextView tvFullName, tvCredit;
    private DrawerLayout navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welny);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
               getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorLight));
        }

        RelativeLayout menuIcon = findViewById(R.id.rl_menu);
        navDrawer = findViewById(R.id.drawer_layout);
        TextView textViewOrder = findViewById(R.id.tv_order);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerOpened(View drawerView) {

            }

            public void onDrawerClosed(View view) {
                navDrawer.setVisibility(View.GONE);
            }
        };

        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navDrawer.setVisibility(View.GONE);
        menuIcon.setOnClickListener(view -> {
            if (navDrawer.isDrawerOpen(GravityCompat.START)) {
                navDrawer.closeDrawer(GravityCompat.START);
            } else {
                tvFullName = findViewById(R.id.tv_full_name_header);
                tvCredit = findViewById(R.id.tv_credit);

                GetUserInfo getUserInfo = Preferences.getUserInfo(this);
                if (getUserInfo != null) {
                    String fullName = getUserInfo.getFullname();
                    tvFullName.setText(fullName);
                    String creditStr = getString(R.string.credits).toUpperCase() + " " + getUserInfo.getCredits() + " \u20BD";
                    tvCredit.setText(creditStr);
                }

                LinearLayout linearLayoutWelnyPlus = findViewById(R.id.ly_welny_plus);
                linearLayoutWelnyPlus.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    Fragment second;

                    if (getUserInfo != null && getUserInfo.getCustomerPackage() != null) {
                        second = new OrderWelnyFragment();
                    } else {
                        second = new TryWelnyFragment();
                    }

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, second);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });

                LinearLayout linearLayoutInviteFriends = findViewById(R.id.ly_invite_friends);
                linearLayoutInviteFriends.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    final Fragment second = new InviteFriendFragment();

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, second);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });

                LinearLayout linearLayoutOrders = findViewById(R.id.ly_orders);
                linearLayoutOrders.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);

                    SystemClock.sleep(500);

                    Intent intent = new Intent(this, WebViewActivity.class);
                    Bundle b1 = new Bundle();
                    b1.putString("link", Links.BOOKINGS_URL);
                    intent.putExtras(b1);
                    startActivity(intent);

                });

                LinearLayout linearLayoutAccount = findViewById(R.id.ly_account);
                linearLayoutAccount.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    final Fragment second = new UpdateAccountFragment();

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, second);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });

                LinearLayout linearLayoutSupport = findViewById(R.id.ly_support);
                linearLayoutSupport.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);

                    final Fragment second = new SupportFragment();

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, second);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });

                textViewOrder.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    final Fragment second = new MassageTypesFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, second);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });

                LinearLayout linearLayoutExit = findViewById(R.id.ly_exit);
                linearLayoutExit.setOnClickListener(v -> {
                   presenter.sendLogOutRequest();
                });

                navDrawer.openDrawer((int) Gravity.START);
                navDrawer.setVisibility(View.VISIBLE);
            }
        });

        final Fragment second = new MassageTypesFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layout, second);
        fragmentTransaction.commit();

        presenter = new GetUserInfoPresenter(this, this);
    }

    @OnClick(R.id.fy_top_view)
    public void openLogs() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void performLogOut() {
        Preferences.setToken(this, "");
        Preferences.setUid(this, "");
        Preferences.setUserInfo(this, null);
        Intent intent = new Intent(this, VerifyPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void showGetUserInfoSuccessResponse() {

    }

    @Override
    public void showGetUserInfoFailResponse() {

    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
