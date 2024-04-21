package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IServiceService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ServiceItemsRecyclerViewAdapter extends RecyclerView.Adapter<ServiceItemsRecyclerViewAdapter.ViewHolder> {

    private List<ServiceItems> serviceItems;
    IServiceService serviceService;
    Context context;
    @SuppressLint("NotifyDataSetChanged")
    public void setServiceItems(List<ServiceItems> serviceItems) {
        this.serviceItems = serviceItems;
        notifyDataSetChanged();
    }
    public ServiceItemsRecyclerViewAdapter(List<ServiceItems> serviceItems, Context context) {
        this.serviceItems = serviceItems;
        serviceService = new ServiceServiceImpl(context);
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_service_in_fragment_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItems serviceItem = serviceItems.get(position);
        Service service = serviceService.findById(serviceItem.getServiceId());
        if (service != null) {
            holder.tvServiceName.setText(service.getName());
            holder.tvPirce.setText(Utils.formatVND(service.getPrice()));
            holder.tvQuantity.setText(String.valueOf(serviceItem.getQuantity()));
            holder.tvTotalPrice.setText(Utils.formatVND(service.getPrice().multiply(BigDecimal.valueOf(serviceItem.getQuantity()))));
            try {
                if (service.getImage() != null) {
                    Bitmap bitmap = Utils.convertByteToBitmap(service.getImage());
                    holder.sivImageService.setImageBitmap(bitmap);
                } else {
                    InputStream is = context.getAssets().open("defaultImage/serviceLoadError.png");
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    holder.sivImageService.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return serviceItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvPirce, tvQuantity, tvTotalPrice;
        ShapeableImageView sivImageService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPirce = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            sivImageService = itemView.findViewById(R.id.sivImageService);
        }
    }
}