package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityChangePasswordBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.PasswordUtils;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private User currentUser;
    private UserServiceImpl userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userService = new UserServiceImpl(ChangePasswordActivity.this);
        currentUser = MainApplication.curentUser;

        setWidget();
        setStatusBarColor();
        Lingver.getInstance().setLocale(ChangePasswordActivity.this, MainApplication.language);
    }

    private void setWidget() {
        if (currentUser.getRole().equals("Manager")) {
            binding.textViewRole.setText(getString(R.string.manager));
        } else {
            binding.textViewRole.setText(getString(R.string.staff));
        }
        binding.textViewName.setText(currentUser.getName());
        binding.editTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6 || s.length() > 20){
                    binding.editTextNewPassword.setError(getResources().getString(R.string.error_password_too_short_or_long));
                }
            }
        });

        binding.editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = binding.editTextNewPassword.getText().toString();
                if (!s.toString().equals(newPassword)) {
                    binding.editTextConfirmPassword.setError(getResources().getString(R.string.error_password_not_match));
                }
            }
        });
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));
    }

    public void saveChangePassword(View view){
        String oldPassword = binding.editTextCurrentPassword.getText().toString().trim();
        String newPassword = binding.editTextNewPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

        if (oldPassword.equals("")) {
            binding.editTextCurrentPassword.setError(getResources().getString(R.string.error_password_empty));
        }
        if (newPassword.equals("")) {
            binding.editTextNewPassword.setError(getResources().getString(R.string.error_password_empty));
        }
        if (confirmPassword.equals("")) {
            binding.editTextConfirmPassword.setError(getResources().getString(R.string.error_password_empty));
        }

        // Kiểm tra xem có lỗi nào đang được hiển thị trên các ô text hay không
        if (binding.editTextCurrentPassword.getError() != null || binding.editTextNewPassword.getError() != null || binding.editTextConfirmPassword.getError() != null) {
            Toast.makeText(this, "Change password failed", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện thay đổi mật khẩu
        else {
            if (!currentUser.getPassword().equals(PasswordUtils.hashPassword(oldPassword))) {
                binding.editTextCurrentPassword.setError(getResources().getString(R.string.error_password_incorrect));
                Toast.makeText(this, "Change password failed", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                currentUser.setPassword(newPassword);
                userService.changePassword(currentUser.getId(), newPassword);
                Toast.makeText(this, "Change password successfully", Toast.LENGTH_SHORT).show();
                // Xóa dữ liệu người dùng hiện tại
                MainApplication.curentUser = null;

                // Quay trở lại trang đăng nhập
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                finish();
            }
        }
    }
    public void goBack(View view) {
        finish();
    }
}