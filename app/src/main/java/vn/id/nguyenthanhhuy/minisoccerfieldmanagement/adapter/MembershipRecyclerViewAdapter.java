package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;

public class MembershipRecyclerViewAdapter extends  RecyclerView.Adapter<MembershipRecyclerViewAdapter.ViewHolder> {

    private final List<Membership> memberships;
    private final Context context;
    private final OnMembershipClickListener listener;

    public MembershipRecyclerViewAdapter(Context context, List<Membership> memberships, OnMembershipClickListener listener) {
        this.memberships = memberships;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_membership, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Membership membership = memberships.get(position);
        holder.tvNameMembership .setText(membership.getName());
        holder.tvPriceMembership.setText(String.valueOf(membership.getMinimumSpendAmount()));
        holder.tvRateMembership.setText(String.valueOf(membership.getDiscountRate()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMembershipClick(membership);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberships.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameMembership, tvPriceMembership, tvRateMembership;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameMembership = itemView.findViewById(R.id.tvNameMembership);
            tvPriceMembership = itemView.findViewById(R.id.tvPriceMembership);
            tvRateMembership = itemView.findViewById(R.id.tvRate);
        }
    }
}
