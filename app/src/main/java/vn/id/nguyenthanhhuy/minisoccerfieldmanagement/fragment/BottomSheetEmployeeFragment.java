package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.io.InputStream;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditProfileActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentBottomSheetEmployeeBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetEmployeeFragment extends BottomSheetDialogFragment {

    public interface OnDismissListener {
        void onDismiss();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
    private User employee_selected;
    private FragmentBottomSheetEmployeeBinding binding;
    private UserServiceImpl userService;

    public BottomSheetEmployeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BottomSheetEmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomSheetEmployeeFragment newInstance(User employee) {
        BottomSheetEmployeeFragment fragment = new BottomSheetEmployeeFragment();
        Bundle args = new Bundle();
        args.putSerializable("employee", employee);
        fragment.setArguments(args);
        return fragment;
    }

    private void setWidgets() {
        binding.textViewFullName.setText(employee_selected.getName());
        binding.textViewGender.setText(employee_selected.getGender());
        binding.textViewDateOfBirth.setText(employee_selected.getDateOfBirth());
        binding.textViewPhoneNumber.setText(employee_selected.getPhoneNumber());
        binding.textViewUserName.setText(employee_selected.getUserName());
        binding.textViewRole.setText(employee_selected.getRole());
        if (employee_selected.getImage() != null) {
            // Convert byte array to bitmap
            byte[] imageBytes = employee_selected.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            binding.imageViewEmplAvatar.setImageBitmap(bitmap);
        } else {
            try {
                InputStream inputStream = this.getContext().getAssets().open("defaultImage/none_user.png");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.imageViewEmplAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        binding.buttonDismissalMembership.setOnClickListener(v -> {
            // Dismiss the bottom sheet
            if(userService.softDelete(employee_selected.getId()))
            {
                // Notify the adapter
                Toast.makeText(getContext(), "Dismissal successfully", Toast.LENGTH_SHORT).show();
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
            dismiss();
        });

        binding.buttonEditEmployee.setOnClickListener(v -> {
            // Dismiss the bottom sheet
            dismiss();
            // Open EditOrAddEmployeeActivity
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            intent.putExtra("option", "editEmployee");
            intent.putExtra("employee", employee_selected);
            startActivity(intent);
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employee_selected = (User) getArguments().getSerializable("employee");
        }
        userService = new UserServiceImpl(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetEmployeeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setWidgets();
        return view;
    }
}