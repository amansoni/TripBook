package com.amansoni.tripbook.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.util.ImageWrapper;

import java.util.ArrayList;

/**
 * RecyclerView list fragment. Adds options to change the list view style
 */
public class HorizontalListFragment extends Fragment {

    private static final String TAG = "HorizontalListFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView mRecyclerView;
    public HorizontalListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private String mItemType = null;
    private long mItemId = 0;
    protected boolean mEditable = false;
    TripBookItemData tripBookItemData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripBookItemData = new TripBookItemData(getActivity());

        // set the item type for the list
        Bundle args = getArguments();
        if (args != null) {
            mItemType = args.getString("itemType");
            Log.d(TAG, "Now showing" + mItemType);
            mItemId = args.getLong("itemId");
            mEditable = args.getBoolean("editable");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horizontal_list, container, false);
        view.setTag(TAG);
        ((TextView) view.findViewById(R.id.textViewHeader)).setText(mItemType);
        if (!mEditable) {
            view.findViewById(R.id.add_button).setVisibility(View.GONE);
        }

        // setup RecyclerView and layout managers
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        // setup DataAdapter
        if (mItemType != null) {
            tripBookItemData.setItemType(mItemType);
        }
        if (mItemId != 0) {
            tripBookItemData.setItemId(mItemId);
        }
        mAdapter = new HorizontalListAdapter(getActivity(), tripBookItemData, mItemId, mEditable, R.color.list_text_selected, R.color.list_text_unselected);
        mRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(container);

//        ImageButton mAddButton = (ImageButton)view.findViewById(R.id.add_button);
//        mAddButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    new AddItemDialogFragment().show(getFragmentManager(), "addnew");
//                    return true;
//                }
//                return true; // consume the event
//            }
//        });

//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener((Context) getActivity(),mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Toast.makeText(view.getContext(), "click" + view.getClass(), Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        // do whatever
//                        Toast.makeText(view.getContext(), "long click"+ view.getClass(), Toast.LENGTH_SHORT).show();
//                    }
//
//                })
//        );

        return view;
    }

    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = mAdapter.getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        TripBookItem tripBookItem;
        tripBookItem = (TripBookItem) tripBookItemData.getAllRows().get(position);
//        if (mItemType == null) {
//        } else {
//            tripBookItem = (TripBookItem) new TripBookItemData(mItemType).getAllRows().get(position);
//        }

        switch (item.getItemId()) {
            case 1:
                tripBookItem.star();
                new TripBookItemData(getActivity()).update(tripBookItem);
                mAdapter.notifyItemChanged(position);
                synchronized(mAdapter){
                    mAdapter.notify();
                }
//                do your stuff
                //Toast.makeText(getActivity(), "star " + position, Toast.LENGTH_SHORT).show();
                break;
            case 2:
//                Toast.makeText(getActivity(), "star2 " + position, Toast.LENGTH_SHORT).show();
                // do your stuff
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Provide views to RecyclerView with data from mDataSet.
     */
    public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ListViewHolder> {
        private static final String TAG = "HorizontalListAdapter";
        protected boolean mEditable = false;
        protected int selectedColour = 0;
        protected int unSelectedColour = 0;
        private int mPosition = -1;
        private long mItemId = 0;
        protected final FragmentActivity mActivity;
        ArrayList<TripBookItem> selectedItems;
        private ArrayList<TripBookItem> tripBookItems;

        /**
         * Initialize the dataset of the Adapter.
         */
        public HorizontalListAdapter(FragmentActivity activity, TripBookItemData dataSet, long itemId, boolean editable, int list_text_selected, int list_text_unselected) {
            mActivity = activity;
            mItemId = itemId;
            mEditable = editable;
            selectedColour = list_text_selected;
            unSelectedColour = list_text_unselected;
            if (!editable) {
                if (itemId == 0)
                    tripBookItems = dataSet.getAllRows();
                else{
                    dataSet.setLinkedItemId(itemId);
                    tripBookItems = dataSet.getAllRows();
                }
                selectedItems = new ArrayList<>();
            } else {
                // get all rows for new items with no selected
                if (itemId == 0) {
                    selectedItems = new ArrayList<>();
                    tripBookItems = dataSet.getAllRows();
                } else {
                    selectedItems = dataSet.getAllRows();
                    tripBookItems = new TripBookItemData(activity, dataSet.getItemType()).getAllRows();
                }
            }
        }

        public int getPosition() {
            return mPosition;
        }

        public ArrayList<TripBookItem> getSelectedItems() {
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
            ImageWrapper.loadImageFromFile(mActivity, listViewHolder.itemImage, listViewHolder.tripBookItem.getThumbnail(), 400);

//        ImageWrapper.loadImage(mActivity, listViewHolder.itemImage, listViewHolder.tripBookItem.getPhoto().getFilename());

            for (TripBookCommon item : selectedItems) {
                if (item.getId() == listViewHolder.tripBookItem.getId()) {
                    Log.d(TAG, "In selectedItems" + listViewHolder.tripBookItem.getTitle());
                    listViewHolder.isSelected = true;
                }
            }
            if (selectedItems.contains(listViewHolder.tripBookItem)) {
                Log.d(TAG, "In selectedItems" + listViewHolder.tripBookItem.getTitle());
                listViewHolder.isSelected = true;
            } else {
                Log.d(TAG, "Not selectedItems" + listViewHolder.tripBookItem.getTitle());
            }


            if (listViewHolder.isSelected) {
                listViewHolder.itemName.setBackgroundColor(unSelectedColour);
                listViewHolder.itemName.setTextColor(Color.RED);
            } else {
                listViewHolder.itemName.setBackgroundColor(unSelectedColour);
                listViewHolder.itemName.setTextColor(Color.WHITE);
            }
        }

        @Override
        public int getItemCount() {
            return tripBookItems.size();
        }

        /**
         * Custom ListViewHolder)
         */
        public class ListViewHolder extends RecyclerView.ViewHolder
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
//            Toast.makeText(view.getContext(), "position = " + getPosition() + isSelected, Toast.LENGTH_SHORT).show();

                if (mEditable) {
                    isSelected = !isSelected;
                }
                if (isSelected) {
                    itemName.setTextColor(Color.RED);
                    selectedItems.add(tripBookItem);
                } else {
                    for (int i = 0; i < selectedItems.size(); i++) {
                        if (selectedItems.get(i).getId() == tripBookItem.getId()) {
                            selectedItems.remove(i);
                        }
                    }
                    itemName.setTextColor(Color.WHITE);
                }
            }

            @Override
            public boolean onLongClick(View view) {
                mPosition = getPosition();
//            Toast.makeText(view.getContext(), "long click" + getPosition(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

}
