package com.amansoni.tripbook;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.shamanland.fab.FloatingActionButton;

/**
 * RecyclerView list fragment. Adds options to change the list view style
 */
public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int GRID_COLUMNS = 2;
    private static final int STAGGERED_ROWS = 4;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private String mItemType = null;
//    private String mItemType = getString( R.string.title_trip);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the item type for the list
        Bundle args = getArguments();
        if (args != null) {
            mItemType = args.getString("itemType");
            Log.d(TAG, "Now showing" + mItemType);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.listview, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_grid) {
            setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
            return true;
        }

        if (item.getItemId() == R.id.action_view_list) {
            setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            return true;
        }

        if (item.getItemId() == R.id.action_view_staggered) {
            setRecyclerViewLayoutManager(LayoutManagerType.STAGGERED_LAYOUT_MANAGER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_frag, container, false);
        view.setTag(TAG);

        ImageButton mAddButton = (ImageButton)view.findViewById(R.id.add_button);
        mAddButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new AddItemDialogFragment().show(getFragmentManager(), "addnew");
                    return true;
                }
                return true; // consume the event
            }
        });

        // setup RecyclerView and layout managers
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        // setup DataAdapter
        TripBookItemData ds;
        if (mItemType == null) {
            ds = new TripBookItemData(getActivity());
        } else {
            ds = new TripBookItemData(getActivity(), mItemType);
        }
        mAdapter = new RecyclerViewAdapter(getActivity(), ds);
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

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
//        if (mRecyclerView.getLayoutManager() != null && mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) {
////            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
////                    .findFirstCompletelyVisibleItemPosition();
//        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMNS);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            case STAGGERED_LAYOUT_MANAGER:
                mLayoutManager = new StaggeredGridLayoutManager(STAGGERED_ROWS, StaggeredGridLayoutManager.HORIZONTAL);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
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
            tripBookItem = (TripBookItem) new TripBookItemData(getActivity()).getAllRows().get(position);
        } else {
            tripBookItem = (TripBookItem) new TripBookItemData(getActivity(), mItemType).getAllRows().get(position);
        }

        switch (item.getItemId()) {
            case 1:
                tripBookItem.star();
                new TripBookItemData(getActivity()).update(tripBookItem);
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

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER,
        STAGGERED_LAYOUT_MANAGER
    }

}
