package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yariksoffice.lingver.Lingver;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.HomeFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ShowUserProfileFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BookingFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.CustomerFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.MatchReminderService;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationViewMenu;
    public FloatingActionButton floatingActionButton;
    public Bundle args = new Bundle();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = new Intent(this, MatchReminderService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForegroundService(intent);
            }
            else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                startForegroundService(intent);
            }
            else {
                startService(intent);
            }
        }
        catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }

        setContentView(R.layout.activity_main);
        user = (User) MainApplication.curentUser;

        Lingver.getInstance().setLocale(MainActivity.this, MainApplication.language);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));

        setWidget();
        setupBottomNavigationViewMenu();

        if (!MainApplication.isChangeLanguage) {
            switchFragment(new HomeFragment(), args);
        } else {
            switchFragment(new ShowUserProfileFragment(), args);
            MainApplication.isChangeLanguage = false;
            floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
        }
    }

    public void setupBottomNavigationViewMenu() {
        bottomNavigationViewMenu = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationViewMenu.setSelectedItemId(R.id.menu_option_home);
        bottomNavigationViewMenu.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_option_transaction) {
                floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
                switchFragment(new BookingFragment(), args);
                return true;
            }
            if (item.getItemId() == R.id.menu_option_service) {
                floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
                switchFragment(new ServiceFragment(), args);
                return true;
            }
            if (item.getItemId() == R.id.menu_option_customer) {
                floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
                switchFragment(new CustomerFragment(), args);
                return true;
            }
            if (item.getItemId() == R.id.menu_option_show_user_profile) {
                floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
                switchFragment(new ShowUserProfileFragment(), args);
                return true;
            }
            return true;
        });
    }

    public void setWidget() {
        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(v -> {
            floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.primaryColor, getTheme()));
            bottomNavigationViewMenu.setSelectedItemId(R.id.menu_option_home);
            switchFragment(new HomeFragment(), args);
        });
    }

    public void switchFragment(Fragment newFragment, Bundle args) {
        newFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        List<Fragment> allFragments = fragmentManager.getFragments();
        for (Fragment fragment : allFragments) {
            if (fragment.getId() == R.id.container_fragment_home || fragment.getId() == R.id.container_fragment) {
                fragmentTransaction.remove(fragment);
            }
        }

        if (newFragment instanceof HomeFragment) {
            ((LinearLayout) findViewById(R.id.container_fragment_home)).setVisibility(View.VISIBLE);
            ((ScrollView) findViewById(R.id.scroll_view)).setVisibility(View.GONE);

            fragmentTransaction.replace(R.id.container_fragment_home, newFragment);
        } else {
            ((LinearLayout) findViewById(R.id.container_fragment_home)).setVisibility(View.GONE);
            ((ScrollView) findViewById(R.id.scroll_view)).setVisibility(View.VISIBLE);

            fragmentTransaction.replace(R.id.container_fragment, newFragment);
        }

        fragmentTransaction.commit();
    }
}