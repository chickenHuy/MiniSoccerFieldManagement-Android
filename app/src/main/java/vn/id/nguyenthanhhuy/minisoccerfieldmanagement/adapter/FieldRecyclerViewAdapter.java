package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public class FieldRecyclerViewAdapter extends RecyclerView.Adapter<FieldRecyclerViewAdapter.ViewHolder> {
    private List<Field> fields;
    private Context context;

    public FieldRecyclerViewAdapter(Context context, List<Field> fields) {
        this.context = context;
        this.fields = fields;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field_chooser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Field field = fields.get(position);
        holder.tvIdField.setText(field.getId());
        holder.tvNameField.setText(field.getName());
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameField;
        TextView tvIdField;
        CheckBox chkSelectField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameField= itemView.findViewById(R.id.tvNameField);
            tvIdField = itemView.findViewById(R.id.tvIdField);
            chkSelectField = itemView.findViewById(R.id.chkSelectField);
        }
    }
}