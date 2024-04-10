package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.MainActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.LayoutCustomDialogNotifyBinding;

public class CustomDialogFragment extends DialogFragment {
    private LayoutCustomDialogNotifyBinding binding;

    private Activity activity;
    private String message;
    private String details;
    private String status;
    private String actionMessage1 = "";
    private String actionMessage2 = "";
    private View.OnClickListener buttonAction1OnclickListener = null;
    private View.OnClickListener buttonAction2OnclickListener = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutCustomDialogNotifyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textViewNotifyMessage.setText(message);
        binding.textViewDetailNotifyMessage.setText(details);
        binding.buttonAction1.setText(actionMessage1);
        binding.buttonAction2.setText(actionMessage2);

        if (status.equals("success")) {
            binding.imageViewIconNotify.setImageResource(R.drawable.image_success);
        } else if (status.equals("error")) {
            binding.imageViewIconNotify.setImageResource(R.drawable.image_error);
        } else {
            if (status.equals("warning")) {
                binding.imageViewIconNotify.setImageResource(R.drawable.image_warning);
            }
        }

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fall_down);
        binding.imageViewIconNotify.startAnimation(animation);

        if (actionMessage1.equals("")) {
            binding.buttonAction1.setText(getResources().getString((R.string.home_page)));
        } else {
            binding.buttonAction1.setText(actionMessage1);
        }
        if (actionMessage2.equals("")) {
            binding.buttonAction2.setText(getResources().getString((R.string.service_page)));
        } else {
            binding.buttonAction2.setText(actionMessage2);
        }

        if (buttonAction1OnclickListener == null) {
            binding.buttonAction1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            binding.buttonAction1.setOnClickListener(buttonAction1OnclickListener);
        }
        if (buttonAction2OnclickListener == null) {
            binding.buttonAction2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("paymentSuccess", status.equals("success") ? true : false);
                    activity.setResult(ServiceFragment.PAYMENT_SUCCESSFULLY, resultIntent);
                    activity.finish();
                }
            });
        } else {
            binding.buttonAction2.setOnClickListener(buttonAction2OnclickListener);
        }
    }

    public CustomDialogFragment(Activity activity, String message, String details, String status) {
        this.activity = activity;
        this.message = message;
        this.details = details;
        this.status = status;
    }

    public CustomDialogFragment(Activity activity, String message, String details, String status, String actionMessage1, String actionMessage2, View.OnClickListener buttonAction1OnclickListener, View.OnClickListener buttonAction2OnclickListener) {
        this.activity = activity;
        this.message = message;
        this.details = details;
        this.status = status;
        this.actionMessage1 = actionMessage1;
        this.actionMessage2 = actionMessage2;
        this.buttonAction1OnclickListener = buttonAction1OnclickListener;
        this.buttonAction2OnclickListener = buttonAction2OnclickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.rounded_corners_dialog) {
            @Override
            public void onBackPressed() {
                dismiss();
            }
        };
    }
}
