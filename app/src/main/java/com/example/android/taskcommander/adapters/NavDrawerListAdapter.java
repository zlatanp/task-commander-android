package com.example.android.taskcommander.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.taskcommander.R;


public class NavDrawerListAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] items;
    private int[] icons;

    public NavDrawerListAdapter(String[] items, int[] icons, Context context) {
        super(context, R.layout.drawer_item, items);
        this.context = context;
        this.items = items;
        this.icons = icons;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.drawer_item, parent, false);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.navDrawerItemImage);
        icon.setImageResource(icons[position]);

        TextView item = (TextView) convertView.findViewById(R.id.navDrawerItemText);
        item.setText(items[position]);

        return convertView;
    }
}
