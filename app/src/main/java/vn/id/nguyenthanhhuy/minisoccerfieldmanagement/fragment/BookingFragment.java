package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import static vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R.drawable.background_green_radius_10dp;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.CustomerDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IPriceListDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.MembershipDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.PriceListDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.CalendarAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentBookingBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.CalendarDateModel;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;
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
    private ArrayList<CalendarDateModel> calendarList2 = new ArrayList<>();
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
        test();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(String text, String date, String day) {
//        binding.selectedDate.setText("Selected date: " + text);
//        binding.selectedDD.setText("Selected DD: " + date);
//        binding.selectedDay.setText("Selected day: " + day);
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
    private void test() {
        TableLayout tableLayoutFixed = binding.tableLayoutFixed;
        TableLayout tableLayoutScroll = binding.tableLayoutScroll;

        // Tạo hàng đầu tiên chứa tên các sân
        TableRow firstRowFixed = new TableRow(getContext());
        firstRowFixed.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Thêm một TextView trống vào đầu hàng để tạo không gian cho cột chứa giờ
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        emptyView.setText("");
        firstRowFixed.addView(emptyView);

        tableLayoutFixed.addView(firstRowFixed);

        TableRow firstRowScroll = new TableRow(getContext());
        firstRowScroll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Thêm tên các sân vào hàng đầu tiên
        for (char field = 'A'; field <= 'F'; field++) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textView.setText("Sân " + field);
            firstRowScroll.addView(textView);
        }

        tableLayoutScroll.addView(firstRowScroll);

        // Tạo các hàng tiếp theo chứa thông tin booking
        for (int i = 6; i <= 23; i++) {
            for (int j = 0; j < 2; j++) { // Thêm vòng lặp này để tạo các hàng cho 30 phút
                TableRow tableRowFixed = new TableRow(getContext());
                tableRowFixed.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                // Thêm giờ vào đầu mỗi hàng
                TextView timeView = new TextView(getContext());
                timeView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                timeView.setText(i + ":" + (j == 0 ? "00" : "30")); // Thay đổi ở đây để hiển thị 30 phút
                tableRowFixed.addView(timeView);

                tableLayoutFixed.addView(tableRowFixed);

                TableRow tableRowScroll = new TableRow(getContext());
                tableRowScroll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                // Thêm thông tin booking vào mỗi hàng
                for (char field = 'A'; field <= 'F'; field++) {
                    TextView textView = new TextView(getContext());
                    textView.setBackgroundColor(Color.RED);
                    textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    textView.setText("Thông tin booking");
                    tableRowScroll.addView(textView);
                }

                tableLayoutScroll.addView(tableRowScroll);
            }
        }
    }

}