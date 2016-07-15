package com.qian.quickshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qian.quickshop.R;
import com.qian.quickshop.data.ShoppingList;

/**
 * Created by Qian.
 */
public class ShoppingListAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mItems;

    public ShoppingListAdapter(Context context, String[] items) {
        mContext = context;
        mItems = items;
    }


    /**
     * Use to get the total number of elements in the array
     * @return length of the array
     */
    @Override
    public int getCount() {
        return mItems.length;
    }

    /**
     *
     * @param position position of the item in the array
     * @return the item at position p in the array
     */

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // ignore, not gonna use
    }

    /**
     * get view without using brand new holders
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, null);
            holder = new ViewHolder();
            holder.itemNameLabel = (TextView) convertView.findViewById(R.id.itemNameLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        String item = mItems[position];

        holder.itemNameLabel.setText(item);

        return convertView;
    }

    private static class ViewHolder {
        TextView itemNameLabel; // public by default
    }
}
