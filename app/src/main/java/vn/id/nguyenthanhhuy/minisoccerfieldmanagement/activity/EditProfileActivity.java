package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        String[] genderItems = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(
                this,
                R.layout.item_custom_text_spinner,
                genderItems);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) binding.spinnerGender).setAdapter(adapterGender);

        String[] roleItems = new String[]{"Staff", "Manager"};
        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(
                this,
                R.layout.item_custom_text_spinner,
                roleItems);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) binding.spinnerRole).setAdapter(adapterRole);

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

        binding.editTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String regex = "^\\d{10}$";

                if (!s.toString().matches(regex)) {
                    binding.editTextPhoneNumber.setError(getResources().getString(R.string.error_phone_number_incorrect_format));
                }
            }
        });

        binding.editTextDateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                format.setLenient(false);
                String dateOfBirth = s.toString();

                try {
                    format.parse(dateOfBirth);
                } catch (ParseException e) {
                    String temp = s.toString();
                    int numberCharSlash = temp.length() - temp.replace("/", "").length();
                    if (numberCharSlash == 2) {
                        binding.editTextDateOfBirth.setError(getResources().getString(R.string.error_date_of_birth_not_exist));
                    } else {
                        binding.editTextDateOfBirth.setError(getResources().getString(R.string.error_date_of_birth_incorrect_format));
                    }
                }
            }
        });
    }

    public void saveProfile(View view) {
        String fullName = binding.editTextFullName.getText().toString().trim();
        String dateOfBirth = binding.editTextDateOfBirth.getText().toString().trim();
        String phoneNumber = binding.editTextPhoneNumber.getText().toString().trim();

        if (fullName.equals("")) {
            binding.editTextFullName.setError(getResources().getString(R.string.error_full_name_empty));
        }
        if (dateOfBirth.equals("")) {
            binding.editTextDateOfBirth.setError(getResources().getString(R.string.error_date_of_birth_empty));
        }
        if (phoneNumber.equals("")) {
            binding.editTextPhoneNumber.setError(getResources().getString(R.string.error_phone_number_empty));
        }
    }
}