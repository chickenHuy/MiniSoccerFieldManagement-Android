package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.OnPriceUpdatedListener;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentEditPriceListBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IPriceListService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.PriceListServiceImpl;

public class BottomSheetEditPriceListFragment extends BottomSheetDialogFragment {
    private FragmentEditPriceListBinding binding;
    private PriceList priceList;
    public BottomSheetEditPriceListFragment() {
        // Required empty public constructor
    }
    private OnPriceUpdatedListener onPriceUpdatedListener;

    public void setOnPriceUpdatedListener(OnPriceUpdatedListener listener) {
        this.onPriceUpdatedListener = listener;
    }

    public static BottomSheetEditPriceListFragment newInstance(String param1, String param2) {
        BottomSheetEditPriceListFragment fragment = new BottomSheetEditPriceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            DecimalFormat df = new DecimalFormat("#");
            priceList = (PriceList) getArguments().getSerializable("priceList");
            binding.timeEnd.setText(priceList.getEndTime().toString().substring(0, 5));
            binding.timeStart.setText(priceList.getStartTime().toString().substring(0, 5));
            binding.editUnitPrice.setText(String.valueOf(df.format(priceList.getUnitPricePer30Minutes())));
            binding.dateInWeek.setText(String.valueOf(priceList.getDateOfWeek()));
        }
        setEvents();
    }

    private void setEvents() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    private void save() {
        try {
            String price = String.valueOf(binding.editUnitPrice.getText());
            if (price.isEmpty()) {
                throw new Exception("Price must not be empty");
            }
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(binding.editUnitPrice.getText()));
            if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("Price must be greater than 0");
            }
            priceList.setUnitPricePer30Minutes(bigDecimal);
            IPriceListService priceListService = new PriceListServiceImpl(this.getContext());
            if (priceListService.update(priceList)) {
                Toast.makeText(getContext(), "Price list has been updated", Toast.LENGTH_SHORT).show();
                updatePriceToRecyclerView(priceList);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePriceToRecyclerView(PriceList priceList) {
        if (onPriceUpdatedListener != null) {
            onPriceUpdatedListener.onPriceUpdated(priceList);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentEditPriceListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
