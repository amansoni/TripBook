package com.amansoni.tripbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;

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
            position = RecyclerViewAdapter.getPosition();
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
}
