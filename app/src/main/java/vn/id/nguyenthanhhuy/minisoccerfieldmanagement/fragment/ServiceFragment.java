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

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentServiceBinding;


public class ServiceFragment extends Fragment {
    private FragmentServiceBinding binding;
    public static final int GET_QUANTITY = 0;
    public static final int GET_QUANTITY_SUCCESSFULLY = 1;

    private ListView listViewService;
    private ListViewServiceAdapter listViewServiceAdapter;

    private RecyclerView recyclerViewCartService;
    private RecyclerViewServiceAdapter recyclerViewServiceAdapter;
    private List<String> listServiceInCart;
    public static AppCompatButton buttonAdd;
    public static LinearLayout linearLayoutTittleCartService;

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

        setWidget();
        setListView();
        setRecyclerViewCartService();
    }

    public void setWidget() {
        buttonAdd = binding.buttonAdd;
        linearLayoutTittleCartService = binding.linearLayoutTittleCartService;

        ((AppCompatButton) binding.buttonClearCartService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listServiceInCart.clear();
                recyclerViewServiceAdapter.notifyDataSetChanged();

                setCartServiceTittle(View.GONE);
            }
        });
    }

    public void setListView() {
        listViewService = binding.listViewService;

        List<String> serviceList = new ArrayList<>();
        serviceList.add("Service 1");
        serviceList.add("Service 2");
        serviceList.add("Service 3");
        serviceList.add("Service 4");
        serviceList.add("Service 5");
        serviceList.add("Service 1");
        serviceList.add("Service 2");
        serviceList.add("Service 3");
        serviceList.add("Service 4");
        serviceList.add("Service 5");
        serviceList.add("Service 1");
        serviceList.add("Service 2");
        serviceList.add("Service 3");
        serviceList.add("Service 4");
        serviceList.add("Service 5");

        listViewServiceAdapter = new ListViewServiceAdapter(getContext(), serviceList);
        listViewService.setAdapter(listViewServiceAdapter);
        listViewService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BottomSheetServiceFragment bottomSheet = new BottomSheetServiceFragment();
                bottomSheet.setTargetFragment(ServiceFragment.this, GET_QUANTITY);
                bottomSheet.show(getParentFragmentManager(), "ServiceBottomSheetDialogFragment");
            }
        });
    }

    public void setCartServiceTittle(int status){
        linearLayoutTittleCartService.setVisibility(status);
        buttonAdd.setVisibility(status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GET_QUANTITY_SUCCESSFULLY && data != null) {
            int quantity = data.getIntExtra("QUANTITY", 0);

            listServiceInCart.add("Service " + (listServiceInCart.size() + 1));
            if (listServiceInCart.size() == 1) {
                setCartServiceTittle(View.VISIBLE);
            }

            recyclerViewServiceAdapter.notifyDataSetChanged();

            FragmentManager fragmentManager = getParentFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("ServiceBottomSheetDialogFragment");
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }

    public void setRecyclerViewCartService() {
        recyclerViewCartService = binding.recyclerViewCartService;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCartService.setLayoutManager(layoutManager);
        recyclerViewCartService.addItemDecoration(new RecyclerViewServiceAdapter.StartEndSpaceItemDecoration(55, 20, 55));

        listServiceInCart = new ArrayList<>();
        recyclerViewServiceAdapter = new RecyclerViewServiceAdapter(getContext(), listServiceInCart, true);

        recyclerViewCartService.setAdapter(recyclerViewServiceAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}