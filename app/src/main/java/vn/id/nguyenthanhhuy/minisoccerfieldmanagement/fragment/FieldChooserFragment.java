package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.FieldRecyclerViewAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewFieldAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class FieldChooserFragment extends BottomSheetDialogFragment {
    List<Field> fields;
    ListViewFieldAdapter listViewFieldAdapter;
    RecyclerView recyclerView;
    FieldRecyclerViewAdapter adapterField;

    public FieldChooserFragment() {
        // Required empty public constructor
    }
    public static FieldChooserFragment newInstance(String param1, String param2) {
        FieldChooserFragment fragment = new FieldChooserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_field_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_file_chooser);
        fields = new ArrayList<Field>();
        for (int i = 1; i <= 10; i++) {
            String id = "id" + i;
            String name = "Field " + i;
            String status = i % 2 == 0 ? "Active" : "Inactive";
            String type = i % 3 == 0 ? "Type 5" : "Type B";
            byte[] image = new byte[0];
            String combineField1 = "Combine " + i;
            String combineField2 = "Combine " + (i + 1);
            String combineField3 = "Combine " + (i + 2);
            Boolean isDeleted = false;
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

            Field field = new Field(id, name, status, type, image, combineField1, combineField2, combineField3, isDeleted, createdAt, updatedAt);
            fields.add(field);
        }
        adapterField = new FieldRecyclerViewAdapter(getContext(), fields);
        recyclerView.setAdapter(adapterField);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}