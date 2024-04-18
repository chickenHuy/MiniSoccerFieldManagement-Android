package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.MainActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class MatchReminderService extends Service {
    private static final String CHANNEL_ID = "MatchReminderServiceChannel";
    private static final int TEMPORARY_NOTIFICATION_ID = 1;
    private Handler handler;
    private Runnable runnable;
    private IBookingService bookingService;
    private ICustomerService customerService;

    @Override
    public void onCreate() {
        super.onCreate();
        bookingService = new BookingServiceImpl(this);
        customerService = new CustomerServiceImpl(this);
        createNotificationChannel();
        startForegroundWithTemporaryNotification();
        startReminderRunnable();
    }
    @SuppressLint("ForegroundServiceType")
    private void startForegroundWithTemporaryNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Preparing...")
                .setSmallIcon(R.drawable.ic_notification)
                .build();

        startForeground(TEMPORARY_NOTIFICATION_ID, notification);
    }
    private void startReminderRunnable() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                List<Booking> upcomingBookings = bookingService.getBookingUpcoming();
                for (Booking booking : upcomingBookings) {
                    createNotificationForBooking(booking);
                }
                handler.postDelayed(this, 60 * 1000); // Run every minute
            }
        };
        handler.post(runnable);
    }

    @SuppressLint("ForegroundServiceType")
    private void createNotificationForBooking(Booking booking) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        }

        Customer customer = customerService.findById(booking.getCustomerId());
        int notificationId = (int) new Date().getTime(); // Unique ID for each notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Next match:" + Utils.getTimeFromTimestamp(booking.getTimeStart()) + " #" + booking.getId())
                .setContentText("Customer: " + customer.getName() +" - " + customer.getPhoneNumber())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();

        // Call startForeground() right after creating the notification
        startForeground(notificationId, notification);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Match Reminder Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Stop the runnable when the service is destroyed
    }
}