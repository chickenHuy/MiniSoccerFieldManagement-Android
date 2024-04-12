package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;

public class ListViewCustomerAdapter extends BaseAdapter {
    private Context context;
    private List<Customer> customerList;

    public ListViewCustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @Override
    public int getCount() {
        if (customerList == null) {
            return 0;
        }
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

        Customer customer = customerList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.customer_name);
        TextView phoneTextView = convertView.findViewById(R.id.customer_phone);
        nameTextView.setText(customer.getName());
        phoneTextView.setText(customer.getPhoneNumber());

        ImageView avatarImageView = convertView.findViewById(R.id.avatar_image);
        Bitmap customerBitmap = getCustomerBitmap(customer);
        avatarImageView.setImageBitmap(customerBitmap);
        return convertView;
    }

    private Bitmap getCustomerBitmap(Customer customer) {
        Bitmap bitmap;
        if (customer.getImage() != null) {
            bitmap = BitmapFactory.decodeByteArray(customer.getImage(), 0, customer.getImage().length);
        } else {
            try {
                InputStream is = context.getAssets().open("defaultImage/none_user.png");
                bitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
        }
        return bitmap;
    }
    public void updateData(List<Customer> newCustomerList) {
        this.customerList = newCustomerList;
        notifyDataSetChanged();
    }
    public void setData(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
