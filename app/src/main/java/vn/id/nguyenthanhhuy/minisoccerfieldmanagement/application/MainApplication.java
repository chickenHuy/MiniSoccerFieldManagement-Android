package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ShowUserProfileActivity;

public class MainApplication extends Application {
    public static String language = "en";
    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(MainApplication.this, language);
        Lingver.getInstance().setLocale(MainApplication.this, language);
    }
}
