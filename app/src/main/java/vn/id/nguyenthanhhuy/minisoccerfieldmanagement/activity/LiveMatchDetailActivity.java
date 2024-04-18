package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingDetail;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.ActivityLiveMatchDetailBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.MatchRecord;

public class LiveMatchDetailActivity extends AppCompatActivity {

    private TextView tvCountdown;
    private ActivityLiveMatchDetailBinding binding;
    private Booking booking;
    private MatchRecord matchRecord;
    private BookingDetail bookingDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveMatchDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Lấy booking từ Intent
        booking = (Booking) getIntent().getSerializableExtra("booking");
        matchRecord = (MatchRecord) getIntent().getSerializableExtra("matchRecord");
        bookingDetail = (BookingDetail) getIntent().getSerializableExtra("bookingDetail");
        setWidget();
        setInfo_Match();
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
    }

    private void setInfo_Match(){
        // Cập nhật thông tin trận đấu
        binding.textViewField.setText(bookingDetail.getFieldName());
        binding.textViewCusName.setText(bookingDetail.getCustomerName());
        binding.textViewCusPhone.setText(bookingDetail.getCustomerPhone());
        // Định dạng thời gian để chỉ hiển thị giờ và phút
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTimeStart = timeFormat.format(booking.getTimeStart());
        String formattedTimeEnd = timeFormat.format(booking.getTimeEnd());

        binding.textViewTimeStart.setText(formattedTimeStart);
        binding.textViewTimeEnd.setText(formattedTimeEnd);
    }
}