package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ServiceManagementActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentListServiceDeletedBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;

public class ListServiceDeletedFragment extends Fragment {
    private FragmentListServiceDeletedBinding binding;

    private ListView listViewListService;
    private List<Service> listAllService;
    private ListViewServiceAdapter listViewServiceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListServiceDeletedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ServiceManagementActivity) requireActivity()).switchButtonSelected(4);
    }

    public void setWidget() {
        listViewListService = binding.listViewAllService;
        listAllService = new ArrayList<Service>();
        listViewServiceAdapter = new ListViewServiceAdapter(requireContext(), listAllService, true);
        listViewListService.setAdapter(listViewServiceAdapter);
    }

    public void listViewSetUp() {
        listViewListService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(new ContextThemeWrapper(requireContext(), R.style.popup_menu_background_white_radius_10dp), view);
                popup.getMenuInflater().inflate(R.menu.service_management_menu, popup.getMenu());
                popup.getMenu().findItem(R.id.menu_option_revert).setVisible(false);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_option_view_detail) {
                            return true;
                        }
                        if (item.getItemId() == R.id.menu_option_active) {
                            return true;
                        }
                        if (item.getItemId() == R.id.menu_option_inactive) {
                            return true;
                        }
                        if (item.getItemId() == R.id.menu_option_delete) {
                            return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }


    public void getListServiceFromDatabase() {
        listAllService.addAll(((ServiceManagementActivity) requireActivity()).getListServiceFromDatabase(10, 0, "", 1));
        listViewServiceAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}