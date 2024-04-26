package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.SurfaceControl;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.MainActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;

public class SendInvoice {


    private static String invoiceDetails(AppTransaction transaction)
    {
        return "Invoice\n" +
                "Subtotal: " + Utils.formatVND(transaction.getTotalAmount()) + "\n" +
                "Discount : -" + Utils.formatVND(transaction.getDiscountAmount()) + "\n" +
                "Total: " + Utils.formatVND(transaction.getFinalAmount());
    }

    public static void send(AppTransaction transaction, String phone, Context context)
    {    String message = invoiceDetails(transaction);
        SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_LONG).show();
            return;
        }
        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        int simId = subscriptionInfoList.get(0).getSubscriptionId();
        try {
            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(simId);
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Some fields is Empty", Toast.LENGTH_LONG).show();
        }
    }
}
