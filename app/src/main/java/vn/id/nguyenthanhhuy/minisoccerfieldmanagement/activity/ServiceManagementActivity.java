package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
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
    public static int NUMBER_SERVICE_LOAD = 10;
    public String orderBy = "ORDER BY ";
    public String filed = "id";
    public String direction = " DESC";
    public String filter = orderBy + filed + direction;
    private Handler handler = new Handler();
    private Runnable runnable;
    public int currentFragmentIndex = 1;


    public static final int ADD_SERVICE = 1;
    public static final int ADD_SERVICE_SUCCESSFULLY = 2;
    public static final int EDIT_SERVICE = 3;
    public static final int EDIT_SERVICE_SUCCESSFULLY = 4;

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
        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });
        binding.autoCompleteTextViewSearch.setThreshold(1);
        binding.autoCompleteTextViewSearch.setDropDownBackgroundResource(R.drawable.background_white_radius_10dp);
        binding.buttonClear.setOnClickListener(v -> {
            binding.autoCompleteTextViewSearch.setText("");
        });

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

        binding.buttonFilter.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(ServiceManagementActivity.this, R.style.popup_menu_background_white_radius_10dp), v);
            popupMenu.getMenuInflater().inflate(R.menu.service_filter_menu, popupMenu.getMenu());

            if (direction.equals(" ASC")) {
                popupMenu.getMenu().findItem(R.id.menu_option_increase).setChecked(true);
            } else {
                if (direction.equals(" DESC")) {
                    popupMenu.getMenu().findItem(R.id.menu_option_decrease).setChecked(true);
                }
            }

            if (filed.equals("id")) {
                popupMenu.getMenu().findItem(R.id.menu_option_default).setChecked(true);
            } else {
                if (filed.equals("price")) {
                    popupMenu.getMenu().findItem(R.id.menu_option_price).setChecked(true);
                } else {
                    if (filed.equals("sold")) {
                        popupMenu.getMenu().findItem(R.id.menu_option_sold).setChecked(true);
                    } else {
                        if (filed.equals("quantity")) {
                            popupMenu.getMenu().findItem(R.id.menu_option_in_stock).setChecked(true);
                        }
                    }
                }
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_option_increase) {
                    item.setChecked(true);
                    direction = " ASC";
                } else {
                    if (item.getItemId() == R.id.menu_option_decrease) {
                        item.setChecked(true);
                        direction = " DESC";
                    } else {
                        if (item.getItemId() == R.id.menu_option_default) {
                            item.setChecked(true);
                            filed = "id";
                        } else {
                            if (item.getItemId() == R.id.menu_option_price) {
                                item.setChecked(true);
                                filed = "price";
                            } else {
                                if (item.getItemId() == R.id.menu_option_sold) {
                                    item.setChecked(true);
                                    filed = "sold";
                                } else {
                                    if (item.getItemId() == R.id.menu_option_in_stock) {
                                        item.setChecked(true);
                                        filed = "quantity";
                                    }
                                }
                            }
                        }
                    }
                }

                filter = orderBy + filed + direction;
                return true;
            });

            popupMenu.show();
        });

        binding.floatingActionButtonAddService.setOnClickListener(v -> {
            Intent intent = new Intent(ServiceManagementActivity.this, AddServiceActivity.class);
            intent.putExtra("option", "add");
            startActivityForResult(intent, ADD_SERVICE);
        });

        binding.autoCompleteTextViewSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    binding.buttonClear.setVisibility(View.GONE);
                    return;
                } else {
                    binding.buttonClear.setVisibility(View.VISIBLE);
                }

                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }

                List<String> results = findNameServiceByKeyword(s.toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ServiceManagementActivity.this, R.layout.layout_custom_auto_complete_dropdown, results);
                binding.autoCompleteTextViewSearch.setAdapter(adapter);

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (s.toString().length() > 0) {
                            binding.autoCompleteTextViewSearch.showDropDown();
                        }
                    }
                };
                handler.postDelayed(runnable, 500);
            }
        });

        binding.buttonSearch.setOnClickListener(v -> {
            if (currentFragmentIndex == 1) {
                switchFragment(new ListAllServiceFragment(binding.autoCompleteTextViewSearch.getText().toString(), ServiceManagementActivity.this));
            } else {
                if (currentFragmentIndex == 2) {
                    switchFragment(new ListActiveServiceFragment(binding.autoCompleteTextViewSearch.getText().toString(), ServiceManagementActivity.this));
                } else {
                    if (currentFragmentIndex == 3) {
                        switchFragment(new ListInactiveServiceFragment(binding.autoCompleteTextViewSearch.getText().toString(), ServiceManagementActivity.this));
                    } else {
                        switchFragment(new ListServiceDeletedFragment(binding.autoCompleteTextViewSearch.getText().toString(), ServiceManagementActivity.this));
                    }
                }
            }
        });
    }

    public List<String> findNameServiceByKeyword(String keyword) {
        List<String> listServiceName = null;
        try {
            ServiceServiceImpl service = new ServiceServiceImpl(ServiceManagementActivity.this);
            if (currentFragmentIndex == 1) {
                listServiceName = service.findServiceName(keyword, "", 0);
            } else if (currentFragmentIndex == 2) {
                listServiceName = service.findServiceName(keyword, "Active", 0);
            } else if (currentFragmentIndex == 3) {
                listServiceName = service.findServiceName(keyword, "Inactive", 0);
            } else {
                listServiceName = service.findServiceName(keyword, "", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listServiceName;
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

    public List<Service> getListServiceFromDatabase(int limit, int offset, String status, int isDeleted, String orderBy) {
        List<Service> listService = null;

        ServiceServiceImpl serviceService = new ServiceServiceImpl(ServiceManagementActivity.this);
        listService = serviceService.getServicesWithLimitAndOffset(limit, offset, status, isDeleted, orderBy);

        return listService;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_SERVICE_SUCCESSFULLY) {
            switchFragment(new ListAllServiceFragment());
        }
        if (resultCode == EDIT_SERVICE_SUCCESSFULLY) {
            switchFragment(new ListAllServiceFragment());
        }
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

    public void viewServiceDetails(String serviceId) {
        Intent intent = new Intent(ServiceManagementActivity.this, AddServiceActivity.class);
        intent.putExtra("option", "view");
        intent.putExtra("serviceId", serviceId);
        startActivity(intent);
    }

    public void editService(String serviceId) {
        Intent intent = new Intent(ServiceManagementActivity.this, AddServiceActivity.class);
        intent.putExtra("option", "edit");
        intent.putExtra("serviceId", serviceId);
        startActivityForResult(intent, EDIT_SERVICE);
    }

    public boolean deleteService(String serviceId) {
        try {
            ServiceServiceImpl serviceService = new ServiceServiceImpl(ServiceManagementActivity.this);
            serviceService.softDelete(serviceId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}