package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yariksoffice.lingver.Lingver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityEditProfileBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;

    private String option;
    private User currentUser;
    private User employee_selected;
    LinearLayout roleLayout;
    UserServiceImpl userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        roleLayout = binding.roleLayout;
        setContentView(binding.getRoot());
        userService = new UserServiceImpl(EditProfileActivity.this);

        Lingver.getInstance().setLocale(EditProfileActivity.this, MainApplication.language);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            option = extras.getString("option");
            if (option.equals("editProfile")) {
                currentUser = MainApplication.curentUser;
            }
            if(option.equals("editEmployee")){
                employee_selected = (User) extras.getSerializable("employee");
            }
        }

        setStatusBarColor();
        setWidget();
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));
    }

    public void setWidget() {
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

        if (option.equals("editProfile")) {
            binding.editTextFullName.setText(currentUser.getName());
            binding.editTextDateOfBirth.setText(currentUser.getDateOfBirth());
            binding.editTextPhoneNumber.setText(currentUser.getPhoneNumber());
            binding.spinnerGender.setSelection(currentUser.getGender().equals("Male") ? 0 : currentUser.getGender().equals("Female") ? 1 : 2);
            roleLayout.setVisibility(View.INVISIBLE);
            binding.textViewName.setText(currentUser.getName());
            if (currentUser.getRole().equals("Manager")) {
                binding.textViewRole.setText(getString(R.string.manager));
            } else {
                binding.textViewRole.setText(getString(R.string.staff));
            }
            binding.layoutUsername.setVisibility(View.GONE);
        }
        if(option.equals("editEmployee")){
            binding.textViewProfileTittle.setText(getString(R.string.edit_employee));
            binding.editTextFullName.setText(employee_selected.getName());
            binding.editTextDateOfBirth.setText(employee_selected.getDateOfBirth());
            binding.editTextPhoneNumber.setText(employee_selected.getPhoneNumber());
            binding.spinnerGender.setSelection(employee_selected.getGender().equals("Male") ? 0 : employee_selected.getGender().equals("Female") ? 1 : 2);
            roleLayout.setVisibility(View.INVISIBLE);
            binding.linearLayoutStatus.setVisibility(View.GONE);
            binding.layoutUsername.setVisibility(View.GONE);
        }
        if(option.equals("addEmployee")){
            binding.textViewProfileTittle.setText(getString(R.string.add_employee));
            roleLayout.setVisibility(View.VISIBLE);
            binding.layoutUsername.setVisibility(View.VISIBLE);
            binding.linearLayoutStatus.setVisibility(View.GONE);
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
        String gender;
        int genderPosition = binding.spinnerGender.getSelectedItemPosition();
        if (genderPosition == 0) {
            gender = "Male";
        } else if (genderPosition == 1) {
            gender = "Female";
        }else {
            gender = "Other";
        }

        if (fullName.equals("")) {
            binding.editTextFullName.setError(getResources().getString(R.string.error_full_name_empty));
        }
        if (dateOfBirth.equals("")) {
            binding.editTextDateOfBirth.setError(getResources().getString(R.string.error_date_of_birth_empty));
        }
        if (phoneNumber.equals("")) {
            binding.editTextPhoneNumber.setError(getResources().getString(R.string.error_phone_number_empty));
        }

        // Kiểm tra xem có lỗi nào đang được hiển thị trên các ô text hay không
        if (binding.editTextFullName.getError() != null || binding.editTextDateOfBirth.getError() != null || binding.editTextPhoneNumber.getError() != null) {
            Toast.makeText(this, "Update profile failed", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem người dùng có đủ 18 tuổi không
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date userDateOfBirth = format.parse(dateOfBirth);
            Calendar userCalendar = Calendar.getInstance();
            if (userDateOfBirth != null) {
                userCalendar.setTime(userDateOfBirth);
            }

            Calendar currentCalendar = Calendar.getInstance();
            int currentYear = currentCalendar.get(Calendar.YEAR);
            int userYear = userCalendar.get(Calendar.YEAR);

            if (currentYear - userYear < 18) {
                Toast.makeText(this, "User must be at least 18 years old", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (option.equals("editProfile")) {
            currentUser.setName(fullName);
            currentUser.setDateOfBirth(dateOfBirth);
            currentUser.setPhoneNumber(phoneNumber);
            currentUser.setGender(gender);
            System.out.println(currentUser.getId());
            if (userService.update_info(currentUser)) {
                Toast.makeText(this, "Update profile successfully", Toast.LENGTH_SHORT).show();
                MainApplication.curentUser = currentUser;
                setWidget();
            } else {
                Toast.makeText(this, "Update profile failed", Toast.LENGTH_SHORT).show();
            }
            System.out.println(currentUser.getName());
        }

        if(option.equals("editEmployee")) {
            employee_selected.setName(fullName);
            employee_selected.setDateOfBirth(dateOfBirth);
            employee_selected.setPhoneNumber(phoneNumber);
            employee_selected.setGender(gender);

            if (userService.update_info(employee_selected)) {
                Toast.makeText(this, "Update employee successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this, EmployeeManagementActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Update employee failed", Toast.LENGTH_SHORT).show();
            }
        }

        if(option.equals("addEmployee")) {
            int rolePosition = binding.spinnerRole.getSelectedItemPosition();
            String role;
            if (rolePosition == 0) {
                role = "Staff";
            } else{
                role = "Manager";
            }

            String id = CurrentTimeID.nextId("user");
            String name = fullName;
            String userName = binding.editTextUsername.getText().toString().trim();
            String password = "123456";
            String type = null;
            byte[] image = null;
            User newEmployee = new User(id,name,gender,dateOfBirth,image,phoneNumber,userName,password,role,type,false,null,null);
            if (userService.add(newEmployee)) {
                Toast.makeText(this, "Add employee successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this, EmployeeManagementActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Add employee failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goBack(View view) {
        finish();
    }
}