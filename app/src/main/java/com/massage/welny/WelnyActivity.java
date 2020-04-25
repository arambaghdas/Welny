package com.massage.welny;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.massage.welny.fragments.InviteFriendFragment;
import com.massage.welny.fragments.MassageTypesFragment;
import com.massage.welny.fragments.OrderWelnyFragment;
import com.massage.welny.fragments.SupportFragment;
import com.massage.welny.fragments.TryWelnyFragment;
import com.massage.welny.fragments.UpdateAccountFragment;
import com.massage.welny.fragments.WebViewFragment;
import com.massage.welny.object.GetUserInfo;
import com.massage.welny.presenter.GetUserInfoPresenter;
import com.massage.welny.register.VerifyPhoneActivity;
import com.massage.welny.util.Analytics;
import com.massage.welny.util.Links;
import com.massage.welny.util.Preferences;
import com.massage.welny.views.UserInfoView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelnyActivity extends AppCompatActivity implements UserInfoView {

    private GetUserInfoPresenter presenter;
    private TextView tvFullName, tvCredit;
    private DrawerLayout navDrawer;
    private Fragment currentFragment;
    private RelativeLayout rlNavDrawer;
    public static final int WEBVIEW_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welny);
        ButterKnife.bind(this);

        RelativeLayout menuIcon = findViewById(R.id.rl_menu);
        navDrawer = findViewById(R.id.drawer_layout);
        rlNavDrawer = findViewById(R.id.rl_nav_drawer);

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
            closeKeyBoard();

            boolean isAccountFragment = currentFragment != null &&
                    currentFragment instanceof UpdateAccountFragment;

            if (isAccountFragment) {
                ((UpdateAccountFragment )currentFragment).hideFocus();
            }

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
                    Analytics.sendMenuWelnyPlusEvent();
                    String tag;

                    if (getUserInfo != null && getUserInfo.getCustomerPackage() != null) {
                        currentFragment = new OrderWelnyFragment();
                        tag = "OrderWelnyFragment";
                    } else {
                        currentFragment = new TryWelnyFragment();
                        tag = "TryWelnyFragment";
                    }
                    addToBackStuck(currentFragment, tag, false);
                });

                LinearLayout linearLayoutInviteFriends = findViewById(R.id.ly_invite_friends);
                linearLayoutInviteFriends.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    Analytics.sendMenuInviteEvent();
                    currentFragment = new InviteFriendFragment();
                    String tag = "InviteFriendFragment";
                    addToBackStuck(currentFragment, tag, false);
                });

                LinearLayout linearLayoutOrders = findViewById(R.id.ly_orders);
                linearLayoutOrders.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    showOrders();
                });

                LinearLayout linearLayoutAccount = findViewById(R.id.ly_account);
                linearLayoutAccount.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    Analytics.sendMenuAccountEvent();
                    currentFragment = new UpdateAccountFragment();
                    String tag = "UpdateAccountFragment";
                    addToBackStuck(currentFragment, tag, false);
                });

                LinearLayout linearLayoutSupport = findViewById(R.id.ly_support);
                linearLayoutSupport.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    Analytics.sendMenuSupportEvent();
                    currentFragment = new SupportFragment();
                    String tag = "SupportFragment";
                    addToBackStuck(currentFragment, tag, false);
                });

                textViewOrder.setOnClickListener(v -> {
                    navDrawer.closeDrawer(GravityCompat.START);
                    currentFragment = new MassageTypesFragment();
                    String tag = "MassageTypesFragment";
                    addToBackStuck(currentFragment, tag,false);
                });

                LinearLayout linearLayoutExit = findViewById(R.id.ly_exit);
                linearLayoutExit.setOnClickListener(v -> {
                    Analytics.sendMenuOutEvent();
                    presenter.sendLogOutRequest();
                });

                navDrawer.openDrawer((int) Gravity.START);
                navDrawer.setVisibility(View.VISIBLE);
            }
        });

        currentFragment = new MassageTypesFragment();
        String tag = "MassageTypesFragment";
        addToBackStuck(currentFragment, tag,false);

        presenter = new GetUserInfoPresenter(this, this);
    }

    public void showOrders() {
        Analytics.sendMenuOrdersEvent();

        currentFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("link", Links.BOOKINGS_URL);
        bundle.putBoolean("auth", true);
        currentFragment.setArguments(bundle);
        String tag = "WebViewFragment";
        addToBackStuck(currentFragment, tag, false);
    }

    @OnClick(R.id.fy_top_view)
    public void openLogs() {
        //Intent intent = new Intent(this, LogsActivity.class);
        //startActivity(intent);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void performLogOut() {
        Preferences.clearUserSession(this);
        Preferences.setUserInfo(this, null);
        Intent intent = new Intent(this, VerifyPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void addToBackStuck(Fragment second, String tag, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
        //    Log.v("addToBackStuck", "Tag: " + fragmentManager.getBackStackEntryAt(entry).getName());
        //}

        //Log.v("addToBackStuck", "-----------------------------------------");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (addToBackStack) {
            /*
             for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
                if (fragmentManager.getBackStackEntryAt(entry).getName().equals(tag)) {
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                fragmentTransaction.addToBackStack(tag);
            }
            */

            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.replace(R.id.layout, second, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        rlNavDrawer.getGlobalVisibleRect(viewRect);
        if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            navDrawer.closeDrawers();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WEBVIEW_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            showOrders();
        }
    }

}
