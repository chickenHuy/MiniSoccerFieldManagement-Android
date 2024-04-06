package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeGenerator {
    public static List<Time> generateTimes() {
        List<Time> times = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);

        while (calendar.get(Calendar.HOUR_OF_DAY) < 23) {
            times.add(new Time(calendar.getTimeInMillis()));
            calendar.add(Calendar.MINUTE, 30);
        }

        return times;
    }
}