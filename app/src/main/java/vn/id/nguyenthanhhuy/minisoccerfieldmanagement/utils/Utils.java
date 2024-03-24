package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
