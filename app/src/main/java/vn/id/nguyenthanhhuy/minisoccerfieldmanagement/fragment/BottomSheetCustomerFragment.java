package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.io.InputStream;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditOrAddCustomerActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentProfileCustomerBottomSheetBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;

public class BottomSheetCustomerFragment extends BottomSheetDialogFragment {
    public interface OnDismissListener {
        void onDismiss();
    }
    private OnDismissListener onDismissListener;
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private Customer customer_selected;
    private FragmentProfileCustomerBottomSheetBinding binding;
    private CustomerServiceImpl customerService;

    public BottomSheetCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customer_selected = (Customer) getArguments().getSerializable("customer");
        }
        customerService = new CustomerServiceImpl(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileCustomerBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setWidgets();
        setEvents();
        return view;
    }
    public static BottomSheetCustomerFragment newInstance(Customer customer) {
        BottomSheetCustomerFragment fragment = new BottomSheetCustomerFragment();
        Bundle args = new Bundle();
        args.putSerializable("customer", customer);
        fragment.setArguments(args);
        return fragment;
    }

    private void setWidgets() {
        String membershipName = customerService.getMembershipNameByID(customer_selected.getMemberShipId());

        binding.customerName.setText(customer_selected.getName());
        binding.customerId.setText(customer_selected.getId());
        binding.customerPhone.setText(customer_selected.getPhoneNumber());
        binding.customerMembership.setText(membershipName);
        binding.customerTotalSpend.setText(customer_selected.getTotalSpend().toPlainString());

        if (customer_selected.getImage() != null) {
            // Convert byte array to bitmap
            byte[] imageBytes = customer_selected.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            binding.avatarImage.setImageBitmap(bitmap);
        } else {
            try {
                InputStream inputStream = this.getContext().getAssets().open("defaultImage/none_user.png");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.avatarImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setEvents(){
        binding.deleteButton.setOnClickListener(v -> {
            if(customerService.softDelete(customer_selected.getId()))
            {
                Toast.makeText(getContext(), "Dismissal successfully", Toast.LENGTH_SHORT).show();
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
            dismiss();
        });

        binding.editButton.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent(getContext(), EditOrAddCustomerActivity.class);
            intent.putExtra("option", "editCustomer");
            intent.putExtra("customer_id", customer_selected.getId());
            startActivity(intent);
        });
    }
}
