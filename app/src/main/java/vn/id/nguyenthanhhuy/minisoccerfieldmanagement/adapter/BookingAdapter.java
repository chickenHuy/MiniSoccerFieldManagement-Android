package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import static vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R.color.black_overlay;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.BookingServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.FieldServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IBookingService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ICustomerService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.TimeGenerator;

public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FIELD_NAME = 0;
    private static final int TYPE_TIME_SCHEDULER = 1;

    // Your data list here
    private Context context;
    private IBookingService bookingService;
    private ICustomerService customerService;
    private IFieldService fieldService;
    private List<Booking> bookingList;
    private List<Field> fieldList;
    private List<LocalTime> timeList;
    private List<Integer> positionBooked;
    private Booking[] data;
    private Boolean[] hide;
    private Boolean[] blocked;

    public BookingAdapter(List<Booking> dataList, List<Field> fieldList, Context context) {
        this.bookingList = dataList;
        this.fieldList = fieldList;
        bookingService = new BookingServiceImpl(context);
        timeList = TimeGenerator.generateTimes();
        fieldService = new FieldServiceImpl(context);
        this.context = context;
        blocked = new Boolean[(timeList.size()+1)*fieldList.size()];
        data = new Booking[(timeList.size()+1)*fieldList.size()];
        hide = new Boolean[(timeList.size()+1)*fieldList.size()];
        for (int i = 0; i < timeList.size(); i++)
        {
            for (int j = 0; j < fieldList.size(); j++)
            {
                blocked[i * fieldList.size() + j] = false;
                hide[i * fieldList.size() + j] = true;
            }
        }
        positionBooked = getAllPositonBooked();
        customerService = new CustomerServiceImpl(context);
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


        }
        else
            if (positionBooked.contains(position))
            {

                ((TimeSlotViewHolder) holder).linearLayoutTimeSlot.setBackground(context.getDrawable(R.drawable.background_timeslot_booked));
                ((TimeSlotViewHolder) holder).tvPhone.setText("");
                ((TimeSlotViewHolder) holder).tvCustomer.setText("");
                ((TimeSlotViewHolder) holder).tvPhone.setText("");
                ((TimeSlotViewHolder) holder).tvCustomer.setText("");
                if (!hide[position])
                {
                    Customer customer = customerService.findById(data[position].getCustomerId());
                    ((TimeSlotViewHolder) holder).tvPhone.setText(customer.getPhoneNumber());
                    ((TimeSlotViewHolder) holder).tvCustomer.setText(customer.getName());
                }
                else {
                    ((TimeSlotViewHolder) holder).tvPhone.setText("");
                    ((TimeSlotViewHolder) holder).tvCustomer.setText("");
                }


            }
            else if (blocked[position] != null && blocked[position])
            {
                ((TimeSlotViewHolder) holder).linearLayoutTimeSlot.setBackground(context.getDrawable(R.drawable.item_blocked));
                ((TimeSlotViewHolder) holder).tvPhone.setText("");
                ((TimeSlotViewHolder) holder).tvCustomer.setText(" Booked");

            }





    }

    private  List<Integer> getAllPositonBooked()
    {
        List<Integer> list = new ArrayList<>();
        for (Booking booking : bookingList)
        {
            list.addAll(getListPositionOfBooking(booking));
        }
        return list;
    }
    private List<Integer> getListPositionOfBooking(Booking booking)
    {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(booking.getTimeStart().getTime());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(booking.getTimeEnd().getTime());

        int hours = calendarStart.get(Calendar.HOUR_OF_DAY);
        int minutes = calendarEnd.get(Calendar.MINUTE);
        LocalTime timeStart = LocalTime.of(hours, minutes);


        int minutesStart = calendarStart.get(Calendar.HOUR_OF_DAY) * 60 + calendarStart.get(Calendar.MINUTE);
        int minutesEnd = calendarEnd.get(Calendar.HOUR_OF_DAY) * 60 + calendarEnd.get(Calendar.MINUTE);

        int duration = (minutesEnd - minutesStart) / 30;

        int col = 0;
        for (Field field : fieldList)
        {
            if (field.getId().equals(booking.getFieldId()))
            {
                break;
            }
            col++;
        }
        List<Field> tmp = new ArrayList<Field>();
        if (fieldList.get(col).getType().equals(StaticString.TYPE_5_A_SIDE))
        {

            tmp = fieldService.findParent(fieldList.get(col).getId());
        }
        else
        {
            tmp.add(fieldService.findById(fieldList.get(col).getCombineField1()));
            tmp.add(fieldService.findById(fieldList.get(col).getCombineField2()));
            tmp.add(fieldService.findById(fieldList.get(col).getCombineField3()));
        }
        List<Integer> colBlocked = new ArrayList<>();
        for (Field field : tmp)
        {
            colBlocked.add(getColByField(field));
        }

        int row = 1;
        for (LocalTime time : timeList)
        {
            if (time.equals(timeStart))
            {
                break;
            }
            row++;
        }
        int maxRow = row + duration;

        List<Integer> list = new ArrayList<>();
        hide[row * fieldList.size() + col] = false;
        for (int i = row ; i < maxRow; i++)
        {
            for (int j : colBlocked)
            {
                blocked[i * fieldList.size() + j] = true;
            }

            data[i * fieldList.size() + col] = booking;
            list.add(i * fieldList.size() + col);
        }
        return  list;

    }
    private int getColByField(Field field)
    {
        int col = 0;
        for (Field field1 : fieldList)
        {
            if (field1.getId().equals(field.getId()))
            {
                break;
            }
            col++;
        }
        return col;
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
        TextView tvPhone, tvCustomer;

        TimeSlotViewHolder(View itemView) {
            super(itemView);
            linearLayoutTimeSlot = itemView.findViewById(R.id.linear_layout_time_slot);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);

        }

    }


}