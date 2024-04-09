package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class BottomSheetBookingDetailsFragment extends BottomSheetDialogFragment {
    private static final String ARG_booking = "booking";
    private BookingAdapter.OnBookingReloadListener onBookingReloadListener;
    public void onBookingReloadListener(BookingAdapter.OnBookingReloadListener listener) {
        this.onBookingReloadListener = listener;
    }

    FragmentBottomSheetBookingDetailsBinding binding;
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

    private void setData() {
        if (booking != null) {
            binding.tvId.setText(booking.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            binding.tvBookingDay.setText(sdf.format(booking.getCreatedAt()));
            sdf = new SimpleDateFormat("HH:mm");
            binding.tvStartTime.setText(sdf.format(booking.getTimeStart()));
            binding.tvEndTime.setText(sdf.format(booking.getTimeEnd()));
            binding.tvPrice.setText(Utils.formatPrice(booking.getPrice()) + " VND");
            binding.tvStatus.setText(booking.getStatus());
            ICustomerService customerService = new CustomerServiceImpl(getContext());

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
        setData();
        setEvents();
        return  binding.getRoot();
    }

    private void setEvents() {
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditOrAddBookingActivity editOrAddBookingActivity = new EditOrAddBookingActivity();
                Bundle args = new Bundle();
                args.putSerializable("booking", booking);
                Intent intent  = new Intent(getContext(), EditOrAddBookingActivity.class);
                intent.putExtra("args", args);
                startActivity(intent);
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
                    IBookingService bookingService = new BookingServiceImpl(getContext());
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
    }
}