package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.yariksoffice.lingver.Lingver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;

public class EditProfileActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    EditText edtDateOfBirth;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setStatusBarColor();
        configureWidget();
        Lingver.getInstance().setLocale(EditProfileActivity.this, MainApplication.language);

        edtDateOfBirth=(EditText) findViewById(R.id.edtDateOfBirth);
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        TextInputLayout textInputLayoutDateOfBirth = findViewById(R.id.txtInputLayoutDateOfBirth);
        textInputLayoutDateOfBirth.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditProfileActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDateOfBirth.setText(sdf.format(calendar.getTime()));
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));
    }

    public void configureWidget() {

    }

    public void saveChangeProfile(View view) {
        MainApplication.language = "vi";
        Lingver.getInstance().setLocale(EditProfileActivity.this, MainApplication.language);
        recreate();
    }
}