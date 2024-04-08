package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ListViewServiceAdapter extends BaseAdapter {
    private Context context;
    private List<Service> serviceList;
    private boolean isServiceManagement;

    public ListViewServiceAdapter(Context context, List<Service> serviceList, boolean isServiceManagement) {
        this.context = context;
        this.serviceList = serviceList;
        this.isServiceManagement = isServiceManagement;
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_service_in_fragment_service, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.text_view_service_name)).setText(serviceList.get(position).getName());
        ((TextView) convertView.findViewById(R.id.text_view_unit_of_service)).setText(serviceList.get(position).getUnit());
        ((TextView) convertView.findViewById(R.id.text_view_in_stock)).setText(String.valueOf(serviceList.get(position).getQuantity()));
        ((TextView) convertView.findViewById(R.id.text_view_price_of_service)).setText(Utils.formatVND(serviceList.get(position).getPrice()));

        if (isServiceManagement) {
            ((LinearLayout) convertView.findViewById(R.id.linear_layout_sold)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.text_view_sold)).setText(String.valueOf(serviceList.get(position).getSold()));
        }

        try {
            InputStream is = context.getAssets().open("defaultImage/serviceLoadError.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            ((ImageView) convertView.findViewById(R.id.image_view_service)).setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }
}