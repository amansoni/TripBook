package com.amansoni.tripbook.activity;

import android.app.Fragment;

import com.amansoni.tripbook.fragment.ListItemFragment;

public class ListItemActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListItemFragment();
    }
}
