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
            loadFieldData(checkedId);
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
        lvFields.setAdapter(listViewFieldAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            loadFieldData(rdgTypeField.getCheckedRadioButtonId());
            listViewFieldAdapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFieldData(int checkedId) {
        if (checkedId == R.id.rdbField7) {
            fieldList = fieldService.findAllCombinedField();
            listViewFieldAdapter.addAll(fieldList);
        } else if (checkedId == R.id.rdbField5) {
            fieldList = fieldService.findAllNormalField();
        }
        listViewFieldAdapter.clear();
        listViewFieldAdapter.addAll(fieldList);
    }


    public void goBack(View view) {
        finish();
    }
}