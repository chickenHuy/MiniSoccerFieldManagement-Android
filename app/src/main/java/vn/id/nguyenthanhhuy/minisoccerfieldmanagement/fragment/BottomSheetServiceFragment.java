package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.MainActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class BottomSheetServiceFragment extends BottomSheetDialogFragment {
    private boolean openInHomePage;
    private Fragment fragment;
    private Service service;

    private boolean isEmpty = false;
    private boolean isExceed = false;

    public BottomSheetServiceFragment(boolean openInHomePage, Fragment fragment, Service service) {
        this.openInHomePage = openInHomePage;
        this.fragment = fragment;
        this.service = service;
    }

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

        ((TextView) view.findViewById(R.id.text_view_service_name)).setText(this.service.getName());
        ((TextView) view.findViewById(R.id.text_view_unit_of_service)).setText(this.service.getUnit());
        ((TextView) view.findViewById(R.id.text_view_in_stock)).setText(String.valueOf(this.service.getQuantity()));
        ((TextView) view.findViewById(R.id.text_view_price_of_service)).setText(Utils.formatVND(this.service.getPrice()));

        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmpty = s.toString().trim().equals("");
                if (!isEmpty) {
                    int quantity = Integer.parseInt(s.toString().trim());
                    if (quantity < 1) {
                        editTextQuantity.setText("1");
                        return;
                    } else {
                        if (quantity > service.getQuantity()) {
                            isExceed = true;
                            editTextQuantity.setError((getResources().getString(R.string.in_stock) + ": " + service.getQuantity()));
                            return;
                        }
                    }
                }
                isExceed = false;
            }
        });
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
            if (isEmpty) {
                editTextQuantity.setError(getResources().getString(R.string.error_empty));
            } else {
                if (isExceed) {
                    editTextQuantity.setError(getResources().getString(R.string.in_stock) + ": " + service.getQuantity());
                } else {
                    if (!openInHomePage) {
                        int quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
                        Intent intent = new Intent();
                        intent.putExtra("QUANTITY", quantity);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), ServiceFragment.GET_QUANTITY_SUCCESSFULLY, intent);
                        dismiss();
                    } else {
                        this.service.setOrderQuantity(Integer.parseInt(editTextQuantity.getText().toString().trim()));
                        Activity activity = this.fragment.getActivity();

                        assert activity != null;
                        ((MainActivity) activity).args.putBoolean("has_match", false);
                        ((MainActivity) activity).args.putBoolean("open_with_service_item", true);
                        ((MainActivity) activity).args.putSerializable("service_item", this.service);
                        ((MainActivity) activity).bottomNavigationViewMenu.setSelectedItemId(R.id.menu_option_service);
                        dismiss();
                    }
                }
            }
        });
    }
}