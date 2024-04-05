package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

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
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.IPriceListService;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.PriceListServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class SettingPriceListActivity extends AppCompatActivity {

    WeekAdapter adapterWeek;
    PriceListRecyclerViewAdapter adapterPriceList;
    RecyclerView recyclerViewWeek;
    RecyclerView recyclerViewPriceList;
    IPriceListService priceListService;
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
        //priceLists = priceListService.findByDateAndType();
        priceListService = new PriceListServiceImpl(this);
        priceLists = priceListService.findByDateAndType("Monday", StaticString.TYPE_5_A_SIDE);
        recyclerViewPriceList = findViewById(R.id.recyclerViewPrice);
        adapterPriceList = new PriceListRecyclerViewAdapter(priceLists);
        recyclerViewPriceList.setAdapter(adapterPriceList);

        adapterWeek.setOnItemClickListener(new WeekAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String date) {
                // Thực hiện hành động khi một mục được nhấp vào
                // Ví dụ: cập nhật danh sách giá cả dựa trên ngày được chọn
                Toast.makeText(SettingPriceListActivity.this, date, Toast.LENGTH_SHORT).show();
            }
        });




    }
}