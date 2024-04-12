package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.ServiceManagementActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentListActiveServiceBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;

public class ListActiveServiceFragment extends Fragment {
    private FragmentListActiveServiceBinding binding;

    private ListView listViewListService;
    private List<Service> listAllService;
    private ListViewServiceAdapter listViewServiceAdapter;
    private boolean isLoading = false;
    private ExecutorService executorService;
    private int countServiceActive = 0;

    private CustomDialogFragment customDialogWarningFragment;
    private CustomDialogFragment customDialogFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListActiveServiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ServiceManagementActivity) requireActivity()).switchButtonSelected(2);

        ((ServiceManagementActivity) requireActivity()).currentFragmentIndex = 2;
        setWidget();
        listViewSetUp();
        loadService(((ServiceManagementActivity) requireActivity()).NUMBER_SERVICE_LOAD, 0, "Active", 0, ((ServiceManagementActivity) requireActivity()).filter);
    }
    public ListActiveServiceFragment() {
        listAllService = new ArrayList<Service>();
    }

    public ListActiveServiceFragment(String keyword, Context context) {
        listAllService = new ServiceServiceImpl(context).findServiceByKeyword(keyword, ServiceManagementActivity.NUMBER_SERVICE_LOAD, 0, "Active", 0);
    }


    public void setWidget() {
        listViewListService = binding.listViewAllService;
        listViewServiceAdapter = new ListViewServiceAdapter(requireContext(), listAllService, true);
        listViewListService.setAdapter(listViewServiceAdapter);

        countServiceActive = new ServiceServiceImpl(requireActivity()).countServices("Active", 0);
    }

    public void listViewSetUp() {
        listViewListService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(new ContextThemeWrapper(requireContext(), R.style.popup_menu_background_white_radius_10dp), view);
                popup.getMenuInflater().inflate(R.menu.service_management_menu, popup.getMenu());
                popup.getMenu().findItem(R.id.menu_option_active).setVisible(false);
                popup.getMenu().findItem(R.id.menu_option_revert).setVisible(false);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_option_view_detail) {
                            ((ServiceManagementActivity) requireActivity()).viewServiceDetails(listAllService.get(position).getId());
                            return true;
                        }
                        if (item.getItemId() == R.id.menu_option_edit) {
                            ((ServiceManagementActivity) requireActivity()).editService(listAllService.get(position).getId());
                            return true;
                        }
                        if (item.getItemId() == R.id.menu_option_inactive) {
                            boolean isSuccess = false;
                            if (listAllService.get(position).getStatus().equals("Inactive")) {
                                isSuccess = true;
                            } else {
                                if (new ServiceServiceImpl(requireActivity()).updateStatus(listAllService.get(position).getId(), "Inactive")) {
                                    listAllService.remove(position);
                                    listViewServiceAdapter.notifyDataSetChanged();
                                    isSuccess = true;
                                }
                            }
                            if (isSuccess) {
                                Toast.makeText(requireActivity(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                        if (item.getItemId() == R.id.menu_option_delete) {
                            customDialogWarningFragment = new CustomDialogFragment(requireActivity(), getResources().getString(R.string.warning), "Do you want to delete this service?", "warning", "No", "Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogWarningFragment.dismiss();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogWarningFragment.dismiss();
                                    if (((ServiceManagementActivity) requireActivity()).deleteService(listAllService.get(position).getId())) {
                                        customDialogFragment = new CustomDialogFragment(requireActivity(), getResources().getString(R.string.success), "", "success", "", getResources().getString(R.string.string_continue), null, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                listAllService.remove(position);
                                                listViewServiceAdapter.notifyDataSetChanged();
                                                customDialogFragment.dismiss();
                                                customDialogWarningFragment.dismiss();
                                            }
                                        });
                                        customDialogFragment.show(getParentFragmentManager(), "custom_dialog_notify");
                                    }
                                }
                            });
                            customDialogWarningFragment.show(getParentFragmentManager(), "custom_dialog_notify");
                            return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        listViewListService.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !isLoading) {
                    loadService(((ServiceManagementActivity) requireActivity()).NUMBER_SERVICE_LOAD, listAllService.size(), "Active", 0, ((ServiceManagementActivity) requireActivity()).filter);
                }
            }
        });
    }


    public void loadService(int limit, int offset, String status, int isDeleted, String orderBy) {
        if (isLoading) {
            return;
        }

        if (countServiceActive <= listAllService.size()) {
            return;
        }
        ServiceServiceImpl service = new ServiceServiceImpl(getContext());
        isLoading = true;
        executorService = Executors.newSingleThreadExecutor();
        binding.progressBar.setVisibility(View.VISIBLE);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Service> listServiceLoad = service.getServicesWithLimitAndOffset(limit, offset, status, isDeleted, orderBy);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listServiceLoad.size() > 0) {
                            listAllService.addAll(listServiceLoad);
                        }

                        binding.progressBar.setVisibility(View.GONE);
                        listViewServiceAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                });
            }
        });
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