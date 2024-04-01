package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentEditPriceListBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;

public class BottomSheetEditPriceListFragment extends BottomSheetDialogFragment {
    private FragmentEditPriceListBinding binding;
    private PriceList priceList;
    public BottomSheetEditPriceListFragment() {
        // Required empty public constructor
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