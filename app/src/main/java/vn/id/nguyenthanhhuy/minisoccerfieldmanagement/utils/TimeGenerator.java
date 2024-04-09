package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;


import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeGenerator {
    public static List<LocalTime> generateTimes() {
        List<LocalTime> times = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);

        while (calendar.get(Calendar.HOUR_OF_DAY) < 23) {
            // Convert java.sql.Time to java.time.LocalTime
            LocalTime localTime = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            times.add(localTime);
            calendar.add(Calendar.MINUTE, 30);
        }

        return times;
    }
}