package com.amansoni.tripbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amansoni.tripbook.db.TripBookImageData;
import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.provider.Images;
import com.amansoni.tripbook.util.ImageWrapper;

import java.io.File;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ListViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private static int mPosition = -1;
    protected final FragmentActivity mActivity;
    private TripBookItemData mDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public HorizontalListAdapter(FragmentActivity activity, TripBookItemData dataSet) {
        mActivity = activity;
        mDataSet = dataSet;
    }

    public static int getPosition() {
        return mPosition;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.horizontal_list_item, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListViewHolder listViewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        TripBookItem tripBookItem = ((TripBookItem)mDataSet.getAllRows().get(position));
        listViewHolder.itemName.setText(tripBookItem.getTitle());
        ImageWrapper.loadImage(mActivity,listViewHolder.itemImage, Images.imageThumbUrls[position]);

//        //TODO get the associated image, not just the 1st 1
//        if (tripBookItem.getThumbnail() != null) {
//            listViewHolder.itemImage.setImageBitmap(tripBookItem.getThumbnail());
//        } else{
//            listViewHolder.itemImage.setImageResource(R.drawable.empty_photo);
//        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.getAllRows().size();
    }

    /**
     * Custom ListViewHolder)
     */
    public static class ListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {
        public final TextView itemName;
        public final ImageView itemImage;

        public ListViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.item_title);
            itemImage = (ImageView) view.findViewById(R.id.item_image);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menuInfo is null
//            menu.add(Menu.NONE, 1, Menu.NONE, R.string.title_starred);
//            menu.add(Menu.NONE, 2, Menu.NONE, R.string.title_gallery);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
//            TripBookItem selected = (TripBookItem) mData.getAllRows().get(getPosition());
//            // set the db item key and create the view for a single item
//            Bundle args = new Bundle();
//            args.putLong("itemKey", selected.getId());
//            ItemViewFragment itemViewFragment = new ItemViewFragment();
//            itemViewFragment.setArguments(args);
        }

        @Override
        public boolean onLongClick(View view) {
            HorizontalListAdapter.mPosition = getPosition();
            Toast.makeText(view.getContext(), "long click" + getPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
