package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Console;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingDetail;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.BookingServiceImpl;

public class RecyclerViewMatchAdapter extends RecyclerView.Adapter<RecyclerViewMatchAdapter.ViewHolder> {
    private Context context;
    private List<Booking> bookingList;
    private BookingDetail bookingDetail;
    private BookingServiceImpl bookingService;

    public RecyclerViewMatchAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMatchTime;
        private TextView textViewMatchDay;
        private TextView textViewMatchMonth;
        private TextView textViewMatchField;
        private TextView textViewMatchCustomerName;
        private TextView textViewMatchCustomerPhone;
        private TextView textViewMatchDayOfWeek;
        public ViewHolder(View itemView) {
            super(itemView);
            // TODO: Initialize your views here
            textViewMatchDay = itemView.findViewById(R.id.text_view_day);
            textViewMatchMonth = itemView.findViewById(R.id.text_view_month);
            textViewMatchField = itemView.findViewById(R.id.text_view_field_name);
            textViewMatchCustomerName = itemView.findViewById(R.id.text_view_cus_name);
            textViewMatchCustomerPhone = itemView.findViewById(R.id.text_view_cus_phone);
            textViewMatchDayOfWeek = itemView.findViewById(R.id.text_view_day_of_week);
            textViewMatchTime = itemView.findViewById(R.id.text_view_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_match_in_fragment_home, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerViewMatchAdapter.ViewHolder holder, int position) {
        if (bookingList != null && !bookingList.isEmpty()) {
            Booking booking = bookingList.get(position);
            if (booking != null) {
                bookingService = new BookingServiceImpl(context);
                bookingDetail = bookingService.getBookingDetail(booking.getStatus(), booking.getId());

                if (bookingDetail != null) {
                    System.out.println("bookingDetail: " + bookingDetail.getDate()+ " " + bookingDetail.getTime());

                    LocalDateTime dateTime = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && bookingDetail.getDate() != null) {
                        dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(bookingDetail.getDate().getTime()), ZoneId.systemDefault());
                    }
                    LocalDate date = null;
                    if (dateTime != null) {
                        date = dateTime.toLocalDate();
                    }
                    LocalTime time = null;
                    if (dateTime != null) {
                        time = dateTime.toLocalTime();
                    }

                    String day = "";
                    if (date != null) {
                        day = String.valueOf(date.getDayOfMonth());
                    }
                    String month = "";
                    if (date != null) {
                        month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
                    }
                    String formattedTime = "";
                    if (time != null) {
                        formattedTime = time.format(DateTimeFormatter.ofPattern("hh:mm a"));
                    }

                    holder.textViewMatchDay.setText(day);
                    holder.textViewMatchMonth.setText(month);
                    holder.textViewMatchField.setText(bookingDetail.getFieldName() != null ? bookingDetail.getFieldName() : "");
                    holder.textViewMatchCustomerName.setText(bookingDetail.getCustomerName() != null ? bookingDetail.getCustomerName() : "");
                    holder.textViewMatchCustomerPhone.setText(bookingDetail.getCustomerPhone() != null ? bookingDetail.getCustomerPhone() : "");
                    holder.textViewMatchDayOfWeek.setText(bookingDetail.getDayOfWeek() != null ? bookingDetail.getDayOfWeek() : "");
                    holder.textViewMatchTime.setText(formattedTime);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}
