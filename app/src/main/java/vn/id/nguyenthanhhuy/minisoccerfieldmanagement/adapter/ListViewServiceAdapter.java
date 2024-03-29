package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

public class ListViewServiceAdapter extends BaseAdapter {
    private Context context;
    private List<String> serviceList;

    public ListViewServiceAdapter(Context context, List<String> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
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