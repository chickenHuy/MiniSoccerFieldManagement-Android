package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityInvoiceDetailBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.AppTransactionServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceItemsServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class InvoiceDetailActivity extends AppCompatActivity {
    private ActivityInvoiceDetailBinding binding;
    private AppTransactionServiceImpl appTransactionService;
    private CustomerServiceImpl customerService;
    private ServiceItemsServiceImpl serviceItemsService;
    private List<ServiceItems> serviceItemsList;
    private Customer customer;
    private AppTransaction selectedTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appTransactionService = new AppTransactionServiceImpl(this);
        customerService = new CustomerServiceImpl(this);
        serviceItemsService = new ServiceItemsServiceImpl(this);

        selectedTransaction = (AppTransaction) getIntent().getSerializableExtra("selectedTransaction");
        setListServiceItems();
        setListFieldUsage();
        setWidgets();
    }

    private void setListFieldUsage() {
        LinearLayout linearLayout = binding.linearLayoutField;
        LayoutInflater inflater = getLayoutInflater();
        linearLayout.removeAllViews();

        View itemView = inflater.inflate(R.layout.item_list_field_usage, linearLayout, false);

        String fieldName = appTransactionService.getNameOfField(selectedTransaction.getId());
        Booking booking = appTransactionService.getBookingDetails(selectedTransaction.getId());

        if(fieldName != null && booking != null) {
            ((TextView) itemView.findViewById(R.id.field_name)).setText(fieldName);
            ((TextView) itemView.findViewById(R.id.time_start)).setText(Utils.getTimeFromTimestamp(booking.getTimeStart()));
            ((TextView) itemView.findViewById(R.id.time_end)).setText(Utils.getTimeFromTimestamp(booking.getTimeEnd()));
            ((TextView) itemView.findViewById(R.id.price_field)).setText(Utils.formatVND(booking.getPrice()));
            linearLayout.addView(itemView);
        }
    }

    private void setListServiceItems() {
        LinearLayout linearLayout = binding.linearLayoutServiceItems;
        LayoutInflater inflater = getLayoutInflater();
        linearLayout.removeAllViews();

        serviceItemsList = serviceItemsService.findByServiceUsage(selectedTransaction.getServiceUsageId());

        if(serviceItemsList != null){
            for (ServiceItems serviceItem : serviceItemsList) {
                View itemView = inflater.inflate(R.layout.item_list_service_items, linearLayout, false);

                Service service = serviceItemsService.getServiceInfo(serviceItem.getServiceId());

                TextView tvServiceName = itemView.findViewById(R.id.service_name);
                TextView tvQuantity = itemView.findViewById(R.id.quantity);
                TextView tvPrice = itemView.findViewById(R.id.price_item);
                int quantity = serviceItem.getQuantity();
                //BigDecimal price = service.getPrice().multiply(BigDecimal.valueOf(quantity));

                tvServiceName.setText(service.getName());
                tvQuantity.setText(String.valueOf(quantity));
                tvPrice.setText(Utils.formatVND(service.getPrice()));

                linearLayout.addView(itemView);
            }
        }
    }

    private void setWidgets() {
        customer = appTransactionService.getCustomerByServiceUsageId(selectedTransaction.getServiceUsageId());
        if(customer != null){
            binding.nameCustomer.setText(customer.getName());
            binding.phoneCustomer.setText(customer.getPhoneNumber());
            String membershipName = customerService.getMembershipNameByID(customer.getMemberShipId());
            binding.membership.setText(membershipName);
        }

        binding.textViewPaymentCode.setText(selectedTransaction.getId());
        binding.textViewPaymentTime.setText(Utils.getDateFromTimestamp(selectedTransaction.getCreatedAt()));
        binding.textViewTotalAmount.setText(Utils.formatVND(selectedTransaction.getTotalAmount()));
        binding.textViewDiscountAmount.setText(Utils.formatVND(selectedTransaction.getAdditionalFee()));
        binding.textViewAdditionalFee.setText(Utils.formatVND(selectedTransaction.getDiscountAmount()));
        binding.textViewFinalAmount.setText(Utils.formatVND(selectedTransaction.getFinalAmount()));

        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });
    }
}