package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.yariksoffice.lingver.Lingver;

import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityServiceManagementBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ListActiveServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ListAllServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ListInactiveServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ListServiceDeletedFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;

public class ServiceManagementActivity extends AppCompatActivity {
    private ActivityServiceManagementBinding binding;
    private ListView listViewListService;
    private ListViewServiceAdapter listViewListServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Lingver.getInstance().setLocale(ServiceManagementActivity.this, MainApplication.language);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColor, getTheme()));

        setWidget();
    }

    public void setWidget() {
        switchFragment(new ListAllServiceFragment());

        binding.buttonSeeAll.setOnClickListener(v -> {
            switchFragment(new ListAllServiceFragment());
        });

        binding.buttonActive.setOnClickListener(v -> {
            switchFragment(new ListActiveServiceFragment());
        });

        binding.buttonInactive.setOnClickListener(v -> {
            switchFragment(new ListInactiveServiceFragment());
        });

        binding.buttonDeleted.setOnClickListener(v -> {
            switchFragment(new ListServiceDeletedFragment());
        });
    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.linear_layout_list_service, fragment).commit();
    }

    public void switchButtonSelected(int index) {
        if (index == 1) {
            switchButton(binding.buttonSeeAll, binding.buttonActive, binding.buttonInactive, binding.buttonDeleted);
        } else if (index == 2) {
            switchButton(binding.buttonActive, binding.buttonInactive, binding.buttonSeeAll, binding.buttonDeleted);
        } else if (index == 3) {
            switchButton(binding.buttonInactive, binding.buttonActive, binding.buttonSeeAll, binding.buttonDeleted);
        } else {
            switchButton(binding.buttonDeleted, binding.buttonActive, binding.buttonSeeAll, binding.buttonInactive);
        }
    }

    public List<Service> getListServiceFromDatabase(int limit, int offset, String status, int isDeleted) {
        List<Service> listService = null;

        ServiceServiceImpl serviceService = new ServiceServiceImpl(ServiceManagementActivity.this);
        listService = serviceService.getServicesWithLimitAndOffset(limit, offset, status, isDeleted);

        return listService;
    }

    public void switchButton(AppCompatButton buttonActive, AppCompatButton buttonInactive1, AppCompatButton buttonInactive2, AppCompatButton buttonInactive3) {

        buttonActive.setBackgroundTintList(getResources().getColorStateList(R.color.primaryColor));
        buttonActive.setTextColor(getResources().getColor(R.color.white, getTheme()));

        buttonInactive1.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
        buttonInactive1.setTextColor(getResources().getColor(R.color.black, getTheme()));

        buttonInactive2.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
        buttonInactive2.setTextColor(getResources().getColor(R.color.black, getTheme()));

        buttonInactive3.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
        buttonInactive3.setTextColor(getResources().getColor(R.color.black, getTheme()));
    }
}