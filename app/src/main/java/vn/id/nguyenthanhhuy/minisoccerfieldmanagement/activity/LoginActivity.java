package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import java.io.Console;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserServiceImpl userService = new UserServiceImpl(LoginActivity.this);
        User user = new User();
        user.setName("Nguyễn Thiện Luân");
        user.setUserName("thienluan");
        user.setPassword("password");
        user.setId(CurrentTimeID.nextId("user"));
        user.setDateOfBirth("08/12/2003");
        user.setImage(null);
        user.setPhoneNumber("0889192145");
        user.setRole("Manager");
        user.setDeleted(false);
        user.setType(null);
        user.setGender("Nam");
        userService.update(user);
        System.out.println("Checking log NTLuan");


        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        setStatusBarColor();
        Lingver.getInstance().setLocale(LoginActivity.this, MainApplication.language);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));
    }

    public void login(){
        // Kiểm tra xem người dùng đã nhập đủ thông tin chưa
        if (edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return; // Kết thúc hàm
        }
        else{
            // Gọi hàm verifyLoginData trong UserServiceImpl để kiểm tra thông tin đăng nhập
            UserServiceImpl userService = new UserServiceImpl(LoginActivity.this);
            User user = userService.verifyLoginData(edtUsername.getText().toString(), edtPassword.getText().toString());

            // Nếu thông tin đăng nhập không chính xác
            if (user == null) {
                Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
            }
            else {
                // Nếu thông tin đăng nhập chính xác, chuyển sang màn hình Dashboard
                MainApplication.curentUser = user;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}