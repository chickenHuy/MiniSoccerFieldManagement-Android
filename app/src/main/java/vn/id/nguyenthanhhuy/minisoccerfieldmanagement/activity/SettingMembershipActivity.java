package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.MembershipRecyclerViewAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BottomSheetMembershipFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.MembershipServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class SettingMembershipActivity extends AppCompatActivity {

    RecyclerView recyclerViewMemberShip;
    MembershipRecyclerViewAdapter membershipRecyclerViewAdapter;
    Button btnAddMembership;
    List<Membership> memberships;
    MembershipServiceImpl membershipService;
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_membership);
        Utils.setStatusBarColor(this);
        membershipService = new MembershipServiceImpl(this);
        setWigets();
        setEvents();

        // Khởi tạo và đăng ký BroadcastReceiver
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            // Gọi setWigets() khi nhận được sự kiện
            setWigets();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("membership_saved"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWigets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void setEvents() {
        btnAddMembership.setOnClickListener(v -> {
            // Hiển thị dialog thêm mới Membership
            BottomSheetMembershipFragment bottomSheetMembershipFragment = new BottomSheetMembershipFragment().newInstance(null, true);
            bottomSheetMembershipFragment.show(getSupportFragmentManager(), bottomSheetMembershipFragment.getTag());
        });
    }

    public void goBack(View view) {
        finish();
    }

    private void setWigets() {
        recyclerViewMemberShip = findViewById(R.id.recycler_view_membership_setting);
        btnAddMembership = findViewById(R.id.btnAddMembership);

        memberships = new ArrayList<Membership>();
        // Tạo và thêm các đối tượng Membership vào danh sách
//        memberships.add(new Membership("1", "STANDARD", 2, new BigDecimal(1000000), false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
//        memberships.add(new Membership("2", "PREMIUM", 5, new BigDecimal(2000000), false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
//        memberships.add(new Membership("3", "VIP", 10, new BigDecimal(5000000), false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

        List<Membership> membershipsFromDb = membershipService.findAll();
        if (membershipsFromDb == null) {
            memberships = new ArrayList<>();
        } else {
            memberships = membershipsFromDb;
        }
        // Cập nhật adapter của RecyclerView
        MembershipRecyclerViewAdapter adapter = new MembershipRecyclerViewAdapter(this, memberships, this::onMembershipClick);
        recyclerViewMemberShip.setAdapter(adapter);
        recyclerViewMemberShip.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onMembershipClick(@NonNull Membership membership) {
        BottomSheetMembershipFragment bottomSheetMembershipFragment = BottomSheetMembershipFragment.newInstance(membership, false);
        bottomSheetMembershipFragment.show(getSupportFragmentManager(), bottomSheetMembershipFragment.getTag());
    }
}