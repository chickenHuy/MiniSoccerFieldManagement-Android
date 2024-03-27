package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewFieldAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public class FieldManagementActivity extends AppCompatActivity {

    List<Field> fieldList;
    ListViewFieldAdapter listViewFieldAdapter;
    ListView lvFields;
    Button btnAddField, btnBack;
    RadioGroup rdgTypeField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_management);
        setWigets();
    }

    private void setWigets() {
        lvFields = findViewById(R.id.lvFields);
        btnAddField = findViewById(R.id.btnAddField);
        btnBack = findViewById(R.id.btnBack);
        rdgTypeField = findViewById(R.id.rdgTypeField);
        fieldList = new ArrayList<Field>();
        for (int i = 1; i <= 10; i++) {
            String id = "id" + i;
            String name = "Field " + i;
            String status = i % 2 == 0 ? "Active" : "Inactive";
            String type = i % 3 == 0 ? "Type A" : "Type B";
            byte[] image = new byte[0];
            String combineField1 = "Combine " + i;
            String combineField2 = "Combine " + (i + 1);
            String combineField3 = "Combine " + (i + 2);
            Boolean isDeleted = false;
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

            Field field = new Field(id, name, status, type, image, combineField1, combineField2, combineField3, isDeleted, createdAt, updatedAt);
            fieldList.add(field);
        }
        listViewFieldAdapter = new ListViewFieldAdapter(this, R.layout.item_list_field_in_activity_fieldmanagement);
        listViewFieldAdapter.addAll(fieldList);
        lvFields.setAdapter(listViewFieldAdapter);
    }
}