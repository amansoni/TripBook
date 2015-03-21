package com.amansoni.tripbook.map;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.amansoni.tripbook.activity.SingleFragmentActivity;

/**
 * Created by beaumoaj on 11/02/15.
 */
public class PlaceDetailActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected Fragment createFragment() {
        GooglePlace place = (GooglePlace) getIntent().getSerializableExtra("PLACE");
        return PlaceDetailFragment.newInstance(place);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Get the intent that navigateUpFromSameTask would send out
                Intent i = NavUtils.getParentActivityIntent(this);
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // Fire the intent as navigateUpFromSameTask would
                    NavUtils.navigateUpTo(this, i);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}