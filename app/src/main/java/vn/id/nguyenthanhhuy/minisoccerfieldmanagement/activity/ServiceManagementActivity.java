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
        switchFragment(new ListAllServiceFragment(), "see_all_service_fragment", binding.buttonSeeAll, binding.buttonActive, binding.buttonInactive);

        binding.buttonSeeAll.setOnClickListener(v -> {
            switchFragment(new ListAllServiceFragment(), "see_all_service_fragment", binding.buttonSeeAll, binding.buttonActive, binding.buttonInactive);
        });

        binding.buttonActive.setOnClickListener(v -> {
            switchFragment(new ListActiveServiceFragment(), "active_service_fragment", binding.buttonActive, binding.buttonInactive, binding.buttonSeeAll);
        });

        binding.buttonInactive.setOnClickListener(v -> {
            switchFragment(new ListInactiveServiceFragment(), "inactive_service_fragment", binding.buttonInactive, binding.buttonActive, binding.buttonSeeAll);
        });
    }

    public void switchFragment(Fragment fragment, String nameInBackStack, AppCompatButton buttonActive, AppCompatButton buttonInactive1, AppCompatButton buttonInactive2) {

        buttonActive.setBackgroundTintList(getResources().getColorStateList(R.color.primaryColor));
        buttonActive.setTextColor(getResources().getColor(R.color.white, getTheme()));

        buttonInactive1.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
        buttonInactive1.setTextColor(getResources().getColor(R.color.black, getTheme()));

        buttonInactive2.setBackgroundTintList(getResources().getColorStateList(R.color.whiteGray));
        buttonInactive2.setTextColor(getResources().getColor(R.color.black, getTheme()));

        getSupportFragmentManager().beginTransaction().replace(R.id.linear_layout_list_service, fragment).addToBackStack(nameInBackStack).commit();
    }
}