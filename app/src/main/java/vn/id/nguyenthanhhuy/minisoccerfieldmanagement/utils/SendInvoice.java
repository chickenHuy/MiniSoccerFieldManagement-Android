package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;

import android.telephony.SmsManager;
import android.view.SurfaceControl;

import java.text.SimpleDateFormat;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;

public class SendInvoice {


    private static String invoiceDetails(AppTransaction transaction)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return "# " + transaction.getId() + "\n" +
                "Date: " + dateFormat.format(transaction.getCreatedAt())+ "\n" +
                "Subtotal: " + Utils.formatVND(transaction.getTotalAmount()) + "\n" +
                "Other: " + Utils.formatVND(transaction.getAdditionalFee()) + "\n" +
                "Discount : -" + Utils.formatVND(transaction.getDiscountAmount()) + "\n" +
                "Total: " + Utils.formatVND(transaction.getFinalAmount());
    }
    public static void sendInvoice(AppTransaction transaction, String phone) {
        String decoratedMessage = "📄 Invoice Details 📄\n" + invoiceDetails(transaction) + "\nThank you for your business! 👍";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, decoratedMessage, null, null);
    }
}
