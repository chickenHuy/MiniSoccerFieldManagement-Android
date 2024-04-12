package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;

public class EditOrAddCustomerActivity extends AppCompatActivity {
    private ImageView ivAvatar;
    private TextView customerHeading;
    private String option;
    private EditText edtName, edtPhone;
    private Button btnCancel, btnSave, btnBack;
    private Customer customer_selected;
    private CustomerServiceImpl customerService;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_add_customer);
        customerService = new CustomerServiceImpl(this);

        Intent intent = getIntent();
        if (intent != null) {
            option = intent.getStringExtra("option");
            if(option.equals("editCustomer")){
                String customer_id = intent.getStringExtra("customer_id");
                customer_selected = customerService.findById(customer_id);
            }
        }
        setWidgets();

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditOrAddCustomerActivity.this)
                        .setTitle("Select Image")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dispatchTakePictureIntent();
                            }
                        })
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                openGallery();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        if (ivAvatar.getDrawable() == null) {
            Bitmap bitmap;
            try {
                InputStream is = this.getAssets().open("defaultImage/none_user.png");
                bitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
            ivAvatar.setImageBitmap(bitmap);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap avatarBitmap = ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap();
                String name = edtName.getText().toString();
                String phoneNumber = edtPhone.getText().toString();

                if (name.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(EditOrAddCustomerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidName(name)) {
                    Toast.makeText(EditOrAddCustomerActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                    edtName.requestFocus();
                    return;
                }

                byte[] avatarByteArray = bitmapToByteArray(avatarBitmap);

                if(option.equals("editCustomer")){
                    customer_selected.setName(name);
                    customer_selected.setPhoneNumber(phoneNumber);
                    customer_selected.setImage(avatarByteArray);
                    if(customerService.update(customer_selected)){
                        Toast.makeText(EditOrAddCustomerActivity.this, "Customer updated successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(EditOrAddCustomerActivity.this, "Failed to update customer", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setPhoneNumber(phoneNumber);
                    customer.setImage(avatarByteArray);
                    customer.setId(CurrentTimeID.nextId("C"));
                    customer.setMemberShipId("1");
                    customer.setTotalSpend(BigDecimal.ZERO);

                    boolean isSaved = customerService.add(customer);

                    if (isSaved) {
                        Toast.makeText(EditOrAddCustomerActivity.this, "Customer saved successfully", Toast.LENGTH_SHORT).show();
                        edtName.setText("");
                        edtPhone.setText("");
                        edtName.requestFocus();
                    } else {
                        Toast.makeText(EditOrAddCustomerActivity.this, "Failed to save customer", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtName.setText("");
                edtPhone.setText("");
                edtName.requestFocus();
            }
        });
    }

    private void setWidgets() {
        customerHeading = (TextView) findViewById(R.id.customer_heading);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPhone = (EditText) findViewById(R.id.edtPhoneNumber);
        ivAvatar = findViewById(R.id.avatar_image);
        btnSave = findViewById(R.id.save_button);
        btnCancel = findViewById(R.id.cancel_button);
        btnBack = findViewById(R.id.btnBack);

        edtPhone.addTextChangedListener(new TextWatcher() {
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
                    edtPhone.setError(getResources().getString(R.string.error_phone_number_incorrect_format));
                }
            }
        });

        if(option.equals("editCustomer")){
            customerHeading.setText(getString(R.string.edit_customer));
            edtName.setText(customer_selected.getName());
            edtPhone.setText(customer_selected.getPhoneNumber());
            if (customer_selected.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(customer_selected.getImage(), 0, customer_selected.getImage().length);
                ivAvatar.setImageBitmap(bitmap);
            } else {
                Bitmap bitmap;
                try {
                    InputStream is = this.getAssets().open("defaultImage/none_user.png");
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmap = null;
                }
                ivAvatar.setImageBitmap(bitmap);
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\p{L}\\s'-]+");
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            ivAvatar.setImageURI(selectedImageUri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivAvatar.setImageBitmap(imageBitmap);
        }
    }
}