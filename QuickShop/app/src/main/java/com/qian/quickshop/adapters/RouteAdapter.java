package com.qian.quickshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qian.quickshop.R;
import com.qian.quickshop.data.Route;
import com.qian.quickshop.data.Pair;

import java.util.ArrayList;

/**
 * Created by Qian.
 */
public class RouteAdapter extends BaseAdapter {

    private Context mContext;
    private Pair[] mPairs;

    public RouteAdapter(Context context, Pair[] pairs) {
        mContext = context;
        mPairs = pairs;
    }

    /**
     * The function returns the length of the array in the list view.
     * @return length of list view array
     */

    @Override
    public int getCount() {
        return mPairs.length;
    }

    /**
     * The function is use to get the item in an array at position p
     * @param position: position of the item
     * @return the item
     */
    @Override
    public Object getItem(int position) {
        return mPairs[position];
    }

    /**
     * We are not using
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0; // ignore, not gonna use
    }

    /**
     * Use to generate the view without destroying the old ones
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.route_list_item, null);
            holder = new ViewHolder();
            holder.asileNumLabel = (TextView) convertView.findViewById(R.id.asileNumLabel);
            holder.itemNameLabel = (TextView) convertView.findViewById(R.id.itemNameLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Pair pair = mPairs[position];

        // if asile num < 0 means the item does not exist

        if (pair.getAisleNum() < 0) {
            String notFound = "not found";
            holder.asileNumLabel.setText(notFound);
            holder.itemNameLabel.setText(pair.getName());
        } else {
            holder.asileNumLabel.setText(pair.getAisleNum() + "");
            holder.itemNameLabel.setText(pair.getName());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView asileNumLabel; // public by default
        TextView itemNameLabel;
    }
}
