package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.TextView;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditProfileActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.FieldManagementActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.SettingMembershipActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.SettingPriceListActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentShowUserProfileBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;

public class ShowUserProfileFragment extends Fragment {

    public static final int EDIT_PROFILE_INFORMATION = 1;
    public static final int EDIT_PROFILE_INFORMATION_SUCCESSFULLY = 1;
    public static final int CHANGE_PASSWORD = 3;
    public static final int CHANGE_PASSWORD_SUCCESSFULLY = 4;
    public static final int FIELD_MANAGEMENT = 5;

    private FragmentShowUserProfileBinding binding;
    private User currentUser;
    private TextView text_view_name, text_view_phone_number, text_view_username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        currentUser = (User) MainApplication.curentUser;
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

    private void setInformation(){
        text_view_name.setText(currentUser.getName());
        text_view_phone_number.setText(currentUser.getPhoneNumber());
        text_view_username.setText(currentUser.getUserName());
    }

    public void setWidget() {

        binding.switchVietnamese.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isShown()) {
                return;
            }
            if (isChecked) {
                MainApplication.language = "vi";
            } else {
                MainApplication.language = "en";
            }
            Lingver.getInstance().setLocale(requireActivity(), MainApplication.language);
            MainApplication.isChangeLanguage = true;
            requireActivity().recreate();
        });

        binding.buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);

            intent.putExtra("option", "editProfile");
            intent.putExtra("currentUser", currentUser);
            startActivityForResult(intent, EDIT_PROFILE_INFORMATION);
        });

        binding.btnFieldManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FieldManagementActivity.class);
            startActivityForResult(intent, FIELD_MANAGEMENT);
        });

        binding.settings.inflateMenu(R.menu.setting_menu);
        binding.settings.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_price_list)
            {
                Intent intent = new Intent(getActivity(), SettingPriceListActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.action_membership) {
                Intent intent = new Intent(getActivity(), SettingMembershipActivity.class);
                startActivity(intent);

            }
            return false;
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
}