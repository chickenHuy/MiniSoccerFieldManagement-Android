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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.FieldRecyclerViewAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewFieldAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.FieldServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class FieldChooserFragment extends BottomSheetDialogFragment {
    List<Field> fields;
    ListViewFieldAdapter listViewFieldAdapter;
    RecyclerView recyclerView;
    FieldRecyclerViewAdapter adapterField;
    IFieldService fieldService;
    Button btnSave;
    private OnSaveSelectedFieldsListener onSaveSelectedFieldsListener;

    public void setOnSaveSelectedFieldsListener(OnSaveSelectedFieldsListener listener) {
        this.onSaveSelectedFieldsListener = listener;
    }
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
        setWigets(view);
        setEvents(view);
    }

    private void setEvents(View view) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSaveSelectedFieldsListener != null) {
                    // Get the selected fields
                    List<Field> selectedFields = adapterField.getSelectedFields();
                    if (selectedFields.size() == 3){
                    onSaveSelectedFieldsListener.onSaveSelectedFields(selectedFields);
                    dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please select 3 fields", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void setWigets(View view) {
        fieldService = new FieldServiceImpl(this.getContext());
        recyclerView = view.findViewById(R.id.recycler_view_file_chooser);
        fields = fieldService.findAllNormalField();
        adapterField = new FieldRecyclerViewAdapter(getContext(), fields);
        recyclerView.setAdapter(adapterField);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnSave = view.findViewById(R.id.btnSave);


    }
}