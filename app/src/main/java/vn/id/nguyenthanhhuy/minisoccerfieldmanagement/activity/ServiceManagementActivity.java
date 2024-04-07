package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.yariksoffice.lingver.Lingver;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityServiceManagementBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ListActiveServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ListAllServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ListInactiveServiceFragment;

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
        switchFragment(new ListAllServiceFragment(), "see_all_service_fragment");

        binding.buttonSeeAll.setOnClickListener(v -> {
            switchFragment(new ListAllServiceFragment(), "see_all_service_fragment");
        });

        binding.buttonActive.setOnClickListener(v -> {
            switchFragment(new ListActiveServiceFragment(), "active_service_fragment");
        });

        binding.buttonInactive.setOnClickListener(v -> {
            switchFragment(new ListInactiveServiceFragment(), "inactive_service_fragment");
        });
    }

    public void switchFragment(Fragment fragment, String nameInBackStack) {
        getSupportFragmentManager().beginTransaction().replace(R.id.linear_layout_list_service, fragment).addToBackStack(nameInBackStack).commit();
    }

    public void switchButtonSelected(int index) {
        if (index == 1) {
            switchButton(binding.buttonSeeAll, binding.buttonActive, binding.buttonInactive);
        } else if (index == 2) {
            switchButton(binding.buttonActive, binding.buttonInactive, binding.buttonSeeAll);
        } else {
            switchButton(binding.buttonInactive, binding.buttonActive, binding.buttonSeeAll);
        }
    }

    public void switchButton(AppCompatButton buttonActive, AppCompatButton buttonInactive1, AppCompatButton buttonInactive2) {

        buttonActive.setBackgroundTintList(getResources().getColorStateList(R.color.primaryColor));
        buttonActive.setTextColor(getResources().getColor(R.color.white, getTheme()));

        buttonInactive1.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
        buttonInactive1.setTextColor(getResources().getColor(R.color.black, getTheme()));

        buttonInactive2.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
        buttonInactive2.setTextColor(getResources().getColor(R.color.black, getTheme()));
    }
}