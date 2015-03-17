package com.amansoni.tripbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.recycler.AddItemDialogFragment;

/**
 * RecyclerView list fragment. Adds options to change the list view style
 */
public class HorizontalListFragment extends Fragment {

    private static final String TAG = "HorizontalListFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView mRecyclerView;
    protected HorizontalListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private String mItemType = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the item type for the list
        Bundle args = getArguments();
        if (args != null) {
            mItemType = args.getString("itemType");
            Log.d(TAG, "Now showing" + mItemType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horizontal_list, container, false);
        view.setTag(TAG);

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

        // setup RecyclerView and layout managers
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false);
//
//        if (savedInstanceState != null) {
//            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }

        mLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        // setup DataAdapter
        TripBookItemData ds;
        if (mItemType == null) {
            ds = new TripBookItemData();
        } else {
            ds = new TripBookItemData(mItemType);
        }
        mAdapter = new HorizontalListAdapter(getActivity(), ds);
        mRecyclerView.setAdapter(mAdapter);
        registerForContextMenu(container);

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
        if (mItemType == null) {
            tripBookItem = (TripBookItem) new TripBookItemData().getAllRows().get(position);
        } else {
            tripBookItem = (TripBookItem) new TripBookItemData(mItemType).getAllRows().get(position);
        }

        switch (item.getItemId()) {
            case 1:
                tripBookItem.star();
                new TripBookItemData().update(tripBookItem);
                mAdapter.notifyItemChanged(position);
                synchronized(mAdapter){
                    mAdapter.notify();
                }
//                do your stuff
                Toast.makeText(getActivity(), "star " + position, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getActivity(), "star2 " + position, Toast.LENGTH_SHORT).show();
                // do your stuff
                break;
        }
        return super.onContextItemSelected(item);
    }
}
