package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;

public class MainApplication extends Application {
    public static String language = "en";
    public static boolean isChangeLanguage = false;
    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(MainApplication.this, language);
        Lingver.getInstance().setLocale(MainApplication.this, language);
    }
}
