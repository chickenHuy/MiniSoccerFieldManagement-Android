package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import java.sql.Date;
import java.util.logging.SimpleFormatter;

public class Utils {
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap convertByteToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Timestamp toTimestamp(String updateAt) {
        if (updateAt == null) {
            return null;
        }
        return Timestamp.valueOf(updateAt);
    }

    public static void setStatusBarColor(Activity activity) {
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.primaryColor, activity.getTheme()));
    }

    public static String formatVND(BigDecimal price) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return currencyVN.format(price);
    }

    public  static String formatPrice(BigDecimal price) {
        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(price);
    }

    public static Time convertStringToTime(String timeString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            long ms = format.parse(timeString).getTime();
            Time time = new Time(ms);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDay(String day) {
        switch (day.toLowerCase()) {
            case "mon":
                return "Monday";
            case "tue":
                return "Tuesday";
            case "wed":
                return "Wednesday";
            case "thu":
                return "Thursday";
            case "fri":
                return "Friday";
            case "sat":
                return "Saturday";
            case "sun":
                return "Sunday";
            default:
                return "Invalid day";
        }
    }

    public static  Date convertStringToSqlDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            java.util.Date parsedDate = format.parse(dateString);
            Date sqlDate = new Date(parsedDate.getTime());
            return sqlDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
