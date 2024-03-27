package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import java.util.Calendar;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityEditProfileBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;

    private String option;
    private User currentUer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Lingver.getInstance().setLocale(EditProfileActivity.this, MainApplication.language);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            option = extras.getString("option");
            if (option.equals("editProfile")) {
                currentUer = (User) extras.getSerializable("currentUser");
            }

            Toast.makeText(this, option, Toast.LENGTH_SHORT).show();
        }

        setStatusBarColor();
        setWidget();
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));
    }

    public void setWidget() {
        if (option.equals("editProfile")) {

        }
        binding.buttonShowCalender.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        binding.editTextDateOfBirth.setText(date);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    public void saveChangeProfile(View view) {

    }
}