package com.amansoni.tripbook.activity;

/**
 * Created by Aman on 12/03/2015.
 */
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.amansoni.tripbook.MainActivity;
import com.amansoni.tripbook.provider.TripBookProvider;
import com.amansoni.tripbook.model.DatabaseHelper;
import com.amansoni.tripbook.model.TripBookItem;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchableActivity extends ListActivity {
    private final String TAG = "SearchableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        // implement Up Navigation with caret in front of App icon in the Action Bar
//TODO        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        checkIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        Log.d(TAG, "onNewIntent");

        // update the activity launch intent
        setIntent(newIntent);
        // handle it
        checkIntent(newIntent);
    }

    private void checkIntent(Intent intent) {
        String query = "";
        String intentAction = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(intentAction)) {
            query = intent.getStringExtra(SearchManager.QUERY);
            fillList(query);
        } else if (Intent.ACTION_VIEW.equals(intentAction)) {

            Uri details = intent.getData();
            Log.d(TAG, "ViewIntent" + details.toString());

//            Intent detailsIntent = new Intent(Intent.ACTION_VIEW, details);
//            startActivity(detailsIntent);

            // set the db item key and create the view for a single item
            Bundle args = new Bundle();
            args.putLong("itemKey", 1);
            args.putString("itemType", TripBookItem.TYPE_TRIP);
            Intent tripIntent = new Intent(this, AddItemActivity.class);
            tripIntent.putExtras(args);
            startActivity(tripIntent);
        }
//        fillList(query);
    }

    private void fillList(String query) {

        String wildcardQuery = "%" + query + "%";

        Log.d(TAG, "query:" + query);
        Log.d(TAG, "provider:" + TripBookProvider.CONTENT_URI_SEARCH);
        Cursor cursor = getContentResolver().query(
                TripBookProvider.CONTENT_URI_SEARCH,
                null,
                DatabaseHelper.COLUMN_ITEM_TITLE + " LIKE ? OR " + DatabaseHelper.COLUMN_ITEM_TITLE + " LIKE ?",
                new String[] { wildcardQuery, wildcardQuery },
                null);

        Log.d(TAG, "query:" + query);

        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] { DatabaseHelper.COLUMN_ITEM_TITLE, DatabaseHelper.COLUMN_ITEM_TITLE },
                new int[] { android.R.id.text1, android.R.id.text2 },
                0);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View view, int position, long id) {

//        Uri details = Uri.withAppendedPath(EmployeeProvider.CONTENT_URI, "" + id);
        Bundle args = new Bundle();
        args.putLong("itemKey", id);
        Intent detailsIntent = new Intent(this, AddItemActivity.class);
        detailsIntent.putExtras(args);
        startActivity(detailsIntent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            // This is called when the Home (Up) button is pressed
            // in the Action Bar.
            Intent parentActivityIntent = new Intent(this, MainActivity.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}