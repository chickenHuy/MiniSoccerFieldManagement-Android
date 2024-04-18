package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceItemsServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ListViewServiceItemsAdapter extends BaseAdapter {
    private Context context;
    private List<ServiceItems> serviceItemsList;

    private ServiceItemsServiceImpl serviceItemsService;


    public ListViewServiceItemsAdapter(Context context, List<ServiceItems> serviceItemsList) {
        this.context = context;
        this.serviceItemsList = serviceItemsList;
        serviceItemsService = new ServiceItemsServiceImpl(context);
    }

    @Override
    public int getCount() {
        if (serviceItemsList == null) {
            return 0;
        }
        return serviceItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_service_items, parent, false);
        }
        ServiceItems serviceItems = serviceItemsList.get(position);
        Service service = serviceItemsService.getServiceInfo(serviceItems.getServiceId());

        TextView tvServiceName = convertView.findViewById(R.id.service_name);
        TextView tvQuantity = convertView.findViewById(R.id.quantity);
        TextView tvPrice = convertView.findViewById(R.id.price_item);
        int quantity = serviceItems.getQuantity();
        BigDecimal price = service.getPrice().multiply(BigDecimal.valueOf(quantity));

        tvServiceName.setText(service.getName());
        tvQuantity.setText(String.valueOf(quantity));
        tvPrice.setText(Utils.formatVND(price));

        return convertView;
    }
}
