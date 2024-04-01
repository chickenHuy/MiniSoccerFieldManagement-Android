package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.PriceListRecyclerViewAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.WeekAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BottomSheetMembershipFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class SettingPriceListActivity extends AppCompatActivity {

    WeekAdapter adapterWeek;
    PriceListRecyclerViewAdapter adapterPriceList;
    RecyclerView recyclerViewWeek;
    RecyclerView recyclerViewPriceList;

    List<PriceList> priceLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_price_list);
        setWidgets();
        setEvents();
        Utils.setStatusBarColor(this);
    }

    private void setEvents() {
    }

    private void setWidgets() {
        recyclerViewWeek = findViewById(R.id.recyclerViewWeek);
        adapterWeek = new WeekAdapter();
        recyclerViewWeek.setAdapter(adapterWeek);

        priceLists = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Time startTime = new Time(System.currentTimeMillis());
            Time endTime = new Time(System.currentTimeMillis() + 3600000); // 1 hour later
            BigDecimal unitPrice = new BigDecimal("1000.00");
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            PriceList priceList = new PriceList(
                    "id" + i,
                    startTime,
                    endTime,
                    "typeField" + i,
                    "dateOfWeek" + i,
                    unitPrice,
                    false,
                    currentTimestamp,
                    currentTimestamp
            );

            priceLists.add(priceList);
        }
        recyclerViewPriceList = findViewById(R.id.recyclerViewPrice);
        adapterPriceList = new PriceListRecyclerViewAdapter(priceLists);
        recyclerViewPriceList.setAdapter(adapterPriceList);



    }
}