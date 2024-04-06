package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.MainActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ServicePaymentActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentServiceBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;


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
    private List<Service> listServiceInCart;
    public static AppCompatButton buttonAdd;
    public static LinearLayout linearLayoutTittleCartService;

    private boolean hasMatch;
    private boolean openWithServiceItem;
    private Service serviceItem;
    private int quantity;
    private int positionSelected = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

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

        loadService(7, 0, "Active");
    }

    public void getData() {
        if (getArguments() != null) {
            hasMatch = getArguments().getBoolean("has_match");
            openWithServiceItem = getArguments().getBoolean("open_with_service_item");
            if (openWithServiceItem) {
                serviceItem = (Service) getArguments().getSerializable("service_item");
                quantity = getArguments().getInt("service_item_quantity");
            }
        }
    }

    public void setWidget() {
        buttonAdd = binding.buttonAdd;
        linearLayoutTittleCartService = binding.linearLayoutTittleCartService;
        listService = new ArrayList<>();

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
                    Intent intent = new Intent(getActivity(), ServicePaymentActivity.class);

                    intent.putExtra("hasMatch", hasMatch);
                    intent.putExtra("listServiceInCart", (ArrayList<Service>) listServiceInCart);
                    startActivityForResult(intent, GO_TO_PAYMENT);
                }
            }
        });
    }

    public void setListView() {
        listViewService = binding.listViewService;

        listViewServiceAdapter = new ListViewServiceAdapter(getContext(), listService);
        listViewService.setAdapter(listViewServiceAdapter);
        listViewService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSelected = position;

                BottomSheetServiceFragment bottomSheet = new BottomSheetServiceFragment(false, null, null);
                bottomSheet.setTargetFragment(ServiceFragment.this, GET_QUANTITY);
                bottomSheet.show(getParentFragmentManager(), "ServiceBottomSheetDialogFragment");
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

            Service serviceSelected = listService.get(positionSelected);
            serviceSelected.setOrderQuantity(data.getIntExtra("QUANTITY", 0));
            listServiceInCart.add(serviceSelected);
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

    public void loadService(int limit, int offset, String status) {
        ServiceServiceImpl serviceService = new ServiceServiceImpl(getContext());
        List<Service> listServiceLoad = new ArrayList<>();
        listServiceLoad = serviceService.getServicesWithLimitAndOffset(limit, offset, status);
        if (listServiceLoad.size() > 0) {
            listService.addAll(listServiceLoad);
            listViewServiceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}