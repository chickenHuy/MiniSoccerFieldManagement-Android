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

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.BottomSheetServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment.ServiceFragment;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class RecyclerViewServiceAdapter extends RecyclerView.Adapter<RecyclerViewServiceAdapter.ViewHolder> {

    private final Fragment fragment;
    private final Context context;
    private final List<Service> serviceList;
    private final boolean isCartService;
    private final LayoutInflater inflater;

    private final FragmentManager fragmentManager;
    private final Fragment targetFragment;

    public RecyclerViewServiceAdapter(Fragment fragment, Context context, List<Service> serviceList, boolean isCartService, FragmentManager fragmentManager) {
        this.fragment = fragment;
        this.context = context;
        this.isCartService = isCartService;
        this.serviceList = serviceList;
        this.inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        this.targetFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_service_in_fragment_home, parent, false);
        return new ViewHolder(view, isCartService);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Service service = serviceList.get(position);

        holder.textViewServiceName.setText(service.getName());
        holder.textViewServiceUnit.setText(service.getUnit());
        holder.textViewServicePrice.setText(Utils.formatVND(service.getPrice()));

        if (isCartService) {
            holder.textViewQuantity.setText("x" + String.valueOf(service.getOrderQuantity()));
        }

        if (service.getImage() == null) {
            try {
                InputStream is = context.getAssets().open("defaultImage/serviceLoadError.png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                holder.imageViewServiceImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                holder.imageViewServiceImage.setImageBitmap(Utils.convertByteToBitmap(service.getImage()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (isCartService) {
            holder.buttonClose.setVisibility(View.VISIBLE);
            holder.textViewQuantity.setVisibility(View.VISIBLE);

            holder.buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int itemPosition = holder.getAdapterPosition();
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        serviceList.remove(itemPosition);
                        notifyItemRemoved(itemPosition);

                        if (serviceList.size() == 0) {
                            ServiceFragment.linearLayoutTittleCartService.setVisibility(View.GONE);
                            ServiceFragment.buttonAdd.setVisibility(View.GONE);
                        }
                    }
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetServiceFragment bottomSheet = new BottomSheetServiceFragment(true, fragment, service);
                    bottomSheet.setTargetFragment(targetFragment, ServiceFragment.GET_QUANTITY);
                    bottomSheet.show(fragmentManager, "ServiceBottomSheetDialogFragment");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewServiceName;
        ImageView imageViewServiceImage;
        TextView textViewServiceUnit;
        TextView textViewServicePrice;

        AppCompatButton buttonClose;
        TextView textViewQuantity;

        ViewHolder(View itemView, boolean isCartService) {
            super(itemView);

            textViewServiceName = itemView.findViewById(R.id.text_view_service_name);
            imageViewServiceImage = itemView.findViewById(R.id.image_view_service_image);
            textViewServiceUnit = itemView.findViewById(R.id.text_view_unit);
            textViewServicePrice = itemView.findViewById(R.id.text_view_price);

            if (isCartService) {
                buttonClose = itemView.findViewById(R.id.button_close);
                textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            }
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