package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.EditOrAddBookingActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BookingFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    String[] images;
    List<Field> fields;
    LayoutInflater mLayoutInflater;

    public ViewPagerAdapter(Context context, String[] images, List<Field> fields) {
        this.context = context;
        this.images = images;
        this.fields = fields;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((FrameLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list_image_view_pager, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_view);
        TextView tvNameField = (TextView) itemView.findViewById(R.id.tvNameField);
        tvNameField.setText(fields.get(position).getName());
        try {
            InputStream is = context.getAssets().open(images[position]);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(container).addView(itemView);
        itemView.setOnClickListener(v -> {
            addNewBooking(fields.get(position));
        });
        return itemView;
    }

    private void addNewBooking(Field field) {
        Intent intent = new Intent(context, EditOrAddBookingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("startTime", "");
        bundle.putString("endTime", "");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        bundle.putLong("date", timestamp.getTime());
        bundle.putString("fieldId", field.getId());
        intent.putExtras(bundle);
        if(context instanceof Activity) {
            ActivityCompat.startActivityForResult((Activity) context, intent, BookingFragment.REQUEST_CODE_EDIT_ADD_BOOKING, null);
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}