package com.amansoni.tripbook.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.model.TripBookItemData;
import com.amansoni.tripbook.fragment.ItemViewFragment;
import com.amansoni.tripbook.model.TripBookItem;

import java.util.ArrayList;

public class ItemPagerActivity extends ActionBarActivity {
    android.support.v4.view.ViewPager mViewPager;

    public static ActivityOptions getTransition(Activity activity, View TripBookItemView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TripBookItemView.setTransitionName("TripBookItem");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                    TripBookItemView, "TripBookItem");

            return options;
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();


        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setTransitionName("TripBookItem");
        }
        setContentView(mViewPager);

        TripBookItemData tripBookItemData = new TripBookItemData(this);
        long itemId = getIntent().getExtras().getLong(TripBookItem.ITEM_ID);

        String itemType = tripBookItemData.getItem(itemId).getItemType();
        tripBookItemData.setItemType(itemType);
//        String itemType = getIntent().getExtras().getString(TripBookItem.ITEM_TYPE);

        final ArrayList<TripBookItem> tripBookItems = tripBookItemData.getAllRows();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new android.support.v4.app.FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return tripBookItems.size();
            }

            @Override
            public Fragment getItem(int pos) {
                long TripBookItemId = tripBookItems.get(pos).getId();
                return ItemViewFragment.newInstance(TripBookItemId);
            }
        });

        long TripBookItemId = getIntent().getExtras().getLong(TripBookItem.ITEM_ID);
        for (int i = 0; i < tripBookItems.size(); i++) {
            if (tripBookItems.get(i).getId() == TripBookItemId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
