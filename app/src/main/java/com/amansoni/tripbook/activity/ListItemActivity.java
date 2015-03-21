package com.amansoni.tripbook.activity;

import android.support.v4.app.Fragment;

import com.amansoni.tripbook.fragment.ListItemFragment;

public class ListItemActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListItemFragment();
    }
}
