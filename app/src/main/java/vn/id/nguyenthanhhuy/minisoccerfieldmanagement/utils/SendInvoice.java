package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils;

import android.telephony.SmsManager;

import java.text.SimpleDateFormat;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;

public class SendInvoice {

    String phone;
    AppTransaction transaction;
    public  SendInvoice(AppTransaction transaction, String phone) {
        this.transaction = transaction;
        this.phone = phone;
    }

    private String invoiceDetails()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return "# " + transaction.getId() + "\n" +
                "Date: " + dateFormat.format(transaction.getCreatedAt())+ "\n" +
                "Subtotal: " + Utils.formatVND(transaction.getTotalAmount()) + "\n" +
                "Other: " + Utils.formatVND(transaction.getAdditionalFee()) + "\n" +
                "Discount : -" + Utils.formatVND(transaction.getDiscountAmount()) + "\n" +
                "Total: " + Utils.formatVND(transaction.getFinalAmount());
    }
    public void sendInvoice() {
        String decoratedMessage = "📄 Invoice Details 📄\n" + invoiceDetails() + "\nThank you for your business! 👍";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, decoratedMessage, null, null);
    }
}
