package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public class ListViewFieldAdapter extends ArrayAdapter<Field> {

    Context context;
    int resource;

    public ListViewFieldAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        try {
            Field field = getItem(position);
            TextView tvIdField = convertView.findViewById(R.id.tvIdField);
            TextView tvNameField = convertView.findViewById(R.id.tvNameField);
            TextView tvStatusField = convertView.findViewById(R.id.tvStatusField);
            if (field != null) {
                tvStatusField.setText(field.getStatus());
                tvNameField.setText(field.getName());
                tvIdField.setText(field.getId());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
