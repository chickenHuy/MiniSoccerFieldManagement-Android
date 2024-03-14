package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

public class EditProfileActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setStatusBarColor();
        configureWidget();
    }

    public void setStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, getTheme()));
    }

    public void configureWidget() {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_gender);
//        Change it
        autoCompleteTextView.setText("Men");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.add("Men");
        adapter.add("Women");
        adapter.add("Other");
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setDropDownBackgroundResource(R.drawable.background_white_radius_10dp);
    }

    public void saveChange(View view) {

    }
}