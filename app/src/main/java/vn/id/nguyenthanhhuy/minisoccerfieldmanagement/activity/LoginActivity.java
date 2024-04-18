package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import java.io.Console;
import java.util.concurrent.Executor;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.CustomDialogFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    AppCompatButton buttonShowPassword;
    AppCompatButton buttonFingerprint;
    CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        UserServiceImpl userService = new UserServiceImpl(LoginActivity.this);
//        User user = new User();
//        user.setName("Nguyễn Thiện Luân");
//        user.setUserName("thienluan");
//        user.setPassword("password");
//        user.setId("user1711953275532");
//        user.setDateOfBirth("08/12/2003");
//        user.setImage(null);
//        user.setPhoneNumber("0889192145");
//        user.setRole("Manager");
//        user.setDeleted(false);
//        user.setType(null);
//        user.setGender("Nam");
//        userService.update(user);
        System.out.println("Checking log NTLuan");


        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        buttonShowPassword = findViewById(R.id.button_show_password);
        buttonFingerprint = findViewById(R.id.button_fingerprint);

        setStatusBarColor();
        Lingver.getInstance().setLocale(LoginActivity.this, MainApplication.language);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        buttonShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });

        if (MainApplication.fingerprint) {
            buttonFingerprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MainApplication.checkSupportFingerprint(LoginActivity.this)) {
                        customDialogFragment = new CustomDialogFragment(LoginActivity.this, "Error", "Your device does not support fingerprint!!!", "error", "Close", "-1", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialogFragment.dismiss();
                            }
                        }, null);
                        customDialogFragment.show(getSupportFragmentManager(), "custom_dialog_fragment");
                        return;
                    }
                    loginWithFingerprint();
                }
            });
        } else {
            ((LinearLayout) findViewById(R.id.layout_fingerprint)).setVisibility(View.GONE);
        }

    }

    private void loginWithFingerprint() {
        BiometricManager biometricManager = BiometricManager.from(LoginActivity.this);

        Executor executor = ContextCompat.getMainExecutor(LoginActivity.this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                String TOKEN = getSharedPreferences("UserSettings", MODE_PRIVATE).getString("TOKEN", "");
                if (TOKEN.isEmpty()) {
                    customDialogFragment = new CustomDialogFragment(LoginActivity.this, "Error", "Token is empty!!!", "error", "Close", "-1", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDialogFragment.dismiss();
                        }
                    }, null);
                } else {
                    try {
                        String userToken = MainApplication.decrypt(TOKEN);
                        String[] parts = userToken.split(",");

                        String username = parts[0];
                        String hashedPassword = parts[1];

                        loginAuthentication(username, hashedPassword, true);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.login_with_fingerprint))
                .setSubtitle(getResources().getString(R.string.fingerprint_message))
                .setNegativeButtonText(getResources().getString(R.string.cancel))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));
    }

    public void login() {
        if (edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
            customDialogFragment = new CustomDialogFragment(LoginActivity.this, "Login failed", "Please enter username and password!!!", "error", "Close", "-1", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogFragment.dismiss();
                }
            }, null);
            customDialogFragment.show(getSupportFragmentManager(), "custom_dialog_fragment");
            return;
        } else {
            loginAuthentication(edtUsername.getText().toString(), edtPassword.getText().toString(), false);
        }
    }

    public void loginAuthentication(String username, String password, Boolean isFingerprint) {
        UserServiceImpl userService = new UserServiceImpl(LoginActivity.this);
        User user = null;
        if (!isFingerprint) {
            user = userService.verifyLoginData(username, password);
        } else {
            user = userService.verifyLoginWithHashedPassword(username, password);
        }

        if (user == null) {
            customDialogFragment = new CustomDialogFragment(LoginActivity.this, "Login failed", "Username or password is incorrect!!!", "error", "Close", "-1", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogFragment.dismiss();
                }
            }, null);
            customDialogFragment.show(getSupportFragmentManager(), "custom_dialog_fragment");
        } else {
            MainApplication.curentUser = user;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}