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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityServicePaymentBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.CustomDialogFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ServicePaymentActivity extends AppCompatActivity {

    private ActivityServicePaymentBinding binding;

    private boolean hasMatch;
    private boolean paymentSuccess = false;
    private ArrayList<Service> listServiceInCart;
    private final String paymentCode = CurrentTimeID.nextId("PM");
    private final Timestamp paymentTime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    private BigDecimal totalServicePrice = new BigDecimal(0);
    private BigDecimal totalFieldPrice = new BigDecimal(0);
    private BigDecimal additionalFee = new BigDecimal(0);
    private BigDecimal totalDiscount = new BigDecimal(0);
    private BigDecimal totalAmount = new BigDecimal(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServicePaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Lingver.getInstance().setLocale(ServicePaymentActivity.this, MainApplication.language);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));

        getData();
        setListServicesPayment();
        setWidget();
    }

    public void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hasMatch = extras.getBoolean("hasMatch");
            listServiceInCart = (ArrayList<Service>) extras.getSerializable("listServiceInCart");
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

        ((TextView) binding.textViewPaymentCode).setText(paymentCode);
        ((TextView) binding.textViewPaymentTime).setText(paymentTime.toString().substring(0, 19));
        ((TextView) binding.textViewTotalServicePrice).setText(Utils.formatVND(totalServicePrice));
        ((TextView) binding.textViewTotalFieldPrice).setText(Utils.formatVND(totalFieldPrice));
        ((TextView) binding.textViewAdditionalFee).setText(Utils.formatVND(additionalFee));
        ((TextView) binding.textViewDiscount).setText(Utils.formatVND(totalDiscount));
        ((TextView) binding.textViewTotalAmount).setText(Utils.formatVND(totalServicePrice.add(totalFieldPrice).add(additionalFee).subtract(totalDiscount)));
    }

    public void setListServicesPayment() {
        LinearLayout linearLayout = binding.linearLayoutServicesPayment;

        LayoutInflater inflater = getLayoutInflater();
        linearLayout.removeAllViews();

        for (Service service : listServiceInCart) {
            View itemView = inflater.inflate(R.layout.item_list_service_in_fragment_service, linearLayout, false);

            ((LinearLayout) itemView.findViewById(R.id.linear_layout_quantity)).setVisibility(View.VISIBLE);
            ((LinearLayout) itemView.findViewById(R.id.linear_layout_service_item)).setElevation(0);
            ((LinearLayout) itemView.findViewById(R.id.linear_layout_service_wrapper)).setBackgroundColor(Color.TRANSPARENT);

            ((TextView) itemView.findViewById(R.id.text_view_service_name)).setText(service.getName());
            ((TextView) itemView.findViewById(R.id.text_view_unit_of_service)).setText(service.getUnit());
            ((TextView) itemView.findViewById(R.id.text_view_in_stock)).setText(String.valueOf(service.getQuantity()));
            ((TextView) itemView.findViewById(R.id.text_view_price_of_service)).setText(Utils.formatVND(service.getPrice()));
            ((TextView) itemView.findViewById(R.id.text_view_quantity)).setText("" + service.getOrderQuantity());
            linearLayout.addView(itemView);

            totalServicePrice = totalServicePrice.add(service.getPrice().multiply(new BigDecimal(service.getOrderQuantity())));
        }
    }

    public void onButtonPaymentClick(View view) {
        paymentSuccess = true;
        CustomDialogFragment customDialogFragment = new CustomDialogFragment(ServicePaymentActivity.this, getResources().getString(R.string.payment_successfully), "", "success");
        customDialogFragment.show(getSupportFragmentManager(), "custom_dialog_notify");
    }
}