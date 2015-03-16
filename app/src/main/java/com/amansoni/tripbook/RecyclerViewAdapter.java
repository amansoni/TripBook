package com.amansoni.tripbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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

import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ListViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private static int mPosition = -1;
    protected final FragmentActivity mActivity;
    private TripBookItemData mDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public RecyclerViewAdapter(FragmentActivity activity, TripBookItemData dataSet) {
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
                .inflate(R.layout.datalist_line_item, viewGroup, false);
        return new ListViewHolder(v, ((FragmentActivity) viewGroup.getContext()).getSupportFragmentManager(), mDataSet);
    }

//    RecyclerViewAdapter.ListViewHolder view = (RecyclerViewAdapter.ListViewHolder)mRecyclerView.findViewHolderForPosition(position);
//    if (view.mItem.isStarred())
//            view.imageStar.setImageResource(R.drawable.ic_action_important);
//    else
//            view.imageStar.setImageResource(R.drawable.ic_action_not_important);


    @Override
    public void onBindViewHolder(ListViewHolder listViewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        listViewHolder.mItem = (TripBookItem) listViewHolder.mData.getAllRows().get(position);
        listViewHolder.listName.setText(listViewHolder.mItem.getTitle());
        listViewHolder.listDescription.setText(listViewHolder.mItem.getCreatedAt());
//        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(listViewHolder.mItem.getImage()), 48, 48);
//        listViewHolder.listImage.setImageBitmap(ThumbImage);

        if (listViewHolder.mItem.isStarred()){
            Log.d(TAG, "Starred: " + position + " yes");
            listViewHolder.imageStar.setImageResource(R.drawable.ic_action_important);
        }
        else {
            Log.d(TAG, "Starred: " + position + " no");
            listViewHolder.imageStar.setImageResource(R.drawable.ic_action_not_important);
        }
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
        public final TextView listName;
        private final TextView listDescription;
        public final ImageView listImage;
        public final ImageView imageStar;
        private final FragmentManager fragmentManager;
        private final TripBookItemData mData;
        public TripBookItem mItem;

        public ListViewHolder(View view, FragmentManager fragmentManager, TripBookItemData data) {
            super(view);
            this.fragmentManager = fragmentManager;
            mData = data;
//            mItem = (TripBookItem) mData.getAllRows().get(getPosition());
            listName = (TextView) view.findViewById(R.id.list_title);
            listDescription = (TextView) view.findViewById(R.id.list_description);
            listImage = (ImageView) view.findViewById(R.id.list_image);
            imageStar = (ImageView) view.findViewById(R.id.imageStar);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menuInfo is null
            menu.add(Menu.NONE, 1, Menu.NONE, R.string.title_starred);
            menu.add(Menu.NONE, 2, Menu.NONE, R.string.title_gallery);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
            TripBookItem selected = (TripBookItem) mData.getAllRows().get(getPosition());
            // set the db item key and create the view for a single item
            Bundle args = new Bundle();
            args.putLong("itemKey", selected.getId());
            ItemViewFragment itemViewFragment = new ItemViewFragment();
            itemViewFragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.container, itemViewFragment)
                    //.addToBackStack("position = " + getPosition())
                    .commit();
        }

        @Override
        public boolean onLongClick(View view) {
            RecyclerViewAdapter.mPosition = getPosition();
            Toast.makeText(view.getContext(), "long click" + getPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
