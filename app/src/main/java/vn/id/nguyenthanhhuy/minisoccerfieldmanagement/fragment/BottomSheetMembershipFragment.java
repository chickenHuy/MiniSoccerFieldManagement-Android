package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetMembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetMembershipFragment extends BottomSheetServiceFragment {

    public BottomSheetMembershipFragment() {
        // Required empty public constructor
        // Ko biết m có sài class này, thêm construtor này vào là ko lỗi
        super(false, null, null);
    }

    // TODO: Rename and change types and number of parameters
    public static BottomSheetMembershipFragment newInstance(String param1, String param2) {
        BottomSheetMembershipFragment fragment = new BottomSheetMembershipFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_membership, container, false);
    }
}