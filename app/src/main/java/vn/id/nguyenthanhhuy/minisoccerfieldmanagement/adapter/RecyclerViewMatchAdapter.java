package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
public class RecyclerViewMatchAdapter extends RecyclerView.Adapter<RecyclerViewMatchAdapter.ViewHolder> {
    private Context context;
    private List<String> matchList;

    public RecyclerViewMatchAdapter(Context context, List<String> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            // TODO: Initialize your views here
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_match_in_fragment_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO: Bind your data to your views here
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }
}
