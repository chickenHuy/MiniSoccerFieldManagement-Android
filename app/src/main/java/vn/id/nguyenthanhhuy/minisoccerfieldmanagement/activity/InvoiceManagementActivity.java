package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private List<AppTransaction> appTransactionsFromDB;
    private ActivityInvoiceManagementBinding binding;
    private ListViewInvoiceAdapter listViewInvoiceAdapter;
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

        appTransactionsFromDB = appTransactionService.findAll();
        appTransactionList = new ArrayList<>();
        if (appTransactionsFromDB != null) {
            appTransactionList.addAll(appTransactionsFromDB);
        }

        listViewInvoiceAdapter = new ListViewInvoiceAdapter(getApplicationContext(), appTransactionList);
        binding.listViewInvoice.setAdapter(listViewInvoiceAdapter);

        binding.listViewInvoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppTransaction selectedTransaction = appTransactionList.get(position);
                Intent intent = new Intent(InvoiceManagementActivity.this, InvoiceDetailActivity.class);
                intent.putExtra("selectedTransaction", selectedTransaction);
                startActivity(intent);
            }
        });

        binding.buttonClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewSearch.setText("");
                appTransactionList.clear();
                listViewInvoiceAdapter.notifyDataSetChanged();
            }
        });

        binding.buttonByday.setOnClickListener(v -> {
            binding.textViewSearch.setText("");
            showDatePickerDialog();
        });

        binding.buttonSeeAll.setOnClickListener(v -> {
            binding.textViewSearch.setText("");
            appTransactionList.clear();
            if (appTransactionsFromDB != null) {
                appTransactionList.addAll(appTransactionsFromDB);
            }
            listViewInvoiceAdapter.notifyDataSetChanged();
            updateButtonColors(binding.buttonByday, false);
            updateButtonColors(binding.buttonSeeAll, true);
        });

        binding.textViewSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    binding.buttonClean.setVisibility(View.GONE);
                    appTransactionList.clear();
                    listViewInvoiceAdapter.notifyDataSetChanged();
                    return;
                } else {
                    binding.buttonClean.setVisibility(View.VISIBLE);
                }

                String searchText = s.toString();
                List<AppTransaction> listSearch = appTransactionService.searchList(searchText);
                appTransactionList.clear();
                if (listSearch != null) {
                    appTransactionList.addAll(listSearch);
                }
                listViewInvoiceAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                Date selectedDate = selectedCalendar.getTime();
                updateListViewByDay(selectedDate);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void updateListViewByDay(Date selectedDate) {
        List<AppTransaction> filteredTransactions = new ArrayList<>();
        for (AppTransaction transaction : appTransactionsFromDB) {
            if (isSameDay(transaction.getCreatedAt(), selectedDate)) {
                filteredTransactions.add(transaction);
            }
        }
        appTransactionList.clear();
        appTransactionList.addAll(filteredTransactions);
        listViewInvoiceAdapter.notifyDataSetChanged();

        updateButtonColors(binding.buttonByday, true);
        updateButtonColors(binding.buttonSeeAll, false);
    }

    private void updateButtonColors(AppCompatButton button, boolean pressed) {
        if (pressed) {
            button.setBackgroundTintList(getResources().getColorStateList(R.color.primaryColor));
            button.setTextColor(getResources().getColor(R.color.white, getTheme()));
        } else {
            button.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
            button.setTextColor(getResources().getColor(R.color.black, getTheme()));
        }
    }

    public boolean isSameDay(Timestamp createdAt, Date selectedDate) {
        java.sql.Date date1 = new java.sql.Date(createdAt.getTime());

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(selectedDate);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}
