package com.amansoni.tripbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Aman on 24/02/2015.
 */
public class MapButtonAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MapButtonAdapter(Context context, String[] values) {
        super(context, R.layout.navigation_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.navigation_row, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.rowText);
        textView.setText(values[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.rowIcon);
        imageView.setImageResource(getTitleIcon(values[position]));
        return rowView;
    }

    private int getTitleIcon(String title) {

        if (context.getString(R.string.search_nearby) == title){
            return R.drawable.ic_action_search;
        }
        if (context.getString(R.string.select_places) == title){
            return R.drawable.ic_action_settings;
        }
        return R.drawable.ic_action_settings;
    }
}
