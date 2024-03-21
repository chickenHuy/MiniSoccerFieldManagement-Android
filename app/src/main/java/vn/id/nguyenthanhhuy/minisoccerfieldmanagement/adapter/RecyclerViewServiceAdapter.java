package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

public class RecyclerViewServiceAdapter extends RecyclerView.Adapter<RecyclerViewServiceAdapter.ViewHolder> {

    private Context context;
    private List<String> serviceList;
    private LayoutInflater inflater;

    public RecyclerViewServiceAdapter(Context context, List<String> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_service_in_fragment_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String service = serviceList.get(position);
        holder.textViewServiceName.setText(service);

        try {
            InputStream is = context.getAssets().open("viewPagerImages/background_field_1.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            holder.imageViewServiceImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewServiceName;
        ImageView imageViewServiceImage;

        ViewHolder(View itemView) {
            super(itemView);

            textViewServiceName = itemView.findViewById(R.id.text_view_service_name);
            imageViewServiceImage = itemView.findViewById(R.id.image_view_service_image);
        }
    }

    public static class StartEndSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int startSpace;
        private int centerSpace;
        private int endSpace;

        public StartEndSpaceItemDecoration(int startSpace, int centerSpace, int endSpace) {
            this.startSpace = startSpace;
            this.centerSpace = centerSpace;
            this.endSpace = endSpace;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                outRect.left = startSpace;
                outRect.right = centerSpace;
            } else if (position == state.getItemCount() - 1) {
                outRect.right = endSpace;
            } else {
                outRect.right = centerSpace;
            }
        }
    }
}