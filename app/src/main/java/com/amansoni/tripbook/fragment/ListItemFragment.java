package com.amansoni.tripbook.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.activity.ItemPagerActivity;
import com.amansoni.tripbook.activity.LocationLookup;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.util.DividerItemDecoration;
import com.amansoni.tripbook.util.FloatingActionButton;
import com.amansoni.tripbook.util.ImageResizer;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;

public class ListItemFragment extends BaseFragment {
    private static final String TAG = "ListItemFragment";
    private ArrayList<TripBookItem> mTripBookItems;
    private boolean mSubtitleVisible;
    android.support.v4.widget.SwipeRefreshLayout mSwipeRefreshLayout;

    private String mItemType = TripBookItem.TYPE_TRIP;
    private RecyclerView mRecyclerView;
    private MultiSelector mMultiSelector = new MultiSelector();
    private ModalMultiSelectorCallback mMultiMode = new ModalMultiSelectorCallback(mMultiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.list_item, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.action_delete_item) {
                // Need to finish the action mode before doing the following,
                // not after. No idea why, but it crashes.
                actionMode.finish();
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_cancel_save_title)
                        .setMessage(R.string.dialog_cancel_save_message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                for (int i = mTripBookItems.size(); i >= 0; i--) {
                                    if (mMultiSelector.isSelected(i, 0)) {
                                        TripBookItem tripBookItem = mTripBookItems.get(i);
                                        new TripBookItemData(getActivity()).delete(tripBookItem);
                                        mTripBookItems.remove(i);
                                        mRecyclerView.getAdapter().notifyItemRemoved(i);
                                    }
                                }
                                mMultiSelector.clearSelections();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }

            if (menuItem.getItemId() == R.id.action_item_share) {
                // Need to finish the action mode before doing the following,
                // not after. No idea why, but it crashes.
                actionMode.finish();
                createShareIntent();
                mMultiSelector.clearSelections();
                return true;
            }
            return false;
        }
    };

    public static ListItemFragment newInstance(String itemType) {
        Bundle args = new Bundle();
        args.putString(TripBookItem.ITEM_TYPE, itemType);

        ListItemFragment fragment = new ListItemFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Create a sharable intent using the item being viewed.
     *
     * @return the shareIntent
     */
    protected Intent createShareIntent() {
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        String subject = "Please check out these " + mItemType + " details from TripBook";
        String extraText = "";

        for (int i = mTripBookItems.size(); i >= 0; i--) {
            if (mMultiSelector.isSelected(i, 0)) {
                TripBookItem tripBookItem = mTripBookItems.get(i);
                // add full list of image uris for all items
                for (String filepath : tripBookItem.getImages()) {
                    imageUris.add(Uri.parse(filepath)); // Add your image URIs here
                }
                extraText = extraText + tripBookItem.toString();
                // get the linked place information
                TripBookItemData linkedItemData = new TripBookItemData(getActivity());
                linkedItemData.setItemType(TripBookItem.TYPE_PLACE);
                linkedItemData.setItemId(tripBookItem.getId());
                ArrayList<TripBookItem> places = linkedItemData.getAllRows();
                if (places.size() > 0) {
                    extraText = extraText + "\nPlaces to visit";
                    for (TripBookItem place : places) {
                        extraText = extraText + "\n" + place.toString();
                    }
                }
                linkedItemData.setItemType(TripBookItem.TYPE_PLACE);
                ArrayList<TripBookItem> friends = linkedItemData.getAllRows();
                if (friends.size() > 0) {
                    extraText = extraText + "\nFriends coming with";
                    for (TripBookItem friend : friends) {
                        extraText = extraText + "\n" + friend.getTitle();
                    }
                }

            }
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("*/*");

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
//        shareIntent.putStringArrayListExtra(Intent.EXTRA_TEXT, extra_text);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, extraText);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

        startActivity(Intent.createChooser(shareIntent, "Share using..."));
        return shareIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            mItemType = args.getString(TripBookItem.ITEM_TYPE);
            Log.d(TAG, "Now showing" + mItemType);
        }

        getActivity().setTitle(mItemType);
        mSubtitleVisible = false;
    }

    /**
     * Note: since the fragment is retained. the bundle passed in after state is restored is null.
     * THe only way to pass parcelable objects is through the activities onsavedInstanceState and appropiate startup lifecycle
     * However after having second thoughts, since the fragment is retained then all the states and instance variables are
     * retained as well. no need to make the selection states percelable therefore just check for the selectionstate
     * from the multiselector
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        if (mMultiSelector != null) {
            Bundle bundle = savedInstanceState;
            if (bundle != null) {
                mMultiSelector.restoreSelectionStates(bundle.getBundle(TAG));
            }

            if (mMultiSelector.isSelectable()) {
                if (mMultiMode != null) {
                    mMultiMode.setClearOnPrepare(false);
                    ((ActionBarActivity) getActivity()).startSupportActionMode(mMultiMode);
                }

            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(TAG, mMultiSelector.saveSelectionStates());
        super.onSaveInstanceState(outState);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyclerview, parent, false);

        //TODO
        if (mSubtitleVisible) {
//            getActionBar().setSubtitle(R.string.subtitle);
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        TripBookItemData tripBookItemData = new TripBookItemData(getActivity());
        tripBookItemData.setItemType(mItemType);
        mTripBookItems = tripBookItemData.getAllRows();
        mRecyclerView.setAdapter(new ItemAdapter());

        FloatingActionButton mAddButton = (FloatingActionButton) v.findViewById(R.id.add_button);
        mAddButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new AddItemDialogFragment().show(getFragmentManager(), "addnew");
                    return true;
                }
                return true; // consume the event
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        return v;
    }
    void refreshItems() {
        // Load items
        TripBookItemData tripBookItemData = new TripBookItemData(getActivity());
        tripBookItemData.setItemType(mItemType);
        mTripBookItems = tripBookItemData.getAllRows();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void selectItem(TripBookItem tripBookItem) {
        // start an instance of ItemPagerActivity
        Intent i = new Intent(getActivity(), ItemPagerActivity.class);
        i.putExtra(TripBookItem.ITEM_ID, tripBookItem.getId());
        i.putExtra(TripBookItem.ITEM_TYPE, tripBookItem.getItemType());

        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        if (requestCode == GalleryFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {

                Intent intent = new Intent(getActivity(), LocationLookup.class);
                String filename = data.getExtras().get(MediaStore.EXTRA_OUTPUT).toString();
                intent.putExtra(LocationLookup.IMAGE_URI, filename);
                startActivity(intent);

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(getActivity(), "There was an error capturing the image", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        TODO
//        inflater.inflate(R.menu.fragment_crime_list, menu);
//        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
//        if (mSubtitleVisible && showSubtitle != null) {
//            showSubtitle.setTitle(R.string.hide_subtitle);
//        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==  R.id.menu_item_new_crime) {
//            final Crime crime = new Crime();
//            CrimeLab.get(getActivity()).addCrime(crime);
//
//            mRecyclerView.getAdapter().notifyItemInserted(mTripBookItems.indexOf(crime));
//
//            // NOTE: Left this code in for commentary. I believe this is what you would do
//            // to wait until the new crime is added, then animate the selection of the new crime.
//            // It does not work, though: the listener will be called immediately,
//            // because no animations have been queued yet.
////                mRecyclerView.getItemAnimator().isRunning(
////                        new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
////                    @Override
////                    public void onAnimationsFinished() {
////                        selectItem(crime);
////                    }
////                });
//            return true;
//        }
//          else if(item.getItemId()==R.id.menu_item_show_subtitle) {
//                ActionBar actionBar = getActionBar();
//                if (actionBar.getSubtitle() == null) {
//                    actionBar.setSubtitle(R.string.subtitle);
//                    mSubtitleVisible = true;
//                    item.setTitle(R.string.hide_subtitle);
//                } else {
//                    actionBar.setSubtitle(null);
//                    mSubtitleVisible = false;
//                    item.setTitle(R.string.show_subtitle);
//                }
//                return true;
//
//
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        TODO
//        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }


    private class ItemHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView mTitleTextView;
        private final TextView mDateStartTextView;
        private final TextView mDateEndTextView;
        private final ImageView mImageView;
        private TripBookItem mTripBookItem;

        public ItemHolder(View itemView) {
            super(itemView, mMultiSelector);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_row_title);
            mDateStartTextView = (TextView) itemView.findViewById(R.id.list_row_date_start);
            mDateEndTextView = (TextView) itemView.findViewById(R.id.list_row_date_end);
            mImageView = (ImageView) itemView.findViewById(R.id.list_row_imageView);

            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        public void bindItem(TripBookItem tripBookItem) {
            mTripBookItem = tripBookItem;
            mTitleTextView.setText(tripBookItem.getTitle());
            String s;
            if (tripBookItem.getEndDate() != null && tripBookItem.getEndDate().length() > 0)
                s = "From: " + tripBookItem.getCreatedAt() + " to " + tripBookItem.getEndDate();
            else {
                if (tripBookItem.getCreatedAt() != null && tripBookItem.getCreatedAt().length() > 0)
                    s = "Date: " + tripBookItem.getCreatedAt();
                else
                    s = "(unplanned)";
            }
            mDateStartTextView.setText(s);
            mImageView.setImageBitmap(ImageResizer.decodeSampledBitmapFromFile(tripBookItem.getThumbnail(), 250, 250));
        }

        @Override
        public void onClick(View v) {
            if (mTripBookItem == null) {
                return;
            }
            if (!mMultiSelector.tapSelection(this)) {
                selectItem(mTripBookItem);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            ((ActionBarActivity) getActivity()).startSupportActionMode(mMultiMode);
            mMultiSelector.setSelected(this, true);
            return true;
        }
    }


    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int pos) {
            TripBookItem tripBookItem = mTripBookItems.get(pos);
            holder.bindItem(tripBookItem);
            Log.d(TAG, "binding item" + tripBookItem + "at position" + pos);
        }

        @Override
        public int getItemCount() {
            return mTripBookItems.size();
        }
    }
}

