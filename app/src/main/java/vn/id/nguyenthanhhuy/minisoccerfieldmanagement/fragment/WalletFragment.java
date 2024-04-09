package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewCustomerAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentWalletBinding;

public class WalletFragment extends Fragment {
    private FragmentWalletBinding binding;
    private ListViewCustomerAdapter listViewCustomerAdapter;
    private ListView listViewCustomer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentWalletBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListView();

    }

    private void setListView() {
        listViewCustomer = binding.listViewCustomer;
        List<String> customerList = new ArrayList<>();
        customerList.add("Nguyen Van A");
        customerList.add("Tran Thi B");
        customerList.add("Le Van C");
        customerList.add("Le Van C");
        customerList.add("Le Van C");
        customerList.add("Le Van C");
        customerList.add("Le Van C");
        customerList.add("Le Van C");
        customerList.add("Le Van C");
        customerList.add("Le Van C");


        listViewCustomerAdapter = new ListViewCustomerAdapter(requireContext(), customerList);
        binding.listViewCustomer.setAdapter(listViewCustomerAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}