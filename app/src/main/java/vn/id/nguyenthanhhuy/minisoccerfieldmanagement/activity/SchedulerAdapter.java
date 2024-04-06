package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.BookingAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.TimeGenerator;

public class SchedulerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Time> timeList;
    private static final int TYPE_TIME_SCHEDULER = 1;
    private static final int TYPE_FIELD_NAME = 0;

    public SchedulerAdapter() {
        timeList = TimeGenerator.generateTimes();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_FIELD_NAME;
        return TYPE_TIME_SCHEDULER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if (viewType == TYPE_FIELD_NAME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_corner, parent, false);
            return new SchedulerAdapter.FieldNameViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_scheduler, parent, false);
        return new SchedulerAdapter.TimeSchedulerViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            return;
        } else
        if (holder instanceof SchedulerAdapter.TimeSchedulerViewHolder) {
            ((SchedulerAdapter.TimeSchedulerViewHolder) holder).tvTime.setText(timeList.get(position -1).toString().substring(0, 5));
        }
    }

    @Override
    public int getItemCount() {
        return  (timeList.size() + 1);
    }


    class TimeSchedulerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        TimeSchedulerViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
    class FieldNameViewHolder extends RecyclerView.ViewHolder {
        TextView tvFieldName;

        FieldNameViewHolder(View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
        }
    }

}
