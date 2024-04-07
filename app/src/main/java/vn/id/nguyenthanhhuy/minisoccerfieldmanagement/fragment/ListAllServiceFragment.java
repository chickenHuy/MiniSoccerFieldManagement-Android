package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ServiceManagementActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentListAllServiceBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;

public class ListAllServiceFragment extends Fragment {
    private FragmentListAllServiceBinding binding;

    private ListView listViewListService;
    private List<Service> listAllService;
    private ListViewServiceAdapter listViewServiceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListAllServiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ServiceManagementActivity) requireActivity()).switchButtonSelected(1);

        setWidget();
    }

    public void setWidget() {
        listViewListService = binding.listViewAllService;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}