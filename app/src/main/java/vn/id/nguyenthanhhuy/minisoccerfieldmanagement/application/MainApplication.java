package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;

public class MainApplication extends Application {
    public static String language = "en";
    public static User curentUser = null;

    User newUser = new User();


    public static boolean isChangeLanguage = false;
    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(MainApplication.this, language);
        Lingver.getInstance().setLocale(MainApplication.this, language);

        createfakeUser();
    }

    private void createfakeUser() {
        newUser.setName("Nguyễn Thiện Luân");
        newUser.setUserName("1");
        newUser.setPassword("admin");
        newUser.setId(CurrentTimeID.nextId("user"));
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
