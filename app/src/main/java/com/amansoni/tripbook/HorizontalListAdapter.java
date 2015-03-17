package com.amansoni.tripbook;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.provider.Images;
import com.amansoni.tripbook.util.ImageWrapper;

import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ListViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private static int mPosition = -1;
    protected final FragmentActivity mActivity;
    private ArrayList<TripBookCommon> tripBookItems;
    ArrayList<TripBookItem> selectedItems;
    private static long mItemId = 0;
    protected static boolean mEditable = false;

    /**
     * Initialize the dataset of the Adapter.
     * */
    public HorizontalListAdapter(FragmentActivity activity, TripBookItemData dataSet, long itemId, boolean editable) {
        mActivity = activity;
        tripBookItems = (ArrayList<TripBookCommon>) dataSet.getAllRows();
        selectedItems = new ArrayList<>();
        mItemId = itemId;
        mEditable = editable;
    }

    public static int getPosition() {
        return mPosition;
    }

    public ArrayList<TripBookItem> getSelectedItems(){
        return selectedItems;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.horizontal_list_item, viewGroup, false);
        return new ListViewHolder(v, selectedItems);
    }

    @Override
    public void onBindViewHolder(ListViewHolder listViewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        listViewHolder.tripBookItem = ((TripBookItem) tripBookItems.get(position));
        listViewHolder.itemName.setText(listViewHolder.tripBookItem.getTitle());
        ImageWrapper.loadImage(mActivity, listViewHolder.itemImage, Images.imageThumbUrls[position]);

        if(listViewHolder.isSelected)
            listViewHolder.itemView.setBackgroundColor(Color.RED);
        else {
            listViewHolder.itemView.setBackgroundColor(Color.CYAN);
        }
    }

    @Override
    public int getItemCount() {
        return tripBookItems.size();
    }

    /**
     * Custom ListViewHolder)
     */
    public static class ListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {
        public final TextView itemName;
        public final ImageView itemImage;
        public TripBookItem tripBookItem;
        public boolean isSelected;
        public ArrayList<TripBookItem> selectedItems;


        public ListViewHolder(View view, ArrayList<TripBookItem> selectedItems) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.item_title);
            itemImage = (ImageView) view.findViewById(R.id.item_image);
            isSelected = false;
            this.selectedItems = selectedItems;
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
            Toast.makeText(view.getContext(), "position = " + getPosition() + isSelected, Toast.LENGTH_SHORT).show();
            isSelected = !isSelected;

            if (mEditable) {
                if (isSelected) {
                    itemView.setBackgroundColor(Color.RED);
                    selectedItems.add(tripBookItem);
                } else {
                    selectedItems.remove(tripBookItem);
                    itemView.setBackgroundColor(Color.CYAN);
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            HorizontalListAdapter.mPosition = getPosition();
            Toast.makeText(view.getContext(), "long click" + getPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
