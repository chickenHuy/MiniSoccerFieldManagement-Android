package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;

public class ShowUserProfileActivity extends AppCompatActivity {

    private Switch switchVietnamese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_profile);

        setStatusBarColor();
        configureWidget();
        Lingver.getInstance().setLocale(ShowUserProfileActivity.this, MainApplication.language);
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, getTheme()));
    }

    public void configureWidget() {
        switchVietnamese = findViewById(R.id.switch_vietnamese);
        if (MainApplication.language.equals("vi")) {
            switchVietnamese.setChecked(true);
        }
        switchVietnamese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainApplication.language = "vi";
                    Lingver.getInstance().setLocale(ShowUserProfileActivity.this, MainApplication.language);
                    recreate();
                } else {
                    MainApplication.language = "en";
                    Lingver.getInstance().setLocale(ShowUserProfileActivity.this, MainApplication.language);
                    recreate();
                }
            }
        });
    }

    public void editUserAvatar(View view) {

    }
}