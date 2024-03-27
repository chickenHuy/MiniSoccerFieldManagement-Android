package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

public class Utils {
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
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


}
