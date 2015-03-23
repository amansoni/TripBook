package com.amansoni.tripbook.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.amansoni.tripbook.fragment.GalleryFragment;
import com.amansoni.tripbook.fragment.ListItemFragment;

public class ListItemActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListItemFragment();
    }

}
