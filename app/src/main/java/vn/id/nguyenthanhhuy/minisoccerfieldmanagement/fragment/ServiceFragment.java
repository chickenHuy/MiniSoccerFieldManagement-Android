package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ServicePaymentActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentServiceBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceUsage;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IServiceItemsService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IServiceService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceItemsServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceUsageServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;


public class ServiceFragment extends Fragment {
    private FragmentServiceBinding binding;

    public static final int GET_QUANTITY = 0;
    public static final int GET_QUANTITY_SUCCESSFULLY = 1;
    public static final int GO_TO_PAYMENT = 2;
    public static final int PAYMENT_SUCCESSFULLY = 3;

    private ListView listViewService;
    private ListViewServiceAdapter listViewServiceAdapter;

    private RecyclerView recyclerViewCartService;
    private RecyclerViewServiceAdapter recyclerViewServiceAdapter;
    private List<Service> listService;
    private boolean isSearching = false;
    private int countResultSearch = 0;
    private int countAllService = 0;
    private List<Service> listServiceSearch;
    private List<Service> listServiceInCart;
    public static AppCompatButton buttonAdd;
    public static LinearLayout linearLayoutTittleCartService;

    private boolean hasMatch;
    private boolean openWithServiceItem;
    private Service serviceItem;
    private int positionSelected = -1;
    private boolean isLoading = false;
    private ExecutorService executorService;
    private ExecutorService executorServiceSearch;

    private Handler handler = new Handler();
    private Runnable runnable;
    private String serviceUsageId;
    private IServiceItemsService serviceItemsService;
    private IServiceService serviceService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentServiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();
        setWidget();
        setListView();
        setRecyclerViewCartService();

        loadService(10, 0, "Active", 0, "ORDER BY id DESC");
    }

    public void getData() {
        if (getArguments() != null) {
            hasMatch = getArguments().getBoolean("has_match");
            openWithServiceItem = getArguments().getBoolean("open_with_service_item");
            if (openWithServiceItem) {
                serviceItem = (Service) getArguments().getSerializable("service_item");
            }
            if (hasMatch) {
                serviceUsageId = getArguments().getString("serviceUsageId");
            }
        }
    }

    public void setWidget() {
        countAllService = new ServiceServiceImpl(getContext()).countServices("Active", 0);
        serviceItemsService = new ServiceItemsServiceImpl(getContext());
        serviceService = new ServiceServiceImpl(getContext());
        buttonAdd = binding.buttonAdd;
        linearLayoutTittleCartService = binding.linearLayoutTittleCartService;
        listService = new ArrayList<>();
        listServiceSearch = new ArrayList<>();

        binding.buttonClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.autoCompleteTextViewSearch.setText("");
            }
        });

        ((AppCompatButton) binding.buttonClearCartService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listServiceInCart.clear();
                recyclerViewServiceAdapter.notifyDataSetChanged();

                setCartServiceTittle(View.GONE);
            }
        });

        if (!hasMatch) {
            binding.buttonAdd.setText(getResources().getString(R.string.payment));
        }
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasMatch) {
                    List<ServiceItems> listServiceItemInCart = new ArrayList<>();
                    ServiceUsage serviceUsage = new ServiceUsage();
                    serviceUsage.setId(CurrentTimeID.nextId("SU"));

                    for (Service service : listServiceInCart) {
                        ServiceItems serviceItems = new ServiceItems();

                        serviceItems.setId(CurrentTimeID.nextId("SI"));
                        serviceItems.setServiceUsageId(serviceUsage.getId());
                        serviceItems.setServiceId(service.getId());
                        serviceItems.setQuantity(service.getOrderQuantity());

                        listServiceItemInCart.add(serviceItems);
                    }

                    Intent intent = new Intent(getActivity(), ServicePaymentActivity.class);
                    intent.putExtra("hasMatch", hasMatch);
                    intent.putExtra("serviceUsage", serviceUsage);
                    intent.putExtra("listServiceItemInCart", (ArrayList<ServiceItems>) listServiceItemInCart);
                    startActivityForResult(intent, GO_TO_PAYMENT);
                }
                else {

                    for (Service service : listServiceInCart) {
                        ServiceItems serviceItems = new ServiceItems();

                        serviceItems.setId(CurrentTimeID.nextId("SI"));
                        serviceItems.setServiceUsageId(serviceUsageId);
                        serviceItems.setServiceId(service.getId());
                        serviceItems.setQuantity(service.getOrderQuantity());

                        try {
                            serviceService.updateQuantity(serviceItems.getServiceId(), service.getQuantity() - serviceItems.getQuantity());
                            serviceService.updateSold(serviceItems.getServiceId(), service.getSold() + serviceItems.getQuantity());
                            mergerService(serviceItems);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    getActivity().onBackPressed();
                }
            }
        });

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearching = true;
                listServiceSearch.clear();
                listServiceSearch = new ServiceServiceImpl(getContext()).findServiceByKeyword(binding.autoCompleteTextViewSearch.getText().toString(), 10, 0, "Active", 0);
                countResultSearch = new ServiceServiceImpl(getContext()).countServicesSearch(binding.autoCompleteTextViewSearch.getText().toString(), "Active", 0);

                listViewServiceAdapter = new ListViewServiceAdapter(getContext(), listServiceSearch, false);
                listViewService.setAdapter(listViewServiceAdapter);
            }
        });

        binding.autoCompleteTextViewSearch.setThreshold(1);
        binding.autoCompleteTextViewSearch.setDropDownBackgroundResource(R.drawable.background_white_radius_10dp);
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
                    binding.buttonClean.setVisibility(View.GONE);
                    return;
                } else {
                    binding.buttonClean.setVisibility(View.VISIBLE);
                }

                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }

                List<String> results = findNameServiceByKeyword(s.toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.layout_custom_auto_complete_dropdown, results);
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

    }

    private void mergerService(ServiceItems serviceNew) {
        List<ServiceItems> serviceItems = serviceItemsService.findByServiceUsage(serviceUsageId);
        for (ServiceItems item : serviceItems) {
            if (item.getServiceId().equals(serviceNew.getServiceId())) {
                item.setQuantity(item.getQuantity() + serviceNew.getQuantity());
                serviceItemsService.update(item);
                return;
            }
        }
        serviceItemsService.add(serviceNew);
    }

    public void setListView() {

        listViewService = binding.listViewService;

        listViewServiceAdapter = new ListViewServiceAdapter(getContext(), listService, false);
        listViewService.setAdapter(listViewServiceAdapter);
        listViewService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSelected = position;
                Service serviceSelected = null;
                if (isSearching) {
                    serviceSelected = listServiceSearch.get(positionSelected);
                } else {
                    serviceSelected = listService.get(positionSelected);
                }

                BottomSheetServiceFragment bottomSheet = new BottomSheetServiceFragment(false, null, serviceSelected);
                bottomSheet.setTargetFragment(ServiceFragment.this, GET_QUANTITY);
                bottomSheet.show(getParentFragmentManager(), "ServiceBottomSheetDialogFragment");
            }
        });

        listViewService.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (isSearching) {
                        if (listServiceSearch.size() >= countResultSearch) {
                            return;
                        }
                        loadServiceSearch(10, listServiceSearch.size(), "Active", 0);
                    } else {
                        if (listService.size() >= new ServiceServiceImpl(getContext()).countServices("Active", 0)) {
                            return;
                        }
                        loadService(10, listService.size(), "Active", 0, "ORDER BY id DESC");
                    }
                }
            }
        });
    }

    public void setCartServiceTittle(int status) {
        linearLayoutTittleCartService.setVisibility(status);
        buttonAdd.setVisibility(status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GET_QUANTITY_SUCCESSFULLY && data != null) {

            Service serviceSelected = new Service();
            if (isSearching) {
                serviceSelected = listServiceSearch.get(positionSelected).clone();
            } else {
                serviceSelected = listService.get(positionSelected).clone();
            }
            serviceSelected.setOrderQuantity(data.getIntExtra("QUANTITY", 0));

            boolean isExist = false;
            for (Service serviceInCart : listServiceInCart) {
                Log.i("CHECK", "onActivityResult: " + serviceInCart.getOrderQuantity());
                if (serviceInCart.getId().equals(serviceSelected.getId())) {
                    int quantity = serviceInCart.getOrderQuantity() + serviceSelected.getOrderQuantity();
                    if (quantity > serviceInCart.getQuantity()) {
                        Toast.makeText(getContext(), getResources().getString(R.string.failed) + ": " + getResources().getString(R.string.in_stock) + " " + serviceInCart.getQuantity(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    serviceInCart.setOrderQuantity(quantity);
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                listServiceInCart.add(serviceSelected);
            }

            positionSelected = -1;

            if (listServiceInCart.size() == 1) {
                setCartServiceTittle(View.VISIBLE);
            }

            recyclerViewServiceAdapter.notifyDataSetChanged();

            FragmentManager fragmentManager = getParentFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("ServiceBottomSheetDialogFragment");
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
            return;
        }
        if (resultCode == PAYMENT_SUCCESSFULLY && data != null) {
            listServiceInCart.clear();
            recyclerViewServiceAdapter.notifyDataSetChanged();
            setCartServiceTittle(View.GONE);
            return;
        }
    }

    public void setRecyclerViewCartService() {
        recyclerViewCartService = binding.recyclerViewCartService;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCartService.setLayoutManager(layoutManager);
        recyclerViewCartService.addItemDecoration(new RecyclerViewServiceAdapter.StartEndSpaceItemDecoration(55, 20, 55));

        listServiceInCart = new ArrayList<>();

        if (openWithServiceItem) {
            listServiceInCart.add(serviceItem);
            buttonAdd.setVisibility(View.VISIBLE);
            linearLayoutTittleCartService.setVisibility(View.VISIBLE);
        }
        recyclerViewServiceAdapter = new RecyclerViewServiceAdapter(this, getContext(), listServiceInCart, true, getParentFragmentManager());
        recyclerViewCartService.setAdapter(recyclerViewServiceAdapter);
    }

    public void loadService(int limit, int offset, String status, int isDeleted, String orderBy) {
        if (isLoading) {
            return;
        }

        ServiceServiceImpl service = new ServiceServiceImpl(getContext());
        isLoading = true;
        executorService = Executors.newSingleThreadExecutor();
        binding.progressBar.setVisibility(View.VISIBLE);

        executorService.execute(new Runnable() {

            @Override
            public void run() {
                List<Service> listServiceLoad = service.getServicesWithLimitAndOffset(limit, offset, status, isDeleted, orderBy);
                getActivity().

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listServiceLoad.size() > 0) {
                                    listService.addAll(listServiceLoad);
                                }
                                binding.progressBar.setVisibility(View.GONE);
                                listViewServiceAdapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        });
            }
        });
    }

    public void loadServiceSearch(int limit, int offset, String status, int isDeleted) {
        if (isLoading) {
            return;
        }

        isLoading = true;
        executorServiceSearch = Executors.newSingleThreadExecutor();
        binding.progressBar.setVisibility(View.VISIBLE);

        executorServiceSearch.execute(new Runnable() {

            @Override
            public void run() {
                List<Service> listServiceLoad = new ServiceServiceImpl(getContext()).findServiceByKeyword(binding.autoCompleteTextViewSearch.getText().toString().trim(), limit, offset, status, isDeleted);
                getActivity().
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("CHECK", "run: " + listServiceLoad.size());
                                if (listServiceLoad.size() > 0) {
                                    listServiceSearch.addAll(listServiceLoad);
                                }
                                binding.progressBar.setVisibility(View.GONE);
                                listViewServiceAdapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        });
            }
        });
    }

    public List<String> findNameServiceByKeyword(String keyword) {
        List<String> listServiceName = null;
        try {
            ServiceServiceImpl service = new ServiceServiceImpl(getContext());
            listServiceName = service.findServiceName(keyword, "Active", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listServiceName;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}