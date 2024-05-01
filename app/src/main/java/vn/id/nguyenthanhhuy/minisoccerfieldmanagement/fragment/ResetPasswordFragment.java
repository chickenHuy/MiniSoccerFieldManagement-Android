package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import static java.lang.Math.random;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.UUID;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IUserDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.UserDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.LoginActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentResetPasswordBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IUserService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;

public class ResetPasswordFragment extends DialogFragment {

    private FragmentResetPasswordBinding binding;
    private Activity activity;
    private User user;
    private IUserDAO userDAO;
    private IUserService userService;
    CustomDialogFragment customDialogFragment;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public ResetPasswordFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentResetPasswordBinding.inflate(getLayoutInflater());
        userDAO = new UserDAOImpl(activity);
        userService = new UserServiceImpl(activity);
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Quyền SEND_SMS đã được cấp, bạn có thể gửi tin nhắn ở đây
                } else {
                    // Quyền SEND_SMS đã bị từ chối, bạn cần xử lý trường hợp này
                }
                return;
            }
            // Các trường hợp quyền khác bạn muốn xử lý
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding.imageViewIconNotify.setImageResource(R.mipmap.ic_launcher);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fall_down);
        binding.imageViewIconNotify.startAnimation(animation);

        binding.buttonAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.buttonAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.edtUsername.getText().toString();
                if (username.isEmpty()) {
                    binding.edtUsername.setError("Username is required");
                    binding.edtUsername.requestFocus();
                    return;
                }
                // TODO: Implement password change logic here
                user = userDAO.findByUsername(username);
                if (user == null) {
                    binding.edtUsername.setError("Username not found");
                    binding.edtUsername.requestFocus();
                    return;
                }
                String newPassword = UUID.randomUUID().toString().substring(0, 8);
                boolean isPasswordChanged = userService.changePassword(user.getId(), newPassword);
                // TODO: Send new password to user's phone number
                if(isPasswordChanged){
                    sendSMS(user.getPhoneNumber(), "Your new password is: " + newPassword);
                }
                dismiss();
                customDialogFragment = new CustomDialogFragment(activity, getString(R.string.reset_password_successfully), getString(R.string.Your_new_password_send_to_your_phone), "success", "Close", "-1", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialogFragment.dismiss();
                    }
                }, null);
                customDialogFragment.show(getActivity().getSupportFragmentManager(), "custom_dialog_fragment");
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}