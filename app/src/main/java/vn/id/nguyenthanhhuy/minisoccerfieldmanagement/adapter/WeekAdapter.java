package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {

    private final List<String> weekDays = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    private int adapterPosition = -1;
    private OnItemClickListener mListener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_in_week_item, parent, false);
        return new ViewHolder(view);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String day = weekDays.get(position);
        holder.dayTextView.setText(day.substring(0, 3));
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            adapterPosition = currentPosition;
            notifyItemRangeChanged(0, weekDays.size());

            if (mListener != null) {
                mListener.onItemClick(day);
            }
        });

        if (position == adapterPosition) {
            holder.dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primaryColor));
            holder.linear.setBackground(holder.itemView.getContext().getDrawable(R.drawable.rectangle_fill));
        } else {
            holder.dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            holder.linear.setBackground(holder.itemView.getContext().getDrawable(R.drawable.rectangle_outline));
        }
    }

    @Override
    public int getItemCount() {
        return weekDays.size();
    }


    public interface OnItemClickListener {
        void onItemClick(String date);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        LinearLayout linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linear = (LinearLayout) itemView.findViewById(R.id.linear_calendar);
            dayTextView = itemView.findViewById(R.id.tv_date_in_week);
        }
    }

}