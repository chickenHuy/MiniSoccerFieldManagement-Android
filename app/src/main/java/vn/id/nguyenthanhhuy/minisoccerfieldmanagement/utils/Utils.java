package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

import java.sql.Date;
import java.util.logging.SimpleFormatter;

public class Utils {
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
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

    public static String formatPrice(BigDecimal price) {
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
            case "th 2":
                return "Monday";
            case "tue":
            case "th 3 ":
                return "Tuesday";
            case "wed":
            case "th 4":
                return "Wednesday";
            case "thu":
            case "th 5":
                return "Thursday";
            case "fri":
            case "th 6":
                return "Friday";
            case "sat":
            case "th 7":
                return "Saturday";
            case "sun":
            case "cn":
                return "Sunday";
            default:
                return "Invalid day";
        }
    }

    public static Date convertStringToSqlDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            java.util.Date parsedDate = format.parse(dateString);
            Date sqlDate = new Date(parsedDate.getTime());
            return sqlDate;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Locale localeVN = new Locale("vi", "VN");
                SimpleDateFormat formatVn = new SimpleDateFormat("dd MMMM yyyy", localeVN);
                java.util.Date parsedDate = formatVn.parse(dateString);
                Date sqlDate = new Date(parsedDate.getTime());
                return sqlDate;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }


    public static String getTimeFromTimestamp(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(timestamp);
    }

    public static String getDateFromTimestamp(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(timestamp);
    }



    public static String getDayOfWeekFromTimestamp(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Invalid day";
        }
    }

    public static  Boolean areConsecutiveRows(int sumCol, List<Integer> list){
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) / sumCol != list.get(i + 1) / sumCol - 1){
                return false;
            }
        }
        return true;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, dd, yyyy", Locale.US);
        return dateFormat.format(date);
    }

}
