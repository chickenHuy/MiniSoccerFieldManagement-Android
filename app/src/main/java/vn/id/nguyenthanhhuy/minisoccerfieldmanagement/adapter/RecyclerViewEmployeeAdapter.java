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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;

public class RecyclerViewEmployeeAdapter extends RecyclerView.Adapter<RecyclerViewEmployeeAdapter.ViewHolder>{

    private final Context context;
    private List<User> employeeList;
    private final OnEmployeeClickListener listener;
    private final LayoutInflater inflater;

    public RecyclerViewEmployeeAdapter(Context context, List<User> employeeList, OnEmployeeClickListener listener, LayoutInflater inflater) {
        this.context = context;
        this.employeeList = employeeList;
        this.listener = listener;
        this.inflater = inflater;
    }

    public void updateData(List<User> newEmployeeList) {
        this.employeeList = newEmployeeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewEmployeeAdapter.ViewHolder holder, int position) {
        User user = employeeList.get(position);
        holder.textViewFullName.setText(user.getName());
        holder.textViewPhoneNumber.setText(user.getPhoneNumber());

        try {
            InputStream inputStream = context.getAssets().open("defaultImage/none_user.png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.imageViewEmplAvatar.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEmployeeClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewFullName;
        private TextView textViewPhoneNumber;
        private ImageView imageViewEmplAvatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.text_view_full_name);
            textViewPhoneNumber = itemView.findViewById(R.id.text_view_phone_number);
            imageViewEmplAvatar = itemView.findViewById(R.id.image_view_empl_avatar);
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

    public interface OnEmployeeClickListener {
        void onEmployeeClick(User user);
    }
}
