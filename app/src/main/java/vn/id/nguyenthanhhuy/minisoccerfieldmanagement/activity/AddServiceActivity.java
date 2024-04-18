package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityAddServiceBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.CustomDialogFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

@RequiresApi(api = 26)
public class AddServiceActivity extends AppCompatActivity {

    private ActivityAddServiceBinding binding;
    private String option = "";
    private Service currentService = null;
    private Bitmap bitmapServiceImage = null;
    private CustomDialogFragment customDialogFragment;
    private boolean hasChange = false;

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
                if (hasChange) {
                    setResult(ServiceManagementActivity.EDIT_SERVICE_SUCCESSFULLY);
                    finish();
                } else {
                    finish();
                }
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
                new AlertDialog.Builder(AddServiceActivity.this)
                        .setTitle(R.string.choose_option)
                        .setPositiveButton(R.string.take_photo, (dialog, which) -> {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        })
                        .setNegativeButton(R.string.choose_from_gallery, (dialog, which) -> {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 1);
                        })
                        .show();
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (option.equals("add")) {
                    if (checkFieldInput()) {
                        if (insertService()) {
                            hasChange = true;
                            customDialogFragment = new CustomDialogFragment(AddServiceActivity.this, getResources().getString(R.string.success), "", "success", "", getResources().getString(R.string.string_continue), null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogFragment.dismiss();
                                    setResult(ServiceManagementActivity.ADD_SERVICE_SUCCESSFULLY);
                                    finish();
                                }
                            });
                            customDialogFragment.show(getSupportFragmentManager(), "custom_dialog_notify");
                        }
                    }
                } else if (option.equals("edit")) {
                    if (checkFieldInput()) {
                        if (editService()) {
                            hasChange = true;
                            customDialogFragment = new CustomDialogFragment(AddServiceActivity.this, getResources().getString(R.string.success), "", "success", "", getResources().getString(R.string.string_continue), null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogFragment.dismiss();
                                    setResult(ServiceManagementActivity.EDIT_SERVICE_SUCCESSFULLY);
                                    finish();
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
        if (option.equals("edit")) {
            newService.setId(currentService.getId());
        } else {
            newService.setId(CurrentTimeID.nextId("SV"));
        }
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

        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;

            if (requestCode == 0) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            else if (requestCode == 1) {
                Uri selectedImage = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                binding.imageViewServiceImage.setImageBitmap(bitmap);
                bitmapServiceImage = bitmap;
            }
        }
    }
}