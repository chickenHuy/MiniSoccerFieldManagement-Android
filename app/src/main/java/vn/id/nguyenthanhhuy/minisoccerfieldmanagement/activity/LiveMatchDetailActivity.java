package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ServiceItemsRecyclerViewAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingDetail;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityLiveMatchDetailBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.MatchRecord;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceUsage;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ICustomerService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IServiceItemsService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IServiceService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IServiceUsageService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceItemsServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceUsageServiceImpl;

public class LiveMatchDetailActivity extends AppCompatActivity {

    private TextView tvCountdown;
    private ActivityLiveMatchDetailBinding binding;
    private Booking booking;
    private MatchRecord matchRecord;
    private BookingDetail bookingDetail;
    private IServiceUsageService serviceUsageService;
    private ICustomerService customerService;


    private List<ServiceItems> servicesItems;
    private ServiceItemsRecyclerViewAdapter serviceItemsRecyclerViewAdapter;
    private IServiceItemsService serviceItemsService;
    int discount = 0;
    private ServiceUsage serviceUsage;
    private NumberFormat format;
    private ExtendedFloatingActionButton btnCheckOut;
    private BigDecimal firstTotal = BigDecimal.ZERO;
    private  IServiceService serviceService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveMatchDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Lấy booking từ Intent
        serviceUsageService = new ServiceUsageServiceImpl(this);
        customerService = new CustomerServiceImpl(this);
        booking = (Booking) getIntent().getSerializableExtra("booking");
        matchRecord = (MatchRecord) getIntent().getSerializableExtra("matchRecord");
        bookingDetail = (BookingDetail) getIntent().getSerializableExtra("bookingDetail");
        setWidget();
        setAdapter();
        setInfo_Match();
        setAdditionalServices();
        setCheckOutEvent();
    }

    private void setCheckOutEvent() {
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show thông báo alert
                AlertDialog.Builder builder = new AlertDialog.Builder(LiveMatchDetailActivity.this);
                builder.setTitle("Check out");
                builder.setMessage("Are you sure you want to check out?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(LiveMatchDetailActivity.this, ServicePaymentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("serviceUsage", serviceUsage);
                    bundle.putBoolean("hasMatch", true);
                    bundle.putString("totalDiscount", String.valueOf(firstTotal.multiply(new BigDecimal(discount)).divide(new BigDecimal(100))));
                    bundle.putSerializable("listServiceItemInCart", (ArrayList<ServiceItems>) serviceItemsService.findByServiceUsage(serviceUsage.getId()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            }
        });
    }

    private void setAdapter() {
        serviceItemsService = new ServiceItemsServiceImpl(this);
        serviceService = new ServiceServiceImpl(this);
        serviceUsage = serviceUsageService.findByMatchRecord(matchRecord.getId());
        servicesItems = serviceItemsService.findByServiceUsage(serviceUsage.getId());
        serviceItemsRecyclerViewAdapter = new ServiceItemsRecyclerViewAdapter(servicesItems, this);
        binding.rvServiceInMatch.setAdapter(serviceItemsRecyclerViewAdapter);
        int numberOfColumns = 2;
        binding.rvServiceInMatch.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
    }

    private void setAdditionalServices() {
        binding.addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceFragment serviceFragment = new ServiceFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("has_match", true);

                if (serviceUsage != null) {
                    bundle.putString("serviceUsageId", serviceUsage.getId());
                }

                serviceFragment.setArguments(bundle);

                // Replace the current fragment with the new one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.matchContainer, serviceFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setWidget() {
        // Lấy TextView từ layout
        tvCountdown = findViewById(R.id.tvCountdown);

        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();

        // Lấy starttime và endtime từ booking
        long starttime = booking.getTimeStart().getTime();
        long endtime = booking.getTimeEnd().getTime();

        // Kiểm tra và thiết lập giá trị cho tvCountdown
        if (currentTime < starttime) {
            // Nếu thời gian hiện tại nhỏ hơn starttime, đặt giá trị là khoảng thời gian giữa start và end
            long duration = endtime - starttime;
            long seconds = (duration / 1000) % 60;
            long minutes = (duration / (1000 * 60)) % 60;
            long hours = (duration / (1000 * 60 * 60)) % 24;
            binding.tvCountdown.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        } else if (currentTime > endtime) {
            // Nếu thời gian hiện tại lớn hơn endtime, đặt giá trị là 00:00:00
            binding.tvCountdown.setText("00:00:00");
        } else {
            // Nếu thời gian hiện tại nằm giữa starttime và endtime, bắt đầu CountDownTimer
            long timeRemaining = endtime - currentTime;

            new CountDownTimer(timeRemaining, 1000) {
                public void onTick(long millisUntilFinished) {
                    // Cập nhật TextView với thời gian còn lại
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                    long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                    binding.tvCountdown.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                }

                public void onFinish() {
                    // Khi hết thời gian, cập nhật TextView
                    binding.tvCountdown.setText("00:00:00");
                }
            }.start();
        }

        btnCheckOut = findViewById(R.id.btnCheckOut);
    }
    private Boolean hasService()
    {
        servicesItems = serviceItemsService.findByServiceUsage(serviceUsage.getId());
        serviceItemsRecyclerViewAdapter.setServiceItems(servicesItems);
        return true;
    }
    private void setInfo_Match(){
        // Cập nhật thông tin trận đấu
        binding.liveMatchTittle.setText(booking.getId());
        binding.textViewField.setText(bookingDetail.getFieldName());
        binding.textViewCusName.setText(bookingDetail.getCustomerName());
        binding.textViewCusPhone.setText(bookingDetail.getCustomerPhone());
        // Định dạng thời gian để chỉ hiển thị giờ và phút
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTimeStart = timeFormat.format(booking.getTimeStart());
        String formattedTimeEnd = timeFormat.format(booking.getTimeEnd());

        binding.textViewCheckIn.setText(new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(matchRecord.getCheckIn()));
        binding.textViewTimeStart.setText(formattedTimeStart);
        binding.textViewTimeEnd.setText(formattedTimeEnd);
        binding.textViewDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(booking.getTimeStart()));

        format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        caculateServiceFee();
    }

    public void goBack(View view) {
        finish();
    }


    @Override
    public void onBackPressed() {
        hasService();
        caculateServiceFee();
        super.onBackPressed();
    }

    private void caculateServiceFee() {
        String formattedPrice = format.format(booking.getPrice());
        binding.textViewRentalFee.setText(formattedPrice);

        double total_service = serviceUsageService.getTotalServicePriceByMatchId(matchRecord.getId());
        String formattedTotal = format.format(total_service);
        binding.textViewAdditionalServices.setText(formattedTotal);

        discount = customerService.findDiscountByCustomer(booking.getCustomerId());
        binding.textViewDiscount.setText(String.valueOf(discount) + "%");

        BigDecimal total_service_bd = BigDecimal.valueOf(total_service);
        BigDecimal discount_bd = BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100));
        firstTotal = booking.getPrice().add(total_service_bd);
        BigDecimal total = firstTotal.multiply(BigDecimal.ONE.subtract(discount_bd));
        String formattedTotalPrice = format.format(total);
        binding.textViewTotal.setText(formattedTotalPrice);

    }
}