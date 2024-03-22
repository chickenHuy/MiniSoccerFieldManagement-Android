package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.CalendarDateModel;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private ArrayList<CalendarDateModel> list = new ArrayList<>();
    private int adapterPosition = -1;
    private OnItemClickListener mListener;

    public CalendarAdapter(OnItemClickListener onItemClickListener) {
    }

    public interface OnItemClickListener {
        void onItemClick(String text, String date, String day);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.date_layout, parent, false);
        return new CalendarViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
 @Override
public void onBindViewHolder(CalendarViewHolder holder, int position) {
    CalendarDateModel itemList = list.get(position);
    holder.calendarDay.setText(itemList.getCalendarDay());
    holder.calendarDate.setText(itemList.getCalendarDate());

    holder.itemView.setOnClickListener(v -> {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition == RecyclerView.NO_POSITION) return;

        adapterPosition = currentPosition;
        notifyItemRangeChanged(0, list.size());

        String text = itemList.getCalendarYear();
        String date = itemList.getCalendarDate();
        String day = itemList.getCalendarDay();
        if (mListener != null) {
            mListener.onItemClick(text, date, day);
        }
    });

    if (position == adapterPosition) {
        holder.calendarDay.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primaryColor));
        holder.calendarDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primaryColor));
        holder.linear.setBackground(holder.itemView.getContext().getDrawable(R.drawable.rectangle_fill));
    } else {
        holder.calendarDay.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
        holder.calendarDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
        holder.linear.setBackground(holder.itemView.getContext().getDrawable(R.drawable.rectangle_outline));
    }
}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<CalendarDateModel> calendarList) {
        list.clear();
        list.addAll(calendarList);
        notifyDataSetChanged();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView calendarDay;
        TextView calendarDate;
        LinearLayout linear;

        public CalendarViewHolder(View view) {
            super(view);
            calendarDay = view.findViewById(R.id.tv_calendar_day);
            calendarDate = view.findViewById(R.id.tv_calendar_date);
            linear = view.findViewById(R.id.linear_calendar);
        }
    }
}