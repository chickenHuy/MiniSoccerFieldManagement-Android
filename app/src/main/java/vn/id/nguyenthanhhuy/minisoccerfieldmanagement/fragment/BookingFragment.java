package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditOrAddBookingActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.BookingAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.CalendarAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.SchedulerAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentBookingBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.CalendarDateModel;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.BookingServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.FieldServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IBookingService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.TimeGenerator;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;


public class BookingFragment extends Fragment implements CalendarAdapter.OnItemClickListener {
    private FragmentBookingBinding binding;
    private  int adapterPosition = -1;
    private RecyclerView recyclerView;
    private TextView tvDateMonth;
    private ImageView ivCalendarNext;
    private ImageView ivCalendarPrevious;

    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Calendar currentDate = Calendar.getInstance(Locale.ENGLISH);
    private ArrayList<Date> dates = new ArrayList<>();
    private CalendarAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CalendarDateModel> calendarList2 = new ArrayList<>();



    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private List<Field> fieldList;

    private SchedulerAdapter schedulerAdapter;

    private IFieldService fieldService;
    private IBookingService bookingService;

    public  interface onAdapterChangedListener{
        void onAdapterChanged(String date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentBookingBinding.inflate(inflater, container, false);
        tvDateMonth = binding.textDateMonth;
        recyclerView = binding.recyclerView;
        ivCalendarNext = binding.ivCalendarNext;
        ivCalendarPrevious = binding.ivCalendarPrevious;
        setUpAdapter();
        setUpClickListener();
        setUpCalendar();
        java.sql.Date date1 = new java.sql.Date(System.currentTimeMillis());
        return binding.getRoot();
    }

    private void loadScheduler(String date) {
        Timestamp dateTimestamp = new Timestamp(Utils.convertStringToSqlDate(date).getTime());
        bookingList = bookingService.findByDate(dateTimestamp);
        bookingAdapter = new BookingAdapter(bookingList, fieldList, getContext());
        bookingAdapter.onAdapterChangedListener(new BookingFragment.onAdapterChangedListener() {
            @Override
            public void onAdapterChanged(String date) {
                loadScheduler(date);
            }
        });
        binding.recyclerTimeSlot.setAdapter(bookingAdapter);
        binding.recyclerTimeSlot.setLayoutManager(layoutManager);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fieldService = new FieldServiceImpl(getContext());
        bookingService = new BookingServiceImpl(getContext());
        bookingList = bookingService.findByDate(new Timestamp(System.currentTimeMillis()));
        fieldList = fieldService.findAll();


        bookingAdapter = new BookingAdapter(bookingList, fieldList, getContext());
        binding.recyclerTimeSlot.setAdapter(bookingAdapter);
        int numberOfColumns = fieldList.size();
        layoutManager = new GridLayoutManager(getContext(),numberOfColumns);
        binding.recyclerTimeSlot.setLayoutManager(layoutManager);


        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(),1);
        schedulerAdapter = new SchedulerAdapter();
        binding.recyclerTimeScheduler.setAdapter(schedulerAdapter);
        binding.recyclerTimeScheduler.setLayoutManager(layoutManager2);

        binding.recyclerTimeScheduler.setOnTouchListener((v, event) -> true);
        binding.recyclerTimeSlot.setOnTouchListener((v, event) -> true);

        setEvents();
    }

    private void setEvents() {
        binding.addBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện chuyển sang activity EditOrAddBookingActivity
                // và truyền dữ liệu qua intent
                Intent intent = new Intent(getContext(), EditOrAddBookingActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(String text, String date, String day) {
        loadScheduler(text);
    }

    private void setUpClickListener() {
        ivCalendarNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendar();
            }
        });
        ivCalendarPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                if (cal.equals(currentDate))
                    setUpCalendar();
                else
                    setUpCalendar();
            }
        });
    }

    private void setUpAdapter() {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        adapter = new CalendarAdapter(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String text, String date, String day) {
                for (int index = 0; index < calendarList2.size(); index++) {
                    CalendarDateModel calendarModel = calendarList2.get(index);
                    calendarModel.setSelected(index == adapterPosition);
                }
                adapter.setData(calendarList2);
                adapter.setOnItemClickListener(this);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setUpCalendar() {
        ArrayList<CalendarDateModel> calendarList = new ArrayList<>();
        tvDateMonth.setText(sdf.format(cal.getTime()));
        Calendar monthCalendar = (Calendar) cal.clone();
        int maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        dates.clear();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int todayPosition = -1;
        while (dates.size() < maxDaysInMonth) {
            dates.add(monthCalendar.getTime());
            calendarList.add(new CalendarDateModel(monthCalendar.getTime(), false));
            if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == monthCalendar.get(Calendar.DAY_OF_YEAR)) {
                todayPosition = dates.size() - 1;
            }
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendarList2.clear();
        calendarList2.addAll(calendarList);
        adapter.setOnItemClickListener(this);
        adapter.setData(calendarList);
        if (todayPosition != -1) {
            recyclerView.scrollToPosition(todayPosition);
        }
    }

}