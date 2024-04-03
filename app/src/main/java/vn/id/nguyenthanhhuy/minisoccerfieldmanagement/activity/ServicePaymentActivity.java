package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yariksoffice.lingver.Lingver;

import java.util.ArrayList;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityServicePaymentBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ServiceFragment;

public class ServicePaymentActivity extends AppCompatActivity {

    private ActivityServicePaymentBinding binding;

    private boolean hasMatch;
    private boolean paymentSuccess = false;
    private ArrayList<String> listServiceInCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServicePaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Lingver.getInstance().setLocale(ServicePaymentActivity.this, MainApplication.language);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));

        getData();
        setWidget();
        setListServicesPayment();
    }

    public void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hasMatch = extras.getBoolean("hasMatch");
            listServiceInCart = extras.getStringArrayList("listServiceInCart");
        }
    }

    public void setWidget() {
        ((AppCompatButton) binding.buttonBack).setOnClickListener(v -> {
            if (paymentSuccess) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("paymentSuccess", paymentSuccess);
                setResult(ServiceFragment.PAYMENT_SUCCESSFULLY, resultIntent);
                finish();
            } else {
                finish();
            }
        });

        if (hasMatch) {
            ((LinearLayout) binding.linearLayoutCustomerInformation).setVisibility(View.VISIBLE);
        } else {
            ((AppCompatButton) binding.buttonAddCustomer).setVisibility(View.VISIBLE);
        }
    }

    public void setListServicesPayment() {
        LinearLayout linearLayout = binding.linearLayoutServicesPayment;

        LayoutInflater inflater = getLayoutInflater();
        linearLayout.removeAllViews();

        for (String service : listServiceInCart) {
            View itemView = inflater.inflate(R.layout.item_list_service_in_fragment_service, linearLayout, false);

            ((LinearLayout) itemView.findViewById(R.id.linear_layout_quantity)).setVisibility(View.VISIBLE);
            ((LinearLayout) itemView.findViewById(R.id.linear_layout_service_item)).setElevation(0);
            ((LinearLayout) itemView.findViewById(R.id.linear_layout_service_wrapper)).setBackgroundColor(Color.TRANSPARENT);
            linearLayout.addView(itemView);
        }
    }

    public void onButtonPaymentClick(View view) {
        paymentSuccess = false;

        Dialog dialog = new Dialog(this, R.style.rounded_corners_dialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_custom_dialog_payment_notify, null);

        ((AppCompatButton) dialogView.findViewById(R.id.button_home_page)).setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(ServicePaymentActivity.this, MainActivity.class);
            startActivity(intent);
        });

        ((AppCompatButton) dialogView.findViewById(R.id.button_service_page)).setOnClickListener(v -> {
            dialog.dismiss();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("paymentSuccess", paymentSuccess);
            setResult(ServiceFragment.PAYMENT_SUCCESSFULLY, resultIntent);
            finish();
        });

        ImageView imageViewIconNotify = dialogView.findViewById(R.id.image_view_icon_notify);
        TextView textViewNotifyMessage = dialogView.findViewById(R.id.text_view_notify_message);
        if (paymentSuccess) {
            imageViewIconNotify.setImageResource(R.drawable.image_success);
            textViewNotifyMessage.setText(R.string.payment_successfully);
        } else {
            imageViewIconNotify.setImageResource(R.drawable.image_error);
            textViewNotifyMessage.setText(R.string.payment_error);
            TextView textViewDetailNotifyMessage = dialogView.findViewById(R.id.text_view_detail_notify_message);
            textViewDetailNotifyMessage.setVisibility(View.VISIBLE);
            textViewDetailNotifyMessage.setText("Số lượng sản phẫm không đủ!!!");
        }

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fall_down);
        imageViewIconNotify.startAnimation(animation);

        dialog.setContentView(dialogView);
        dialog.show();

    }
}