package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.AppTransactionServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ListViewInvoiceAdapter extends BaseAdapter {
    private Context context;
    private List<AppTransaction> appTransactionList;
    private AppTransactionServiceImpl appTransactionService;

    public ListViewInvoiceAdapter(Context context, List<AppTransaction> appTransactionList) {
        this.context = context;
        this.appTransactionList = appTransactionList;
    }

    @Override
    public int getCount() {
        if (appTransactionList == null) {
            return 0;
        }
        return appTransactionList.size();
    }

    @Override
    public Object getItem(int position) {
        return appTransactionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_invoice, parent, false);
        }

        AppTransaction appTransaction = appTransactionList.get(position);
        appTransactionService = new AppTransactionServiceImpl(context);
        String userName = appTransactionService.getUserNameByUserID(appTransaction.getUserID());

        TextView tvName = convertView.findViewById(R.id.customer_name);
        TextView tvIDInvoice = convertView.findViewById(R.id.invoiceId);
        TextView tvFinalPrice = convertView.findViewById(R.id.final_price);
        TextView tvDate = convertView.findViewById(R.id.dateOfInvoice);

        tvName.setText(userName);
        tvIDInvoice.setText(appTransaction.getId());
        tvFinalPrice.setText(Utils.formatVND(appTransaction.getFinalAmount()));
        Date date = new Date(appTransaction.getCreatedAt().getTime());
        tvDate.setText(Utils.formatDate(date));

        return convertView;
    }
}
