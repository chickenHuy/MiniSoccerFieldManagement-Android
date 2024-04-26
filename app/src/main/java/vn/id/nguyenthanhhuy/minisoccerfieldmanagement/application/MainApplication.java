package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.biometric.BiometricManager;

import com.yariksoffice.lingver.Lingver;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.MatchReminderService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;

public class MainApplication extends Application {
    public static String language;
    public static Boolean notify = false;
    public static Boolean fingerprint = false;
    public static SharedPreferences.Editor editor;
    public static final String AES = "AES";
    public static final String SECRET_KEY = "chickenHuySecret";

    public static User curentUser = null;

    User newUser = new User();


    public static boolean isChangeLanguage = false;

    @Override
    public void onCreate() {
        super.onCreate();
        getUserSetting();

        Lingver.init(MainApplication.this, language);
        Lingver.getInstance().setLocale(MainApplication.this, language);

        turnOnNotify(getApplicationContext());
        createfakeUser();
    }

    public void getUserSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language = sharedPreferences.getString("language", "en");
        notify = sharedPreferences.getBoolean("notify", false);
        fingerprint = sharedPreferences.getBoolean("fingerprint", false);
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

    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    public static String decrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.decode(data, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static Boolean checkSupportFingerprint(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        boolean canAuthenticate = biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
        if (!canAuthenticate) {
            fingerprint = false;
            editor.putBoolean("fingerprint", false);
            editor.apply();
        }
        return canAuthenticate;
    }

    public static void turnOnNotify(Context context) {
        if (notify) {
            Log.i("MainApplication", "Turn on notify");
            try {
                Intent intent = new Intent(context, MatchReminderService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    context.startForegroundService(intent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(intent);
                }
            } catch (Exception e) {
                Log.i("MainApplication error", "Turn on notify");
            }
        }
    }

    public static void turnOffNotify(Context context) {
        if (!notify) {
            Log.i("MainApplication", "Turn off notify");
            try {
                Intent intent = new Intent(context, MatchReminderService.class);
                context.stopService(intent);
            } catch (Exception e) {
                Log.i("MainApplication error", "Turn off notify");
            }
        }
    }
}
