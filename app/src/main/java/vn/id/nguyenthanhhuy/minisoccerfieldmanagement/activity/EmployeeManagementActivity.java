package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ListView;

import com.yariksoffice.lingver.Lingver;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewEmployeeAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityEmployeeManagementBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BottomSheetEmployeeFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class EmployeeManagementActivity extends AppCompatActivity {

    List<User> employeeList;
    RecyclerViewEmployeeAdapter recyclerViewEmployeeAdapter;
    ListView lvEmployees;
    ActivityEmployeeManagementBinding binding;
    UserServiceImpl userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userService = new UserServiceImpl(this);
        setWidgets();

        Utils.setStatusBarColor(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setWidgets() {

        binding.textViewSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 // Code here will be triggered before the text changed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code here will be triggered when the text is changing
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Code here will be triggered after the text changed
                // You can put your search logic here
                String searchText = s.toString();
                // Call your search function with searchText
                List<User> employeesFromDb = userService.findUser(searchText);
                if (employeesFromDb == null) {
                    employeeList = new ArrayList<>();
                } else {
                    employeeList = employeesFromDb;
                }
                if (recyclerViewEmployeeAdapter == null) {
                    recyclerViewEmployeeAdapter = new RecyclerViewEmployeeAdapter(EmployeeManagementActivity.this, employeeList, EmployeeManagementActivity.this::onEmployeeClick, getLayoutInflater());
                    binding.recyclerViewEmployee.setAdapter(recyclerViewEmployeeAdapter);
                } else {
                    recyclerViewEmployeeAdapter.updateData(employeeList);
                }
            }
        });

        binding.floatingActionButtonAddService.setOnClickListener(v -> {
            // Chuyển activity để thêm Employee mới
            Intent intent = new Intent(EmployeeManagementActivity.this, EditProfileActivity.class);
            intent.putExtra("option", "addEmployee");
            startActivity(intent);
        });

        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });

        List<User> employeesFromDb = userService.findAll();
        if (employeesFromDb == null) {
            employeeList = new ArrayList<>();
        } else {
            employeeList = employeesFromDb;
        }

        recyclerViewEmployeeAdapter = new RecyclerViewEmployeeAdapter(this, employeeList, this::onEmployeeClick, getLayoutInflater());
        binding.recyclerViewEmployee.setAdapter(recyclerViewEmployeeAdapter);
        binding.recyclerViewEmployee.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onEmployeeClick(User employee) {
        // Hiển thị dialog chỉnh sửa thông tin Employee
         BottomSheetEmployeeFragment bottomSheetEmployeeFragment = new BottomSheetEmployeeFragment().newInstance(employee);
         bottomSheetEmployeeFragment.setOnDismissListener(() -> {
             setWidgets();
         });
         bottomSheetEmployeeFragment.show(getSupportFragmentManager(), bottomSheetEmployeeFragment.getTag());
    }
}