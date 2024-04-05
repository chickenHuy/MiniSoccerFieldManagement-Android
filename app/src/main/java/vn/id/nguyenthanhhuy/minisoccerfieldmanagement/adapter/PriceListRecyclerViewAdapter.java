package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BottomSheetEditPriceListFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class PriceListRecyclerViewAdapter extends RecyclerView.Adapter<PriceListRecyclerViewAdapter.ViewHolder>{
    List<PriceList> list;
    Context context;
    public  void setContext(Context context) {
        this.context = context;
    }

    public PriceListRecyclerViewAdapter(List<PriceList> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_list, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PriceList priceList = list.get(position);
        holder.tvTimeStart.setText(priceList.getStartTime().toString().substring(0, 5));
        holder.tvTimeEnd.setText(priceList.getEndTime().toString().substring(0, 5));
        holder.tvPrice.setText(Utils.formatVND(priceList.getUnitPricePer30Minutes()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                BottomSheetEditPriceListFragment editPriceListFragment = new BottomSheetEditPriceListFragment();
                editPriceListFragment.setOnPriceUpdatedListener(priceList1 -> {
                    list.set(position, priceList1);
                    notifyDataSetChanged();
                });
                Bundle bundle = new Bundle();
                bundle.putSerializable("priceList", list.get(position));
                editPriceListFragment.setArguments(bundle);
                editPriceListFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), editPriceListFragment.getTag());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeStart;
        TextView tvTimeEnd;
        TextView tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeStart = itemView.findViewById(R.id.tvTimeStart);
            tvTimeEnd = itemView.findViewById(R.id.tvTimeEnd);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

}
