package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewInvoiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceItemsAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityInvoiceDetailBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
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
    private ListViewServiceItemsAdapter listViewServiceItemsAdapter;
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
        setWidgets();
    }

    private void setWidgets() {
        customer = appTransactionService.getCustomerByServiceUsageId(selectedTransaction.getServiceUsageId());
        if(customer != null){
            binding.nameCustomer.setText(customer.getName());
            binding.phoneCustomer.setText(customer.getPhoneNumber());
            String membershipName = customerService.getMembershipNameByID(customer.getMemberShipId());
            binding.membership.setText(membershipName);
        }

        String fieldName = appTransactionService.getNameOfField(selectedTransaction.getId());
        binding.fieldName.setText(fieldName);

        Booking booking = appTransactionService.getBookingDetails(selectedTransaction.getId());
        binding.timeStart.setText(Utils.getTimeFromTimestamp(booking.getTimeStart()));
        binding.timeEnd.setText(Utils.getTimeFromTimestamp(booking.getTimeEnd()));
        binding.priceField.setText(Utils.formatVND(booking.getPrice()));

        serviceItemsList = serviceItemsService.findByServiceUsage(selectedTransaction.getServiceUsageId());
        if(serviceItemsList != null){
            listViewServiceItemsAdapter = new ListViewServiceItemsAdapter(getApplicationContext(), serviceItemsList);
            binding.listviewServiceItems.setAdapter(listViewServiceItemsAdapter);
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