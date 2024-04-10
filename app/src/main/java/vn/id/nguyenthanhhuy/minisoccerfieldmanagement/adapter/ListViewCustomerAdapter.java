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

public class ListViewCustomerAdapter extends BaseAdapter {
    private Context context;
    private List<String> customerList;

    public ListViewCustomerAdapter(Context context, List<String> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_customer_in_fragment_customer, parent, false);
        }

        try {
            InputStream is = context.getAssets().open("defaultImage/none_user.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            ((ImageView) convertView.findViewById(R.id.avatar_image)).setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
