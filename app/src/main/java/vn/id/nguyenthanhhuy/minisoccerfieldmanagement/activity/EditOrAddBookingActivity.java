package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Application;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.CalendarAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BookingFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.CalendarDateModel;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.BookingServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.FieldServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IBookingService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ICustomerService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IMembershipService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IPriceListService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IUserService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.MembershipServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.PriceListServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.UserServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.TimeGenerator;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class EditOrAddBookingActivity extends AppCompatActivity implements CalendarAdapter.OnItemClickListener  {
    private  int adapterPosition = -1;
    private RecyclerView recyclerView;
    private SimpleDateFormat sdfScheule;
    private TextView tvDateMonth, tvSchedule;
    private ImageView ivCalendarNext;
    private ImageView ivCalendarPrevious;
    private ICustomerService customerService;
    private TextView tvIdBooking;
    private IBookingService bookingService;

    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Calendar currentDate = Calendar.getInstance(Locale.ENGLISH);
    private ArrayList<Date> dates = new ArrayList<>();
    private CalendarAdapter adapter;
    private ArrayList<CalendarDateModel> calendarList2 = new ArrayList<>();
    private  IPriceListService priceListService;

    private String day;
    private List<Field> fields;
    private Button btnSave;
    private TextView tvPrice;
    private EditText edtStartTime, edtEndTime, edtPhoneNumber, edtCustomerName;
    private Spinner spinnerField;
    IFieldService fieldService;


    private java.sql.Date dateSelected;
    private Field fieldSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_add_booking);
        priceListService = new PriceListServiceImpl(EditOrAddBookingActivity.this);
        dateSelected = new java.sql.Date(new Date().getTime());
        sdfScheule = new SimpleDateFormat("dd/MM/yyyy");
        fieldSelected = new Field();
        tvDateMonth = findViewById(R.id.text_date_month);
        recyclerView = findViewById(R.id.recyclerView);
        ivCalendarNext = findViewById(R.id.iv_calendar_next);
        ivCalendarPrevious = findViewById(R.id.iv_calendar_previous);
        this.day = Utils.getDayOfWeekFromTimestamp(new Timestamp(new Date().getTime()));
        setUpAdapter();
        setUpClickListener();
        setUpCalendar();
        setClock();
        setSpinnerField();
        setWidgets();
        setEvents();
        getBundle();
    }

    private void getBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getParcelableExtra("args");
            if (bundle == null) {
                return;
            }
            Booking booking = bundle.getSerializable("booking") != null ? (Booking) bundle.getSerializable("booking") : null;
            if (booking != null) {
                Customer customer = customerService.findById(booking.getCustomerId());
                edtPhoneNumber.setText(customer.getPhoneNumber());
                edtCustomerName.setText(customer.getName());
                edtPhoneNumber.setEnabled(false);
                edtCustomerName.setEnabled(false);

                try {
                    this.day = Utils.getDayOfWeekFromTimestamp(booking.getTimeStart());
                    dateSelected = new java.sql.Date(booking.getTimeStart().getTime());
                    tvSchedule.setText(Utils.getDateFromTimestamp(booking.getTimeStart()));

                    edtStartTime.setText(Utils.getTimeFromTimestamp(booking.getTimeStart()));
                    edtEndTime.setText(Utils.getTimeFromTimestamp(booking.getTimeEnd()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                fieldSelected = fieldService.findById(booking.getFieldId());

                int position = -1;
                for (int i = 0; i < fields.size(); i++) {
                    if (fields.get(i).getId().equals(fieldSelected.getId())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    spinnerField.setSelection(position);
                }

                tvIdBooking.setText("#"+ booking.getId());
                tvPrice.setText(Utils.formatPrice(booking.getPrice()));
                btnSave.setText("Update");
            }
        }
    }

    private void setWidgets() {
        btnSave = findViewById(R.id.btnSave);
        tvPrice = findViewById(R.id.tvPrice);
        tvSchedule = findViewById(R.id.tvSchedule);
        tvSchedule.setText(sdfScheule.format(dateSelected));
        edtPhoneNumber = findViewById(R.id.edtPhoneCustomer);
        edtCustomerName = findViewById(R.id.edtNameCustomer);
        bookingService = new BookingServiceImpl(this);
        customerService = new CustomerServiceImpl(this);
        tvIdBooking = findViewById(R.id.tvIdBooking);
    }

    private void setEvents() {

        edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // User has finished entering the phone number
                    String phoneNumber = edtPhoneNumber.getText().toString();
                    Customer customer = customerService.findByPhoneNumber(phoneNumber);
                    if (customer == null) {
                        edtCustomerName.setText(R.string.kh_ch_h_ng_m_i);
                    } else {
                        edtCustomerName.setText(customer.getName());
                    }
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    LocalDate localDateFromSqlDate = dateSelected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    LocalDate localDate = LocalDate.now();


                    //Check time ande date >= now
                    if (localDateFromSqlDate.isBefore(localDate)) {
                        throw new Exception("Please Select Date >= now");
                    }
                    if (localDateFromSqlDate.isEqual(localDate)) {
                        Calendar cal = Calendar.getInstance();
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);
                        String[] parts = edtStartTime.getText().toString().split(":");
                        int hourStart = Integer.parseInt(parts[0]);
                        int minuteStart = Integer.parseInt(parts[1]);
                        if (hourStart < hour || (hourStart == hour && minuteStart < minute)) {
                            throw new Exception("Please Select Time >= now");
                        }
                    }
                    if (edtStartTime.getText().toString().isEmpty() || edtEndTime.getText().toString().isEmpty()) {
                        throw new Exception("Please Enter Start Time and End Time");
                    }
                    if (dateSelected == null) {
                        throw new Exception("Please Select Date");
                    }
                    if (edtCustomerName.getText().toString().isEmpty() || edtPhoneNumber.getText().toString().isEmpty()){
                        throw new Exception("Please Enter Customer Name and Phone Number");
                    }

                    Booking booking = new Booking();
                    booking.setFieldId(((Field) spinnerField.getSelectedItem()).getId());
                    Customer customer = customerService.findByPhoneNumber(edtPhoneNumber.getText().toString());
                    if (customer != null) {
                        booking.setCustomerId(customer.getId());
                    }
                    else {
                        customer = new Customer();
                        customer.setName(edtCustomerName.getText().toString());
                        customer.setPhoneNumber(edtPhoneNumber.getText().toString());
                        customer.setId(CurrentTimeID.nextId("C"));
                        customer.setTotalSpend(BigDecimal.ZERO);
                        booking.setCustomerId(customer.getId());
                        IMembershipService membershipService = new MembershipServiceImpl(EditOrAddBookingActivity.this);
                        Membership membership = membershipService.findBySpendAmount(new BigDecimal(9999));
                        customer.setMemberShipId(membership.getId());
                        customerService.add(customer);
                    }
                    booking.setNote("");
                    fieldSelected = (Field) spinnerField.getSelectedItem();
                    booking.setFieldId(fieldSelected.getId());
                    String startTimeStr = edtStartTime.getText().toString();

                    String[] parts = startTimeStr.split(":");
                    int hour = Integer.parseInt(parts[0]);
                    int minute = Integer.parseInt(parts[1]);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateSelected);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, minute);

                    Timestamp t = new Timestamp(cal.getTimeInMillis());
                    booking.setTimeStart(t);

                    parts = edtEndTime.getText().toString().split(":");
                    hour = Integer.parseInt(parts[0]);
                    minute = Integer.parseInt(parts[1]);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, minute);

                    t = new Timestamp(cal.getTimeInMillis());
                    booking.setTimeEnd(t);
                    if (booking.getTimeStart().equals(booking.getTimeEnd()) || booking.getTimeStart().after(booking.getTimeEnd())) {
                        throw new Exception("Start time must be before end time");
                    }
                    String idUpdated = tvIdBooking.getText().toString().replace("#", "");
                    if(fieldSelected.getType().equals(StaticString.TYPE_5_A_SIDE)) {
                        if(bookingService.isBookedOfParentField(idUpdated, fieldSelected, t, Utils.convertStringToTime(edtStartTime.getText().toString()), Utils.convertStringToTime(edtEndTime.getText().toString()))) {
                            throw new Exception("This field is already booked");
                        }
                    }
                    if(fieldSelected.getType().equals(StaticString.TYPE_7_A_SIDE)) {
                        if(bookingService.isBookedOfChildField(idUpdated, fieldSelected, t, Utils.convertStringToTime(edtStartTime.getText().toString()), Utils.convertStringToTime(edtEndTime.getText().toString()))) {
                            throw new Exception("This field is already booked");
                        }
                    }

                    BigDecimal price = priceListService.findPriceByTimeAndType(Utils.convertStringToTime(edtStartTime.getText().toString()),Utils.convertStringToTime(edtEndTime.getText().toString()), day, fieldSelected.getType());

                    booking.setPrice(price);
                    booking.setStatus(StaticString.ACTIVE);
                    booking.setUserId("1");
                    booking.setId(CurrentTimeID.nextId("B"));
                    //Show thông báo test
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditOrAddBookingActivity.this);
                    builder.setTitle("SAVE");
                    builder.setMessage("Do you want to save this booking?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Booking booking1 = bookingService.findById(idUpdated);
                            if (booking1 != null) {
                                booking.setId(idUpdated);
                                if(bookingService.update(booking))
                                {
                                    Toast.makeText(EditOrAddBookingActivity.this, "Booking Updated", Toast.LENGTH_SHORT).show();
                                    Timestamp timestamp = booking.getTimeStart();
                                    java.sql.Date date = new java.sql.Date(timestamp.getTime());
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                                    Intent intent = new Intent();
                                    intent.putExtra("date", dateFormat.format(date));
                                    setResult(RESULT_OK, intent);
                                    finish();

                                }
                                else {
                                    Toast.makeText(EditOrAddBookingActivity.this, "Booking failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else if(bookingService.add(booking))
                            {
                                Timestamp timestamp = booking.getTimeStart();
                                java.sql.Date date = new java.sql.Date(timestamp.getTime());
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                                Intent intent = new Intent();
                                intent.putExtra("date", dateFormat.format(date));
                                setResult(RESULT_OK, intent);
                                finish();
                                Toast.makeText(EditOrAddBookingActivity.this, "Booking added", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(EditOrAddBookingActivity.this, "Booking failed", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();






                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditOrAddBookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getPrice()
    {
        if (!edtStartTime.getText().toString().isEmpty() && !edtEndTime.getText().toString().isEmpty() && day!= null && fieldSelected != null) {
            BigDecimal price = priceListService.findPriceByTimeAndType(Utils.convertStringToTime(edtStartTime.getText().toString()), Utils.convertStringToTime(edtEndTime.getText().toString()), day, fieldSelected.getType());
            tvPrice.setText(Utils.formatPrice(new BigDecimal(price.toString())));
        }
    }
    private void setSpinnerField() {
        spinnerField = findViewById(R.id.spinnerField);
        fieldService = new FieldServiceImpl(this);
        fields = fieldService.findAll();

        ArrayAdapter<Field> adapter = new ArrayAdapter<Field>(this, android.R.layout.simple_spinner_item, fields) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(getItem(position).getName() + " : " + getItem(position).getType());  // replace with your field name getter
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(getItem(position).getName() + " : " + getItem(position).getType());  // replace with your field name getter
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerField.setAdapter(adapter);

        spinnerField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fieldSelected = (Field) parent.getItemAtPosition(position);
                getPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Thực hiện hành động khi không có mục nào được chọn
            }
        });
    }

    private void setClock() {
        edtStartTime = findViewById(R.id.edtStartTime);
        edtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditOrAddBookingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Check if the selected time is within the allowed range
                                if ((hourOfDay < 6 || hourOfDay > 22) || (hourOfDay == 22 && minute > 30) || (minute != 0 && minute != 30)) {
                                    Toast.makeText(EditOrAddBookingActivity.this, "Vui lòng chọn thời gian từ 6h sáng đến 22h30 và phút là 0 hoặc 30", Toast.LENGTH_LONG).show();
                                } else {
                                    edtStartTime.setText(hourOfDay + ":" + minute);
                                    getPrice();
                                }
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        edtEndTime = findViewById(R.id.edtEndTime);
        edtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditOrAddBookingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Check if the selected time is within the allowed range
                                if ((hourOfDay < 6 || hourOfDay > 23) || (hourOfDay == 22 && minute > 30) || (minute != 0 && minute != 30) || (hourOfDay == 23 && minute != 0))  {
                                    Toast.makeText(EditOrAddBookingActivity.this, "Vui lòng chọn thời gian từ 6h sáng đến 23h và phút là 0 hoặc 30", Toast.LENGTH_LONG).show();
                                } else {
                                    edtEndTime.setText(hourOfDay + ":" + minute);
                                    getPrice();
                                }
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onItemClick(String text, String date, String day) {
        this.dateSelected = Utils.convertStringToSqlDate(text);
        this.day = Utils.convertDay(day);
        tvSchedule.setText(sdfScheule.format(dateSelected));
        getPrice();
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
    public void goBack(View view) {
        finish();
    }

}