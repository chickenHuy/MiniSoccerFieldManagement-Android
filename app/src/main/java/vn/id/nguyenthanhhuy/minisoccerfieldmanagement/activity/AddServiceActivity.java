package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import java.io.IOException;
import java.math.BigDecimal;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityAddServiceBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.CustomDialogFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class AddServiceActivity extends AppCompatActivity {

    private ActivityAddServiceBinding binding;
    private String option = "";
    private Service currentService = null;
    private Bitmap bitmapServiceImage = null;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Lingver.getInstance().setLocale(AddServiceActivity.this, MainApplication.language);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));

        getData();
        setWidget();
    }

    public void getData() {
        Intent intent = getIntent();
        option = intent.getStringExtra("option");

        if (option.equals("view") || option.equals("edit")) {
            String serviceId = intent.getStringExtra("serviceId");
            try {
                currentService = new ServiceServiceImpl(AddServiceActivity.this).findById(serviceId);
            } catch (Exception e) {
                Toast.makeText(this, "Send service is error!!!", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }
    }

    public void setWidget() {

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (option.equals("add")) {
            binding.textViewActivityTittle.setText(getResources().getString(R.string.add_service));
        } else if (option.equals("edit")) {
            binding.textViewActivityTittle.setText(getResources().getString(R.string.update_service));

            binding.editTextServiceName.setText(currentService.getName());
            binding.editTextServiceUnit.setText(currentService.getUnit());
            binding.editTextServicePrice.setText(String.valueOf(currentService.getPrice()));
            binding.editTextServiceQuantity.setText(String.valueOf(currentService.getQuantity()));
            binding.editTextServiceDescription.setText(currentService.getDescription());
            if (currentService.getStatus() != null && currentService.getStatus().equals("Active")) {
                binding.switchActive.setChecked(true);
            } else {
                binding.switchActive.setChecked(false);
            }
            if (currentService.getImage() != null) {
                bitmapServiceImage = Utils.convertByteToBitmap(currentService.getImage());
                binding.imageViewServiceImage.setImageBitmap(bitmapServiceImage);
            }
        } else if (option.equals("view")) {

            binding.textViewActivityTittle.setText(getResources().getString(R.string.view_service_details));

            binding.imageButtonAddImage.setVisibility(View.GONE);
            binding.textViewRequire0.setVisibility(View.INVISIBLE);
            binding.textViewRequire1.setVisibility(View.INVISIBLE);
            binding.textViewRequire2.setVisibility(View.INVISIBLE);
            binding.textViewRequire3.setVisibility(View.INVISIBLE);

            binding.buttonSave.setVisibility(View.GONE);
            binding.textInputLayoutServiceSold.setVisibility(View.VISIBLE);

            binding.editTextServiceName.setKeyListener(null);
            binding.editTextServiceUnit.setKeyListener(null);
            binding.editTextServicePrice.setKeyListener(null);
            binding.editTextServiceQuantity.setKeyListener(null);
            binding.editTextServiceSold.setKeyListener(null);
            binding.editTextServiceDescription.setKeyListener(null);
            binding.switchActive.setKeyListener(null);

            binding.editTextServiceName.setText(currentService.getName());
            binding.editTextServiceUnit.setText(currentService.getUnit());
            binding.editTextServicePrice.setText(Utils.formatVND(currentService.getPrice()));
            binding.editTextServiceQuantity.setText(String.valueOf(currentService.getQuantity()));
            binding.editTextServiceSold.setText(String.valueOf(currentService.getSold()));
            binding.editTextServiceDescription.setText(currentService.getDescription());
            if (currentService.getStatus() != null && currentService.getStatus().equals("Active")) {
                binding.switchActive.setChecked(true);
            } else {
                binding.switchActive.setChecked(false);
            }
            if (currentService.getImage() != null) {
                bitmapServiceImage = Utils.convertByteToBitmap(currentService.getImage());
                binding.imageViewServiceImage.setImageBitmap(bitmapServiceImage);
            }
        }


        binding.imageButtonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddServiceActivity.this).setTitle("Select Image").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (option.equals("add")) {
                    if (checkFieldInput()) {
                        if (insertService()) {
                            CustomDialogFragment customDialogFragment = new CustomDialogFragment(AddServiceActivity.this, getResources().getString(R.string.success), "", "success", "", getResources().getString(R.string.string_continue), null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AddServiceActivity.this, ServiceManagementActivity.class);
                                    startActivityForResult(intent, ServiceManagementActivity.ADD_SERVICE_SUCCESSFULLY);
                                }
                            });
                            customDialogFragment.show(getSupportFragmentManager(), "custom_dialog_notify");
                        }
                    }
                } else if (option.equals("edit")) {
                    if (checkFieldInput()) {
                        if (editService()) {
                            CustomDialogFragment customDialogFragment = new CustomDialogFragment(AddServiceActivity.this, getResources().getString(R.string.success), "", "success", "", getResources().getString(R.string.string_continue), null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AddServiceActivity.this, ServiceManagementActivity.class);
                                    startActivityForResult(intent, ServiceManagementActivity.EDIT_SERVICE_SUCCESSFULLY);
                                }
                            });
                            customDialogFragment.show(getSupportFragmentManager(), "custom_dialog_notify");
                        }
                    }
                }
            }
        });
    }

    public boolean checkFieldInput() {
        boolean check = true;
        if (binding.editTextServiceName.getText().toString().trim().isEmpty()) {
            binding.editTextServiceName.setError(getResources().getText(R.string.error_empty));
            check = false;
        }
        if (binding.editTextServiceUnit.getText().toString().trim().isEmpty()) {
            binding.editTextServiceUnit.setError(getResources().getText(R.string.error_empty));
            check = false;
        }
        if (binding.editTextServicePrice.getText().toString().trim().isEmpty()) {
            binding.editTextServicePrice.setError(getResources().getText(R.string.error_empty));
            check = false;
        }
        if (binding.editTextServiceQuantity.getText().toString().trim().isEmpty()) {
            binding.editTextServiceQuantity.setError(getResources().getText(R.string.error_empty));
            check = false;
        }
        if (!binding.editTextServicePrice.getText().toString().trim().matches("^[0-9]+$")) {
            binding.editTextServicePrice.setError(getResources().getText(R.string.error_contains_non_number_character));
            check = false;
        }
        if (!binding.editTextServiceQuantity.getText().toString().trim().matches("^[0-9]+$")) {
            binding.editTextServiceQuantity.setError(getResources().getText(R.string.error_contains_non_number_character));
            check = false;
        }
        return check;
    }

    public Service getDataFromForm() {
        Service newService = new Service();
        newService.setId(currentService.getId());
        newService.setName(binding.editTextServiceName.getText().toString().trim());
        newService.setUnit(binding.editTextServiceUnit.getText().toString().trim());
        newService.setPrice(new BigDecimal(binding.editTextServicePrice.getText().toString().trim()));
        newService.setQuantity(Integer.parseInt(binding.editTextServiceQuantity.getText().toString().trim()));
        newService.setSold(0);
        newService.setDescription(binding.editTextServiceDescription.getText().toString().trim());
        if (binding.switchActive.isChecked()) {
            newService.setStatus("Active");
        } else {
            newService.setStatus("Inactive");
        }
        if (bitmapServiceImage != null) {
            newService.setImage(Utils.convertBitmapToByteArray(bitmapServiceImage));
        } else {
            newService.setImage(null);
        }
        return newService;
    }

    public boolean insertService() {
        ServiceServiceImpl service = new ServiceServiceImpl(AddServiceActivity.this);
        try {
            return service.add(getDataFromForm());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editService() {
        ServiceServiceImpl service = new ServiceServiceImpl(AddServiceActivity.this);
        try {
            return service.update(getDataFromForm());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                bitmapServiceImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                binding.imageViewServiceImage.setImageBitmap(bitmapServiceImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmapServiceImage = (Bitmap) extras.get("data");
            binding.imageViewServiceImage.setImageBitmap(bitmapServiceImage);
        }
    }
}