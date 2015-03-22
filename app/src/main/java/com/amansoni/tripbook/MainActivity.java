package com.amansoni.tripbook;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.amansoni.tripbook.activity.FacebookActivity;
import com.amansoni.tripbook.activity.ListItemActivity;
import com.amansoni.tripbook.activity.MapsActivity;
import com.amansoni.tripbook.activity.SettingsActivity;
import com.amansoni.tripbook.fragment.ListItemFragment;
import com.amansoni.tripbook.fragment.NavigationDrawerFragment;
import com.amansoni.tripbook.images.GalleryFragment;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.model.TripBookItemData;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static Context mContext;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // first check for activities that are not fragments


        if (position == 6) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (position == 5) {
            mTitle = getString(R.string.title_map);
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (position == 4) {
            mTitle = getString(R.string.title_gallery);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new GalleryFragment())
                    .commit();
        } else {
            // display the ListItemFragment for the appropriate itemtype
            Bundle args = new Bundle();
            String itemType = null;
            switch (position) {
                case 0:
                    mTitle = getString(R.string.title_trip);
                    itemType = TripBookItem.TYPE_TRIP;
                    break;
                case 1:
                    mTitle = getString(R.string.title_place);
                    itemType = TripBookItem.TYPE_PLACE;
                    TripBookItemData test = new TripBookItemData(this);
                    if (test.getAllRows().size() == 0) {
                        test.createTestData(this);
                    }
                    break;
                case 2:
                    Intent facebookIntent = new Intent(this, FacebookActivity.class);
                    startActivity(facebookIntent);
//                    mTitle = getString(R.string.title_social);
//                    itemType = TripBookItem.TYPE_FRIENDS;
                    break;
                case 3:
                    mTitle = getString(R.string.title_starred);
                    itemType = TripBookItem.TYPE_STARRED;
                    break;
            }
            if (position != 2) {
                // set the list to display
                Fragment viewNow = ListItemFragment.newInstance(itemType);
//                args.putString(TripBookItem.ITEM_TYPE, itemType);
//                viewNow.setArguments(args);
                // update the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, viewNow)
                        .commit();
            }
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_trip);
                break;
            case 1:
                mTitle = getString(R.string.title_place);
                break;
            case 2:
                mTitle = getString(R.string.title_social);
                break;
            case 3:
                mTitle = getString(R.string.title_starred);
                break;
            case 4:
                mTitle = getString(R.string.title_gallery);
                break;
            case 5:
                mTitle = getString(R.string.title_map);
                break;
            case 6:
                mTitle = getString(R.string.title_settings);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
//            Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mContext = getApplicationContext();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
