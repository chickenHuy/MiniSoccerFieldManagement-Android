package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewCustomerAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewInvoiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewEmployeeAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityEmployeeManagementBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityInvoiceManagementBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentCustomerBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BottomSheetEmployeeFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.AppTransactionServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class InvoiceManagementActivity extends AppCompatActivity {
    private List<AppTransaction> appTransactionList;
    private ActivityInvoiceManagementBinding binding;
    private ListViewInvoiceAdapter listViewInvoiceAdapter;
    private ListView listViewInvoice;
    private AppTransactionServiceImpl appTransactionService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appTransactionService = new AppTransactionServiceImpl(this);
        setWidgets();
        Utils.setStatusBarColor(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void setWidgets() {
        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });

        AppTransaction transaction = new AppTransaction();

        // Thiết lập các thuộc tính cho đối tượng
        transaction.setId("1");
        transaction.setUserID("Trương Nguyễn Bảo Nguyên");
        transaction.setServiceUsageId("serviceUsage123");
        transaction.setType("Type A");
        transaction.setTotalAmount(new BigDecimal("100.00"));
        transaction.setAdditionalFee(new BigDecimal("10.00"));
        transaction.setDiscountAmount(new BigDecimal("5.00"));
        transaction.setFinalAmount(new BigDecimal("105000"));
        transaction.setDeleted(false);
        transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        transaction.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        AppTransaction transaction2 = new AppTransaction();

        // Thiết lập các thuộc tính cho đối tượng
        transaction2.setId("1");
        transaction2.setUserID("Lê Quang Lâm");
        transaction2.setServiceUsageId("serviceUsage123");
        transaction2.setType("Type A");
        transaction2.setTotalAmount(new BigDecimal("100.00"));
        transaction2.setAdditionalFee(new BigDecimal("10.00"));
        transaction2.setDiscountAmount(new BigDecimal("5.00"));
        transaction2.setFinalAmount(new BigDecimal("105000"));
        transaction2.setDeleted(false);
        transaction2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        transaction2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        List<AppTransaction> appTransactionsFromDB = appTransactionService.findAll();
        if (appTransactionsFromDB == null) {
            appTransactionList = new ArrayList<>();
        } else {
            appTransactionList = appTransactionsFromDB;
        }
        appTransactionList.add(transaction);
        appTransactionList.add(transaction2);
        listViewInvoiceAdapter = new ListViewInvoiceAdapter(getApplicationContext(), appTransactionList);
        binding.listViewInvoice.setAdapter(listViewInvoiceAdapter);

        binding.listViewInvoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InvoiceManagementActivity.this, InvoiceDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}