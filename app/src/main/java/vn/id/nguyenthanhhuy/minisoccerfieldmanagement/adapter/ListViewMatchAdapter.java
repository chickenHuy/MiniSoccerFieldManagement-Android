package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

public class ListViewMatchAdapter extends BaseAdapter {
    private Context context;
    private List<String> matchList;

    public ListViewMatchAdapter(Context context, List<String> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_match_in_fragment_home, parent, false);
        }
        return convertView;
    }
}
