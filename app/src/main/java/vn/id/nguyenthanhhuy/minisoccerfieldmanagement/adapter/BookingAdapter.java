package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.TimeGenerator;

public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FIELD_NAME = 0;
    private static final int TYPE_TIME_SCHEDULER = 1;

    // Your data list here
    private List<Booking> bookingList;
    private List<Field> fieldList;
    private List<Time> timeList;


    public BookingAdapter(List<Booking> dataList, List<Field> fieldList) {
        this.bookingList = dataList;
        this.fieldList = fieldList;
        timeList = TimeGenerator.generateTimes();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < fieldList.size())
            return TYPE_FIELD_NAME;
        return TYPE_TIME_SCHEDULER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_FIELD_NAME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name_field, parent, false);
            return new FieldNameViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
            return new TimeSlotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FieldNameViewHolder) {
                Field field = fieldList.get(position);
                ((FieldNameViewHolder) holder).tvFieldName.setText(field.getName());


        } else {
            // Bind data for TimeSlot item
            // You need to implement the logic here
        }
    }

    @Override
    public int getItemCount() {
        return (fieldList.size()) * (timeList.size()+1);
    }

    class FieldNameViewHolder extends RecyclerView.ViewHolder {
        TextView tvFieldName;

        FieldNameViewHolder(View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
        }
    }


    class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutTimeSlot;

        TimeSlotViewHolder(View itemView) {
            super(itemView);
            linearLayoutTimeSlot = itemView.findViewById(R.id.linear_layout_time_slot);
        }
    }
}