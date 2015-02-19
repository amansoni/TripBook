package com.amansoni.tripbook;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TbImage;
import com.amansoni.tripbook.model.TripBookItem;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
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

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.datalist_line_item, viewGroup, false);
        return new ViewHolder(v, ((FragmentActivity) viewGroup.getContext()).getSupportFragmentManager(), mDataSet);
    }

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        TripBookItem tbImage = (TripBookItem)mDataSet.getAllRows().get(position);
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getListName().setText(tbImage.getTitle());
        viewHolder.getListDescription().setText(tbImage.getCreatedAt());
//        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(tbImage.getImage()), 48, 48);
//        viewHolder.getListImage().setImageBitmap(ThumbImage);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.getAllRows().size(); // mDataSet.length;
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView listName;
        private final TextView listDescription;
        private final ImageView listImage;
        private final FragmentManager fragmentManager;
        private final TripBookItemData mData;

        public ViewHolder(View view, FragmentManager fragmentManager, TripBookItemData data) {
            super(view);
            this.fragmentManager = fragmentManager;
            view.setOnClickListener(this);
            listName = (TextView) view.findViewById(R.id.list_title);
            listDescription = (TextView) view.findViewById(R.id.list_description);
            listImage = (ImageView) view.findViewById(R.id.list_image);
            mData = data;
        }

        public TextView getListName() {
            return listName;
        }

        public TextView getListDescription() {
            return listDescription;
        }

        public ImageView getListImage() {
            return listImage;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
            TripBookItem selected = (TripBookItem)mData.getAllRows().get(getPosition());
            // set the db item key and create the view for a single item
            Bundle args = new Bundle();
            args.putLong("itemKey", selected.getId());
            ItemViewFragment itemViewFragment = new ItemViewFragment();
            itemViewFragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.container, itemViewFragment)
                    .addToBackStack("position = " + getPosition())
                    .commit();
        }
    }
}
