package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Calendar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditOrAddBookingActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.BookingAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentBottomSheetBookingDetailsBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.BookingServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.CustomerServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.FieldServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IBookingService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ICustomerService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IFieldService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IMatchRecordService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.MatchRecordServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class BottomSheetBookingDetailsFragment extends BottomSheetDialogFragment {
    private static final String ARG_booking = "booking";
    SimpleDateFormat sdfDate;
    SimpleDateFormat sdfTime;
    ICustomerService customerService;
    IBookingService bookingService;
    private BookingAdapter.OnBookingReloadListener onBookingReloadListener;
    public void onBookingReloadListener(BookingAdapter.OnBookingReloadListener listener) {
        this.onBookingReloadListener = listener;
    }

    FragmentBottomSheetBookingDetailsBinding binding;
    private static final int REQUEST_CODE_EDIT_UPDATE_BOOKING = 2;
    private Booking booking;

    public BottomSheetBookingDetailsFragment() {
        booking = new Booking();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable(ARG_booking);
        }
    }

    private void setSchedule()
    {
        binding.tvBookingDay.setText(sdfDate.format(booking.getCreatedAt()));
        binding.tvStartTime.setText(sdfTime.format(booking.getTimeStart()));
        binding.tvEndTime.setText(sdfTime.format(booking.getTimeEnd()));
        binding.tvPrice.setText(Utils.formatPrice(booking.getPrice()) + " VND");
    }
    private void setData() {
        if (booking != null) {
            binding.tvId.setText(booking.getId());
            setSchedule();
            binding.tvStatus.setText(booking.getStatus());
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            // Set time fields to zero
            cal1.setTime(booking.getTimeStart());
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);

            cal2.setTime(new Date(System.currentTimeMillis()));
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);

            // Compare only the dates
            if (cal1.getTime().before(cal2.getTime())) {
                binding.llAction.setVisibility(View.GONE);
            }
            IMatchRecordService matchRecordService = new MatchRecordServiceImpl(getContext());
            if (booking.getStatus() == StaticString.COMPLETED ||  matchRecordService.findByBooking(booking.getId()) != null)
            {
                binding.llAction.setVisibility(View.GONE);
            }
            Customer customer = customerService.findById(booking.getCustomerId());
            if (customer != null) {
                binding.tvPhoneNumber.setText(customer.getPhoneNumber());
                binding.tvCustomerName.setText(customer.getName());
                if (customer.getImage() != null) {
                    Glide.with(getContext())
                            .load(Utils.convertByteToBitmap(customer.getImage()))
                            .apply(new RequestOptions().circleCrop())
                            .into(binding.imgCustomerAvatar);
                }
                else {
                    InputStream is = null;
                    try {
                        is = getContext().getAssets().open("defaultImage/none_user.png");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Decode the InputStream into a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    // Use the Bitmap (for example, set it to an ImageView)
                    binding.imgCustomerAvatar.setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentBottomSheetBookingDetailsBinding.inflate(inflater, container, false);
        bookingService = new BookingServiceImpl(getContext());
        customerService = new CustomerServiceImpl(getContext());
        sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        sdfTime = new SimpleDateFormat("HH:mm");
        setData();
        setEvents();
        return  binding.getRoot();
    }

    private void setEvents() {
        // Set OnClickListener for ImageView
        binding.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.tvPhoneNumber.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditOrAddBookingActivity editOrAddBookingActivity = new EditOrAddBookingActivity();
                Bundle args = new Bundle();
                args.putSerializable("booking", booking);
                Intent intent  = new Intent(getContext(), EditOrAddBookingActivity.class);
                intent.putExtra("args", args);
                startActivityForResult(intent, REQUEST_CODE_EDIT_UPDATE_BOOKING);
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog hỏi xác nhận xóa
                //Nếu đồng ý xóa thì gọi hàm xóa booking
                //Nếu không thì không làm gì cả
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete booking");
                builder.setMessage("Are you sure you want to delete this booking?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    //Xóa booking
                    booking.setStatus("Canceled");
                    bookingService.updateStatus(booking.getId(), "Canceled");
                    bookingService.softDelete(booking.getId());
                    //Load lại trang BookingFragment
                    if (onBookingReloadListener != null) {
                        Timestamp timestamp = booking.getTimeStart();
                        java.sql.Date date = new java.sql.Date(timestamp.getTime());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                        onBookingReloadListener.onBookingReload(dateFormat.format(date));
                    }
                    dismiss();

                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    //Không làm gì cả
                });
                builder.show();
            }
        });

        binding.customerLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_UPDATE_BOOKING && resultCode == RESULT_OK) {
            String date = data.getStringExtra("date");
            booking = bookingService.findById(booking.getId());
            setData();

            if (onBookingReloadListener != null) {
                onBookingReloadListener.onBookingReload(date);
            }
        }
    }


}