package com.amansoni.tripbook.fragment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.amansoni.tripbook.R;
import com.amansoni.tripbook.activity.ItemPagerActivity;
import com.amansoni.tripbook.db.TripBookItemData;
import com.amansoni.tripbook.model.TripBookItem;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;

public class ListItemFragment extends BaseFragment {
    private static final String TAG = "ListItemFragment";
    private RecyclerView mRecyclerView;
    private MultiSelector mMultiSelector = new MultiSelector();
    private ModalMultiSelectorCallback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.list_item, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId()==  R.id.action_delete_item){
                    // Need to finish the action mode before doing the following,
                    // not after. No idea why, but it crashes.
                    actionMode.finish();

                    for (int i = mTripBookItems.size(); i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
//                            Crime crime = mTripBookItems.get(i);
//                            CrimeLab.get(getActivity()).deleteCrime(crime);
                            mRecyclerView.getAdapter().notifyItemRemoved(i);
                        }
                    }

                    mMultiSelector.clearSelections();
                    return true;

            }
            return false;
        }
    };
    private ArrayList<TripBookItem> mTripBookItems;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.app_name);
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
                if (mDeleteMode != null) {
                    mDeleteMode.setClearOnPrepare(false);
                    ((ActionBarActivity) getActivity()).startSupportActionMode(mDeleteMode);
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
        mTripBookItems = new TripBookItemData(getActivity()).getAllRows();
        mRecyclerView.setAdapter(new ItemAdapter());

        return v;
    }

    private void selectItem(TripBookItem tripBookItem) {
        // start an instance of CrimePagerActivity
        Intent i = new Intent(getActivity(), ItemPagerActivity.class);
        i.putExtra(TripBookItem.ITEM_ID, tripBookItem.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // NOTE: shared element transition here.
            // Support library fragments do not support the three parameter
            // startActivityForResult call. So to get this to work, the entire
            // project had to be shifted over to use stdlib fragments,
            // and the v13 ViewPager.
            int index = mTripBookItems.indexOf(tripBookItem);
            ItemHolder holder = (ItemHolder) mRecyclerView
                    .findViewHolderForPosition(index);

            ActivityOptions options = ItemPagerActivity.getTransition(
                    getActivity(), holder.itemView);
//TODO
//            startActivityForResult(i, 0, options.toBundle());
            startActivityForResult(i, 0);
        } else {
            startActivityForResult(i, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRecyclerView.getAdapter().notifyDataSetChanged();
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
        private final TextView mDateTextView;
//        private final CheckBox mSolvedCheckBox;
        private TripBookItem mTripBookItem;

        public ItemHolder(View itemView) {
            super(itemView, mMultiSelector);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_row_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_row_date);
//            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.crime_list_item_solvedCheckBox);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        public void bindCrime(TripBookItem tripBookItem) {
            mTripBookItem = tripBookItem;
            mTitleTextView.setText(tripBookItem.getTitle());
            mDateTextView.setText(tripBookItem.getCreatedAt().toString());
//            mSolvedCheckBox.setChecked(tripBookItem.isSolved());
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

            ((ActionBarActivity) getActivity()).startSupportActionMode(mDeleteMode);
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
            holder.bindCrime(tripBookItem);
            Log.d(TAG, "binding item" + tripBookItem + "at position" + pos);
        }

        @Override
        public int getItemCount() {
            return mTripBookItems.size();
        }
    }
}

