package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;
import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicReference;
public class CurrentTimeID {
    private static AtomicReference<Long> currentTime =
            new AtomicReference<>(System.currentTimeMillis());

    @NonNull
    public static String nextId(String prefix) {
        return prefix + currentTime.accumulateAndGet(System.currentTimeMillis(),
                (prev, next) -> next > prev ? next : prev + 1).toString();
    }

}
