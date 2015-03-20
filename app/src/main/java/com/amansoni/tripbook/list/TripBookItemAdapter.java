package com.amansoni.tripbook.list;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.model.TripBookItem;

import java.io.File;
import java.net.URI;
import java.util.List;


public class TripBookItemAdapter extends ArrayAdapter<TripBookItem> {

    private Context context;
    private int resourceId;

    public TripBookItemAdapter(Context context, int resourceId,
                               List<TripBookItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    private static Bitmap getPreview(URI uri) {
        File image = new File(uri);

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
            return null;

        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 40;
        return BitmapFactory.decodeFile(image.getPath(), opts);
    }

    /**
     * Populate the view holder with data.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TripBookItem tripBookItem = getItem(position);
        View viewToUse = null;

        // This block exists to inflate the photo list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            //TODO
            // viewToUse = mInflater.inflate(resourceId, null);
            viewToUse = mInflater.inflate(R.layout.list_row, null);
            holder.itemImage = (ImageView) viewToUse.findViewById(R.id.list_row_imageView);
            holder.itemTitle = (TextView) viewToUse.findViewById(R.id.list_row_title);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        // Set the thumbnail
        holder.itemImage.setImageBitmap(getPreview(tripBookItem.getThumbnailUri()));
        holder.itemTitle.setText(tripBookItem.getTitle());

        return viewToUse;
    }

    /**
     * The "ViewHolder" pattern is used for speed.
     * <p/>
     * Reference: http://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html
     */
    private class ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
    }
}