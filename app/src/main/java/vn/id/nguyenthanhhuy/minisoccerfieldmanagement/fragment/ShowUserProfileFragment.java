package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditProfileActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.FieldManagementActivity;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentShowUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLanguage();
        setWidget();
    }

    public void setLanguage() {
        if (MainApplication.language.equals("vi")) {
            binding.switchVietnamese.setChecked(true);
        } else {
            binding.switchVietnamese.setChecked(false);
        }
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
            User currentUser = new User();

            intent.putExtra("option", "editProfile");
            intent.putExtra("currentUser", currentUser);
            startActivityForResult(intent, EDIT_PROFILE_INFORMATION);
        });

        binding.btnFieldManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FieldManagementActivity.class);
            startActivityForResult(intent, FIELD_MANAGEMENT);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}