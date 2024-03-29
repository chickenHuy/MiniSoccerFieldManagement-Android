package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

public class BottomSheetServiceFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatButton buttonSubtraction = view.findViewById(R.id.button_subtraction);
        AppCompatButton buttonAddition = view.findViewById(R.id.button_addition);
        EditText editTextQuantity = view.findViewById(R.id.edit_text_quantity);
        AppCompatButton buttonAddToCart = view.findViewById(R.id.button_add_to_cart);

        buttonSubtraction.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
            if (quantity > 1) {
                quantity--;
                editTextQuantity.setText(String.valueOf(quantity));
            }
        });
        buttonAddition.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
            quantity++;
            editTextQuantity.setText(String.valueOf(quantity));
        });
        buttonAddToCart.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
            Intent intent = new Intent();
            intent.putExtra("QUANTITY", quantity);
            getTargetFragment().onActivityResult(getTargetRequestCode(), ServiceFragment.GET_QUANTITY_SUCCESSFULLY, intent);
            dismiss();
        });
    }
}