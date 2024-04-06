package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;

public class MainApplication extends Application {
    public static String language;
    public static Boolean notify = false;
    public static SharedPreferences.Editor editor;

    public static User curentUser = null;

    User newUser = new User();


    public static boolean isChangeLanguage = false;
    @Override
    public void onCreate() {
        super.onCreate();
        getUserSetting();

        Lingver.init(MainApplication.this, language);
        Lingver.getInstance().setLocale(MainApplication.this, language);

        createfakeUser();
    }

    public void getUserSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language = sharedPreferences.getString("language", "en");
        notify = sharedPreferences.getBoolean("notify", false);
    }

    private void createfakeUser() {
        newUser.setName("Nguyễn Thiện Luân");
        newUser.setUserName("1");
        newUser.setPassword("admin");
        newUser.setId("user1711953275532");
        newUser.setDateOfBirth("08/12/2003");
        newUser.setImage(null);
        newUser.setPhoneNumber("0889192145");
        newUser.setRole("Manager");
        newUser.setDeleted(false);
        newUser.setType(null);
        newUser.setGender("Nam");
        curentUser = newUser;
    }
}
