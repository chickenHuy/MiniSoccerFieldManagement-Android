package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditOrAddCustomerActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewCustomerAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentCustomerBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;

public class CustomerFragment extends Fragment {
    private FragmentCustomerBinding binding;
    private ListViewCustomerAdapter listViewCustomerAdapter;
    private ListView listViewCustomer;
    private CustomerServiceImpl customerService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCustomerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListView();
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditOrAddCustomerActivity.class);
                intent.putExtra("option", "addCustomer");
                startActivity(intent);
            }
        });

        listViewCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                BottomSheetCustomerFragment bottomSheet = BottomSheetCustomerFragment.newInstance(selectedCustomer);
                bottomSheet.setOnDismissListener(() -> {
                    setListView();
                });
                bottomSheet.show(getParentFragmentManager(), "CustomerBottomSheetDialogFragment");
            }
        });
    }

    private void setListView() {
        listViewCustomer = binding.listViewCustomer;
        List<Customer> customerList = new ArrayList<Customer>();
        customerService = new CustomerServiceImpl(getContext());
        customerList = customerService.findAll();
        listViewCustomerAdapter = new ListViewCustomerAdapter(requireContext(), customerList);
        binding.listViewCustomer.setAdapter(listViewCustomerAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }

    private void refreshListView() {
        List<Customer> customerList = customerService.findAll();
        listViewCustomerAdapter.setData(customerList);
        listViewCustomerAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}