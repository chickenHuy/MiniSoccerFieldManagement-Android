package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.FieldChooserFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class EditOrAddFieldActivity extends AppCompatActivity {

    RadioGroup rdoGroupTypeField;
    EditText edtName;
    Switch swtStatus;
    Button btnSave, btnDelete, btnAddField;
    ImageButton btnAddImageField;
    ImageView imgField;
    TextView tvIdSubField1, tvIdSubField2, tvIdSubField3, tvNameSubField1, tvNameSubField2, tvNameSubField3;
    LinearLayout llSubField;
    IFieldService fieldService;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_add_field);
        Utils.setStatusBarColor(this);
        setWigets();
        setEvents();
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
                FieldChooserFragment fieldChooserFragment = new FieldChooserFragment();
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
                            public void onClick(DialogInterface dialog, int which) {
                                // Tạo Intent để mở camera
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
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
                byte[] fieldImage = Utils.convertBitmapToByteArray(((BitmapDrawable) imgField.getDrawable()).getBitmap());
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


                // Show a success message
                Toast.makeText(EditOrAddFieldActivity.this, "Field information saved successfully", Toast.LENGTH_SHORT).show();
            }


        });


    }
    private void saveFieldInformationHasSubField(String fieldName, boolean fieldStatus, byte[] fieldImage, String id1, String id2, String id3) {
    }
    private void saveFieldInformationNormal(String fieldName, boolean fieldStatus, byte[] fieldImage) {

    }
    private void setWigets() {
        rdoGroupTypeField = findViewById(R.id.rdoGroupTypeField);
        llSubField = findViewById(R.id.llSubField);
        btnAddField = findViewById(R.id.btnAddField);
        btnAddImageField = findViewById(R.id.btnAddImgField);
        imgField = findViewById(R.id.imgField);
        btnSave = findViewById(R.id.btnSave);
        edtName = findViewById(R.id.edtName);
        swtStatus = findViewById(R.id.swtStatus);
        tvIdSubField1 = findViewById(R.id.tvIdSubField1);
        tvIdSubField2 = findViewById(R.id.tvIdSubField2);
        tvIdSubField3 = findViewById(R.id.tvIdSubField3);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                // Đặt ảnh đã chọn vào ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgField.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgField.setImageBitmap(imageBitmap);
        }
    }
}