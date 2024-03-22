package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.FeedsFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.HomeFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ShowUserProfileFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BookingFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.WalletFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationViewMenu;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Lingver.getInstance().setLocale(MainActivity.this, MainApplication.language);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));

        setWidget();
        setupBottomNavigationViewMenu();

        if (!MainApplication.isChangeLanguage) {
            switchFragment(new HomeFragment());
        } else {
            switchFragment(new ShowUserProfileFragment());
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
                switchFragment(new BookingFragment());
                return true;
            }
            if (item.getItemId() == R.id.menu_option_feeds) {
                floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
                switchFragment(new FeedsFragment());
                return true;
            }
            if (item.getItemId() == R.id.menu_option_wallet) {
                floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
                switchFragment(new WalletFragment());
                return true;
            }
            if (item.getItemId() == R.id.menu_option_show_user_profile) {
                floatingActionButton.setImageTintList(getResources().getColorStateList(R.color.black, getTheme()));
                switchFragment(new ShowUserProfileFragment());
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
            switchFragment(new HomeFragment());
        });
    }

    public void switchFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, newFragment);
        fragmentTransaction.commit();
    }
}