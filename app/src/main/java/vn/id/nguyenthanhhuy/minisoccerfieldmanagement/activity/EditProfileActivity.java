package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;

public class EditProfileActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setStatusBarColor();
        configureWidget();
        Lingver.getInstance().setLocale(EditProfileActivity.this, MainApplication.language);
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));
    }

    public void configureWidget() {

    }

    public void saveChangeProfile(View view) {
        MainApplication.language = "vi";
        Lingver.getInstance().setLocale(EditProfileActivity.this, MainApplication.language);
        recreate();
    }
}