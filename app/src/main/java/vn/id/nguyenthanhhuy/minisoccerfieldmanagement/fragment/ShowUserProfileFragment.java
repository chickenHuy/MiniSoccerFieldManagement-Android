package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yariksoffice.lingver.Lingver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ChangePasswordActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.DataStatistics;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditProfileActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EmployeeManagementActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.FieldManagementActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ServiceManagementActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.SettingMembershipActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.SettingPriceListActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentShowUserProfileBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ShowUserProfileFragment extends Fragment {

    public static final int EDIT_PROFILE_INFORMATION = 1;
    public static final int EDIT_PROFILE_INFORMATION_SUCCESSFULLY = 1;
    public static final int CHANGE_PASSWORD = 3;
    public static final int CHANGE_PASSWORD_SUCCESSFULLY = 4;
    public static final int FIELD_MANAGEMENT = 5;

    private FragmentShowUserProfileBinding binding;
    private User currentUser;
    private UserServiceImpl userService;
    private TextView text_view_name, text_view_phone_number, text_view_username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        currentUser = (User) MainApplication.curentUser;
        userService = new UserServiceImpl(getContext());
        binding = FragmentShowUserProfileBinding.inflate(inflater, container, false);
        text_view_name = binding.textViewName;
        text_view_username = binding.textViewUsername;
        text_view_phone_number = binding.textViewPhoneNumber;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLanguage();
        setWidget();
        setInformation();
    }

    public void setLanguage() {
        if (MainApplication.language.equals("vi")) {
            binding.switchVietnamese.setChecked(true);
        } else {
            binding.switchVietnamese.setChecked(false);
        }
    }

    private void setInformation() {
        if (currentUser.getImage() == null) {
            setDefaultImage(binding.imageViewAvatar);
        } else {
            Glide.with(this)
                    .load(Utils.convertByteToBitmap(currentUser.getImage()))
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imageViewAvatar);
        }
        text_view_name.setText(currentUser.getName());
        text_view_phone_number.setText(currentUser.getPhoneNumber());
        text_view_username.setText(currentUser.getUserName());
    }

    public void setWidget() {

        binding.switchVietnamese.setChecked(MainApplication.language.equals("vi"));
        binding.switchNotification.setChecked(MainApplication.notify);

        binding.switchVietnamese.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isShown()) {
                return;
            }
            if (isChecked) {
                MainApplication.language = "vi";
                MainApplication.editor.putString("language", "vi");
                MainApplication.editor.apply();
            } else {
                MainApplication.language = "en";
                MainApplication.editor.putString("language", "en");
                MainApplication.editor.apply();
            }
            Lingver.getInstance().setLocale(requireActivity(), MainApplication.language);
            MainApplication.isChangeLanguage = true;
            requireActivity().recreate();
        });

        binding.switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isShown()) {
                return;
            }
            if (isChecked) {
                MainApplication.notify = true;
                MainApplication.editor.putBoolean("notify", true);
                MainApplication.editor.apply();
            } else {
                MainApplication.notify = false;
                MainApplication.editor.putBoolean("notify", false);
                MainApplication.editor.apply();
            }
        });

        binding.buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);

            intent.putExtra("option", "editProfile");
            intent.putExtra("currentUser", currentUser);
            startActivityForResult(intent, EDIT_PROFILE_INFORMATION);
        });

        binding.editUserAvatar.setOnClickListener(this::editUserAvatar);

        binding.buttonChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            intent.putExtra("option", "changePassword");
            startActivityForResult(intent, CHANGE_PASSWORD);
        });

        binding.btnFieldManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FieldManagementActivity.class);
            startActivityForResult(intent, FIELD_MANAGEMENT);
        });

        binding.settings.inflateMenu(R.menu.setting_menu);
        binding.settings.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_price_list) {
                Intent intent = new Intent(getActivity(), SettingPriceListActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.action_membership) {
                Intent intent = new Intent(getActivity(), SettingMembershipActivity.class);
                startActivity(intent);

            }
            return false;
        });

        binding.buttonContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"chickencontactservice@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact for Mini Soccer Field Management App");
                emailIntent.setPackage("com.google.android.gm");
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Gmail App is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://1drv.ms/b/s!Am7QqX0gxVCPg6dktfbps9TbhruxXQ?e=sTgjI5"));
                startActivity(browserIntent);
            }
        });

        binding.buttonServiceManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceManagementActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonEmplManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmployeeManagementActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonDataStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataStatistics.class);
                startActivity(intent);
            }
        });


        Drawable overflowIcon = binding.settings.getOverflowIcon();

        if (overflowIcon != null) {
            // Tạo một bản sao của biểu tượng và tô màu cho nó
            Drawable newIcon = overflowIcon.mutate();
            newIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP); // Thay đổi màu ở đây

            // Đặt biểu tượng mới cho toolbar
            ActionMenuView toolbar;
            binding.settings.setOverflowIcon(newIcon);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update user information when the fragment resumes
        //setWidget();
        setInformation();
    }

    public void editUserAvatar(View view) {
        // Tạo một AlertDialog để hỏi người dùng
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.choose_option)
                .setPositiveButton(R.string.take_photo, (dialog, which) -> {
                    // Mở camera để chụp ảnh mới
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                })
                .setNegativeButton(R.string.choose_from_gallery, (dialog, which) -> {
                    // Mở thư viện ảnh để chọn ảnh
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                })
                .show();
    }

    public void setDefaultImage(ImageView imageView) {
        AssetManager assetManager = getContext().getAssets();

        try (InputStream is = assetManager.open("defaultImage/none_user.png")) {
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            Bitmap bitmap = null;

            // Nếu người dùng chụp ảnh
            if (requestCode == 0) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            // Nếu người dùng chọn ảnh từ thư viện
            else if (requestCode == 1) {
                Uri selectedImage = data.getData();
                try {
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    Bitmap originalBitmap = BitmapFactory.decodeStream(imageStream);
                    // Nén ảnh
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    originalBitmap.compress(Bitmap.CompressFormat.JPEG, 5, out);
                    Bitmap compressedBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    bitmap = compressedBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                // Gán ảnh cho currentUser
                currentUser.setImage(Utils.convertBitmapToByteArray(bitmap));
                if (userService.update_info(currentUser)) {
                    Toast.makeText(getContext(), "Update Image successfully", Toast.LENGTH_SHORT).show();
                    MainApplication.curentUser = currentUser;
                    onResume();
                } else {
                    Toast.makeText(getContext(), "Update Image failed", Toast.LENGTH_SHORT).show();
                    currentUser.setImage(null);
                }
            }
        }
    }
}