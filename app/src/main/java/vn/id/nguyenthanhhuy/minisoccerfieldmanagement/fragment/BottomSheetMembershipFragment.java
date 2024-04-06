package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.math.BigInteger;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentBottomSheetMembershipBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.MembershipServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetMembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetMembershipFragment extends BottomSheetDialogFragment {
    private FragmentBottomSheetMembershipBinding binding;
    private boolean isNew;
    MembershipServiceImpl membershipService;
    Membership edit_membership;

    // Gọi phương thức onConfirm của listener khi người dùng xác nhận
    private void onUserConfirm()
    {
        String membershipname = binding.edtMemberShipName.getText().toString();
        String priceText = binding.edtPriceMembership.getText().toString();
        String quantityText = binding.editTextQuantity.getText().toString();
        if(membershipname.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()){
            binding.edtMemberShipName.setError(getResources().getString(R.string.error_empty));
            binding.edtPriceMembership.setError(getResources().getString(R.string.error_empty));
            binding.editTextQuantity.setError(getResources().getString(R.string.error_empty));
            return;
        }
        Membership membership = new Membership();
        membership.setName(membershipname);
        membership.setMinimumSpendAmount(new BigDecimal(priceText));
        membership.setDiscountRate(Integer.parseInt(quantityText));
        if (isNew) {
            membership.setId(CurrentTimeID.nextId("membership"));
            if(membershipService.add(membership))
            {
                Toast.makeText(getContext(), "Add membership successfully", Toast.LENGTH_SHORT).show();

                // Gửi sự kiện thông qua LocalBroadcastManager
                Intent intent = new Intent("membership_saved");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                dismiss();
            }
            // Gọi service để thêm Membership mới vào cơ sở dữ liệu
            // MembershipService.add(membership);
        }
        else {
            membership.setId(edit_membership.getId());
            if(membershipService.update(membership))
            {
                Toast.makeText(getContext(), "Update membership successfully", Toast.LENGTH_SHORT).show();
                // Gửi sự kiện thông qua LocalBroadcastManager
                Intent intent = new Intent("membership_saved");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                dismiss();
            }
            // Gọi service để cập nhật Membership hiện tại
            // MembershipService.update(membership);
        }
    }


    public BottomSheetMembershipFragment() {
        // Required empty public constructor
        // Ko biết m có sài class này, thêm construtor này vào là ko lỗi
//        super(false, null, null);
    }

    // TODO: Rename and change types and number of parameters
    public static BottomSheetMembershipFragment newInstance(Membership membership, boolean isNew) {
        BottomSheetMembershipFragment fragment = new BottomSheetMembershipFragment();
        Bundle args = new Bundle();
        args.putSerializable("membership_input", membership);
        args.putBoolean("isNew", isNew);
        fragment.setArguments(args);
        return fragment;
    }

    public void setWidget(Membership membership) {
        if(membership!=null){
            binding.edtMemberShipName.setText(membership.getName());
            binding.edtPriceMembership.setText(String.valueOf(membership.getMinimumSpendAmount().toBigInteger()));
            binding.editTextQuantity.setText(String.valueOf(membership.getDiscountRate()));
        }
        else{
            binding.edtMemberShipName.setText("");
            binding.edtPriceMembership.setText("");
            binding.editTextQuantity.setText("");
        }

        binding.edtPriceMembership.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String minimumSpendText = s.toString();
                if (!minimumSpendText.isEmpty()) {
                    BigInteger minimumSpend = new BigInteger(minimumSpendText);
                    BigInteger compareValue = new BigInteger("1000000");
                    if (minimumSpend.compareTo(compareValue) < 0) {
                        binding.edtPriceMembership.setError(getResources().getString(R.string.error_minimum_spend_amount_too_low));
                    }
                }
            }
        });

        binding.editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String discountRateText = s.toString();
                if (!discountRateText.isEmpty()) {
                    int discountRate = Integer.parseInt(discountRateText);
                    if (discountRate > 50) {
                        binding.editTextQuantity.setError(getResources().getString(R.string.error_discount_rate_too_high));
                    }
                }
            }
        });

        binding.buttonAddition.setOnClickListener(v -> {
            if(binding.editTextQuantity.getText().toString().isEmpty()){
                binding.editTextQuantity.setText("0");
            }
            int quantity = Integer.parseInt(binding.editTextQuantity.getText().toString().trim());
            quantity++;
            binding.editTextQuantity.setText(String.valueOf(quantity));
        });

        binding.buttonSubtraction.setOnClickListener(v -> {
            if(binding.editTextQuantity.getText().toString().isEmpty()){
                binding.editTextQuantity.setText("0");
            }
            int quantity = Integer.parseInt(binding.editTextQuantity.getText().toString().trim());
            if (quantity > 1) {
                quantity--;
                binding.editTextQuantity.setText(String.valueOf(quantity));
            }
        });

        binding.buttonSaveMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserConfirm();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        membershipService = new MembershipServiceImpl(getContext());
        isNew = true;
        if (getArguments() != null) {
            edit_membership = (Membership) getArguments().getSerializable("membership_input");
            isNew = getArguments().getBoolean("isNew");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBottomSheetMembershipBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setWidget(edit_membership);
        return view;
    }
}