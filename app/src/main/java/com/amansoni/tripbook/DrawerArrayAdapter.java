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
public class DrawerArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public DrawerArrayAdapter(Context context, String[] values) {
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
        // change the icon for Windows and iPhone
        return rowView;
    }

    private int getTitleIcon(String title) {

        if (context.getString(R.string.title_trip) == title){
            return R.drawable.ic_action_event;
        }
        if (context.getString(R.string.title_place) == title){
            return R.drawable.ic_action_about;
        }
        if (context.getString(R.string.title_social) == title){
            return R.drawable.ic_action_person;
        }
        if (context.getString(R.string.title_starred) == title){
            return R.drawable.ic_action_important;
        }
        if (context.getString(R.string.title_gallery) == title){
            return R.drawable.ic_action_picture;
        }
        if (context.getString(R.string.title_map) == title){
            return R.drawable.ic_action_place;
        }
        return R.drawable.ic_action_settings;
    }
}
