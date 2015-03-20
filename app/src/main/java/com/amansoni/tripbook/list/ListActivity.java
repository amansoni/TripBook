package com.amansoni.tripbook.list;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.RecyclerViewAdapter;
import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;

//        www.101apps.co.za

public class ListActivity extends ActionBarActivity {

    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    private ListView myListView;
    private boolean isAddFour = false;
    private boolean isRemoveFour = false;

    private String TAG = "junk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        TripBookItemData ds = new TripBookItemData(this, TripBookItem.TYPE_TRIP);
        mAdapter = new RecyclerViewAdapter(this, ds);
        mRecyclerView.setAdapter(mAdapter);

        myListView = (ListView) findViewById(R.id.listView);

//        PhotoAdapter photoAdapter = new PhotoAdapter(this, android.R.layout.simple_list_item_multiple_choice, PhotoGalleryImageProvider.getAlbumThumbnails(this));
//        myListView.setAdapter(photoAdapter);
        TripBookItemAdapter adapter = new TripBookItemAdapter(this, android.R.layout.simple_list_item_multiple_choice, new TripBookItemData(this, TripBookItem.TYPE_TRIP).getAllRows());
        myListView.setAdapter(adapter);

        myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        myListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
            //            called when an item is checked/unchecked during selection mode
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                mode.setTitle(myListView.getCheckedItemCount()
                        + " selected items");

            }

            /* called when the action mode is first created.
             The supplied menu is used for action buttons*/
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            inflate the menu resource providing the context menu items
                Log.i(TAG, "creating action mode");
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.list_select, menu);
                menu.findItem(R.id.cab_four).setVisible(false);
                return true;
            }

            //called to refresh action mode action menu whenever it is invalidated
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                Log.i(TAG, "preparing action mode");
                if (isAddFour) {
//                    set menu item Four visible
                    Log.i(TAG, "adding menu item Four");
//                    menu.findItem(R.id.cab_four).setVisible(true);
                    return true;
                } else if (isRemoveFour) {
//                    set menu item four invisible
                    Log.i(TAG, "removing menu item Four");
//                    menu.findItem(R.id.cab_four).setVisible(false);
                    return true;
                } else {
//            return false if nothing is done
                    return false;
                }
            }

            //            called to report a user click on an action item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.cab_one:
                        Log.i(TAG, "action item ONE clicked");
                        doSomethingWithActionOneItems();
                        // Action picked, so close the CAB
                        mode.finish();
                        return true;
                    case R.id.cab_two:
                        Log.i(TAG, "action item TWO clicked");
                        doSomethingWithActionTwoItems();
                        // Action picked, so close the CAB
                        mode.finish();
                        return true;
                    case R.id.cab_three:
                        Log.i(TAG, "clicked item three");
                        isAddFour = true;
                        isRemoveFour = false;
                        //invalidate the action mode and refresh the menu content
                        mode.invalidate();
                        return true;
                    case R.id.sub_one:
                        Log.i(TAG, "Clicked sub one");
                        // Action picked, so close the CAB
                        mode.finish();
                        return true;
                    case R.id.sub_two:
                        Log.i(TAG, "Clicked sub two");
                        // Action picked, so close the CAB
                        mode.finish();
                        return true;
                    case R.id.cab_four:
                        Log.i(TAG, "clicked item four");
                        isRemoveFour = true;
                        isAddFour = false;
//                      invalidate the action mode and refresh the menu content
                        mode.invalidate();
                        return true;
                    default:
                        return false;
                }
            }

            //called when an action mode is about to be exited and destroyed
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.i(TAG, "destroying action mode");
                isRemoveFour = false;
                isAddFour = false;
            }
        });
    }

    private void doSomethingWithActionTwoItems() {
        Log.i(TAG, "getting action two items");
        SparseBooleanArray checked = myListView.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i) == true) {
                String theSelectedCountry = (String) myListView
                        .getItemAtPosition(checked.keyAt(i));
                Log.i(TAG, "Selected country key: " + checked.keyAt(i) + " country: " + theSelectedCountry);
            }
        }
    }

    private void doSomethingWithActionOneItems() {
        Log.i(TAG, "getting action one items");
        myListView.getCheckedItemIds();
        SparseBooleanArray checked = myListView.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i) == true) {
                String theSelectedCountry = (String) myListView
                        .getItemAtPosition(checked.keyAt(i));
                Log.i(TAG, "Selected country key: " + checked.keyAt(i) + " country: " + theSelectedCountry);
            }
        }
    }
}
