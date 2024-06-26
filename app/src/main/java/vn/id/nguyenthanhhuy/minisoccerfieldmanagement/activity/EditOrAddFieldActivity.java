package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.FieldChooserFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.OnSaveSelectedFieldsListener;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.FieldServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class EditOrAddFieldActivity extends AppCompatActivity {

    RadioGroup rdoGroupTypeField;
    EditText edtName;
    Switch swtStatus;
    Button btnSave, btnDelete, btnAddField;
    ImageButton btnAddImageField;
    ImageView imgField;
    TextView tvIdSubField1, tvIdSubField2, tvIdSubField3, tvNameSubField1, tvNameSubField2, tvNameSubField3, tvIdField;
    LinearLayout llSubField;
    IFieldService fieldService;
    Bitmap bitmapFieldImage;
    FieldChooserFragment fieldChooserFragment;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_add_field);
        Utils.setStatusBarColor(this);
        setWigets();
        setDefaultImage();
        setEvents();
        setData();
    }

    private void setDefaultImage() {
        try {
            InputStream is = this.getAssets().open("viewPagerImages/background_field_1.png");
            bitmapFieldImage = BitmapFactory.decodeStream(is);
            imgField.setImageBitmap(bitmapFieldImage);
        }
        catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle args = intent.getBundleExtra("args");
            if (args != null) {
                Field field = (Field) args.getSerializable("fieldSelected");
                if (field != null) {
                    edtName.setText(field.getName());
                    if (field.getStatus().equals("inactive")) {
                        swtStatus.setChecked(false);

                    }
                    tvIdField.setText(field.getId());
                    if (field.getImage() == null)
                    {
                        try {
                            InputStream is = this.getAssets().open("viewPagerImages/background_field_1.png");
                            bitmapFieldImage = BitmapFactory.decodeStream(is);
                        }
                        catch (IOException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        bitmapFieldImage = Utils.convertByteToBitmap(field.getImage());

                    }
                    imgField.setImageBitmap(bitmapFieldImage);
                    if (field.getType().equals(StaticString.TYPE_5_A_SIDE)) {
                        rdoGroupTypeField.check(R.id.rdo5aside);
                    } else if (field.getType().equals(StaticString.TYPE_7_A_SIDE)) {
                        rdoGroupTypeField.check(R.id.rdo7aside);
                        llSubField.setVisibility(LinearLayout.VISIBLE);
                        tvIdSubField1.setText(field.getCombineField1());
                        tvIdSubField2.setText(field.getCombineField2());
                        tvIdSubField3.setText(field.getCombineField3());
                        Field field1 = fieldService.findById(field.getCombineField1());
                        Field field2 = fieldService.findById(field.getCombineField2());
                        Field field3 = fieldService.findById(field.getCombineField3());
                        if (field1 != null) {
                            // Set the name of the sub-fields (if they exist
                            tvNameSubField1.setText(field1.getName());
                        }
                        if (field2 != null) {
                            tvNameSubField2.setText(field2.getName());
                        }
                        if (field3 != null) {
                            tvNameSubField3.setText(field3.getName());

                        }
                    }
                    else {
                        rdoGroupTypeField.check(R.id.rdo5aside);
                    }
                }
            }
        }
    }

    private void setEvents() {
        rdoGroupTypeField.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdo7aside) {
                llSubField.setVisibility(LinearLayout.VISIBLE);
            } else if (checkedId == R.id.rdo5aside) {
                llSubField.setVisibility(LinearLayout.GONE);
            }
        });

        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một instance mới của FieldChooserFragment
                Field field1 = fieldService.findById(tvIdSubField1.getText().toString());
                Field field2 = fieldService.findById(tvIdSubField2.getText().toString());
                Field field3 = fieldService.findById(tvIdSubField3.getText().toString());
                List<Field> fields = new ArrayList<Field>();
                if (field1 != null)
                {
                    fields.add(field1);
                }
                if (field2 != null)
                {
                    fields.add(field2);
                }
                if (field3 != null)
                {
                    fields.add(field3);
                }
                if (fields.size() != 0)
                {
                    fieldChooserFragment = FieldChooserFragment.newInstance(fields);
                }
                else
                {
                    fieldChooserFragment = new FieldChooserFragment();
                }
                fieldChooserFragment.setOnSaveSelectedFieldsListener(new OnSaveSelectedFieldsListener() {
                    @Override
                    public void onSaveSelectedFields(List<Field> selectedFields) {
                        tvIdSubField1.setText(selectedFields.get(0).getId());
                        tvNameSubField1.setText(selectedFields.get(0).getName());
                        tvIdSubField2.setText(selectedFields.get(1).getId());
                        tvNameSubField2.setText(selectedFields.get(1).getName());
                        tvIdSubField3.setText(selectedFields.get(2).getId());
                        tvNameSubField3.setText(selectedFields.get(2).getName());

                    }
                });
                fieldChooserFragment.show(getSupportFragmentManager(), "fieldChooserFragment");
            }
        });

        btnAddImageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo AlertDialog để người dùng chọn giữa chụp ảnh và chọn ảnh từ thư viện
                new AlertDialog.Builder(EditOrAddFieldActivity.this)
                        .setTitle("Select Image")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @SuppressLint("QueryPermissionsNeeded")
                            public void onClick(DialogInterface dialog, int which) {
                                // Tạo Intent để mở camera
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        })
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Tạo Intent để mở gallery
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the field information from the UI elements
                String fieldName = edtName.getText().toString();
                boolean fieldStatus = swtStatus.isChecked();
                byte[] fieldImage;
                if (bitmapFieldImage!= null) {
                    fieldImage = Utils.convertBitmapToByteArray(bitmapFieldImage);
                } else {
                    fieldImage = null;
                }
                // Validate the field information
                if (fieldName.isEmpty()) {
                    edtName.setError("Field name is required");
                    return;
                }

                if (rdoGroupTypeField.getCheckedRadioButtonId() == R.id.rdo5aside) {
                    saveFieldInformationNormal(fieldName, fieldStatus, fieldImage);
                }
                else if (rdoGroupTypeField.getCheckedRadioButtonId() == R.id.rdo7aside)                {
                    if (tvIdSubField1.getText().toString().isEmpty() || tvIdSubField2.getText().toString().isEmpty() || tvIdSubField3.getText().toString().isEmpty()) {
                        Toast.makeText(EditOrAddFieldActivity.this, "Please select all sub-fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        saveFieldInformationHasSubField(fieldName, fieldStatus, fieldImage, tvIdSubField1.getText().toString(), tvIdSubField2.getText().toString(), tvIdSubField3.getText().toString());
                    }
                }
            }


        });



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Field field = fieldService.findById(tvIdField.getText().toString());
                if (field != null) {

                    if (fieldService.softDelete(field.getId())) {
                        Toast.makeText(EditOrAddFieldActivity.this, "Field deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditOrAddFieldActivity.this, "Failed to delete field", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }
    private void saveFieldInformationHasSubField(String fieldName, boolean fieldStatus, byte[] fieldImage, String id1, String id2, String id3) {
        Field field = fieldService.findById(String.valueOf(tvIdField.getText()));
        int action = 0;
        if (field  == null) {
            field = new Field();
            field.setId(CurrentTimeID.nextId("F"));
            action = 1;
        }
        field.setName(fieldName);
        field.setType(StaticString.TYPE_7_A_SIDE);
        if (fieldStatus)
        {
            field.setStatus("active");
        }
        else
        {
            field.setStatus("inactive");
        }
        field.setImage(fieldImage);
        field.setCombineField1(tvIdSubField1.getText().toString());
        field.setCombineField2(tvIdSubField2.getText().toString());
        field.setCombineField3(tvIdSubField3.getText().toString());

        if (action == 1)
        {
            if ( fieldService.add(field))
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Field information saved successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Failed to save field information", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (fieldService.update(field))
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Field information saved successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Failed to save field information", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saveFieldInformationNormal(String fieldName, boolean fieldStatus, byte[] fieldImage) {
        int action = 0 ;
        Field field = fieldService.findById(String.valueOf(tvIdField.getText()));
        if (field  == null) {
            field = new Field();
            field.setId(CurrentTimeID.nextId("F"));
            action = 1;
        }
        field.setName(fieldName);
        field.setType(StaticString.TYPE_5_A_SIDE);
        if (fieldStatus)
        {
            field.setStatus("active");
        }
        else
        {
            field.setStatus("inactive");
        }
        field.setImage(fieldImage);
        if (action == 1)
        {
            if ( fieldService.add(field))
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Field information saved successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Failed to save field information", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (fieldService.update(field))
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Field information saved successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditOrAddFieldActivity.this, "Failed to save field information", Toast.LENGTH_SHORT).show();
            }
        }


    }
    private void setWigets() {
        rdoGroupTypeField = findViewById(R.id.rdoGroupTypeField);
        llSubField = findViewById(R.id.llSubField);
        llSubField.setVisibility(LinearLayout.GONE);

        btnAddField = findViewById(R.id.btnAddField);
        btnAddImageField = findViewById(R.id.btnAddImgField);
        btnDelete = findViewById(R.id.btnDeleted);

        imgField = findViewById(R.id.imgField);
        btnSave = findViewById(R.id.btnSave);
        edtName = findViewById(R.id.edtName);
        swtStatus = findViewById(R.id.swtStatus);
        tvIdSubField1 = findViewById(R.id.tvIdSubField1);
        tvIdSubField2 = findViewById(R.id.tvIdSubField2);
        tvIdSubField3 = findViewById(R.id.tvIdSubField3);
        tvIdSubField1.setText("");
        tvIdSubField2.setText("");
        tvIdSubField3.setText("");

        tvNameSubField1 = findViewById(R.id.tvNameSubField1);
        tvNameSubField2 = findViewById(R.id.tvNameSubField2);
        tvNameSubField3 = findViewById(R.id.tvNameSubField3);
        tvNameSubField1.setText("");
        tvNameSubField2.setText("");
        tvNameSubField3.setText("");

        tvIdField = findViewById(R.id.tvIdFields);

        fieldService = new FieldServiceImpl(this);
        fieldChooserFragment = new FieldChooserFragment();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                // Đặt ảnh đã chọn vào ImageView
                bitmapFieldImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgField.setImageBitmap(bitmapFieldImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmapFieldImage  = (Bitmap) extras.get("data");
            imgField.setImageBitmap(bitmapFieldImage);
        }
    }
    public void goBack(View view) {
        finish();
    }


}