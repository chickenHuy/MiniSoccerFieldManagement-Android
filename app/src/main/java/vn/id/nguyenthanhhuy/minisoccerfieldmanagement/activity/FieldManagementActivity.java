package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewFieldAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.FieldServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class FieldManagementActivity extends AppCompatActivity {

    List<Field> fieldList;
    ListViewFieldAdapter listViewFieldAdapter;
    ListView lvFields;
    Button btnAddField, btnBack;
    RadioGroup rdgTypeField;
    IFieldService fieldService;
    RadioGroup rbTypeField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_management);
        setWigets();
        setEvents();
        Utils.setStatusBarColor(this);
    }

    private void setEvents() {
        // Trong FieldManagementActivity.java
        lvFields.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Field selectedField = fieldList.get(position);

                Bundle args = new Bundle();
                args.putSerializable("fieldSelected", (Serializable) selectedField);
                Intent intent = new Intent(FieldManagementActivity.this, EditOrAddFieldActivity.class);

                intent.putExtra("args", args);
                startActivity(intent);
            }

        });

        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent mới để mở EditOrAddFieldActivity
                Intent intent = new Intent(FieldManagementActivity.this, EditOrAddFieldActivity.class);
                // Mở EditOrAddFieldActivity
                startActivity(intent);
            }
        });

        rdgTypeField.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdbField7) {
                fieldList = fieldService.findAllCombinedField();
                listViewFieldAdapter.addAll(fieldList);
            } else if (checkedId == R.id.rdbField5) {
                fieldList = fieldService.findAllNormalField();
            }
            listViewFieldAdapter.clear();
            listViewFieldAdapter.addAll(fieldList);
        });
    }

    private void setWigets() {
        lvFields = findViewById(R.id.lvFields);
        btnAddField = findViewById(R.id.btnAddField);
        btnBack = findViewById(R.id.btnBack);
        rdgTypeField = findViewById(R.id.rdgTypeField);
        fieldList = new ArrayList<Field>();
        fieldService = new FieldServiceImpl(this);
        fieldList = fieldService.findAllNormalField();
        listViewFieldAdapter = new ListViewFieldAdapter(this, R.layout.item_list_field_in_activity_fieldmanagement);
        listViewFieldAdapter.addAll(fieldList);
        Toast.makeText(this,  fieldList.get(0).getId() + "             a", Toast.LENGTH_SHORT).show();
        lvFields.setAdapter(listViewFieldAdapter);
    }



}