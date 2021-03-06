package com.amansoni.tripbook.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.amansoni.tripbook.model.DatabaseHelper;

import java.util.HashMap;

/**
 * Created by Aman on 18/03/2015.
 */
public class TripBookProvider extends ContentProvider {
    // content mime types
    public static final String BASE_DATA_NAME = "tripbookitems";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.tripbook.search." + BASE_DATA_NAME;
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.tripbook.search." + BASE_DATA_NAME;
    public static final Uri CONTENT_URI_SEARCH =
            Uri.parse("content://com.amansoni.tripbook.provider.TripBookProvider/tripbookitems");
    private static final String AUTHORITY = "com.amansoni.tripbook.provider.TripBookProvider";

    // matcher
    private static final int ITEMS = 1; // The incoming URI matches the main table URI pattern
    private static final int ITEM_ID = 2; // The incoming URI matches the main table row ID URI pattern
    private static final int SEARCH_SUGGEST = 3;
    private static final HashMap<String, String> SEARCH_SUGGEST_PROJECTION_MAP;

    static {
        SEARCH_SUGGEST_PROJECTION_MAP = new HashMap<String, String>();
        SEARCH_SUGGEST_PROJECTION_MAP.put(DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_ID);
        SEARCH_SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, DatabaseHelper.COLUMN_ITEM_TITLE + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        SEARCH_SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_2, DatabaseHelper.COLUMN_ITEM_DESCRIPTION + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_2);
        SEARCH_SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, DatabaseHelper.COLUMN_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
    }

    // Uri matcher to decode incoming URIs.
    private final UriMatcher mUriMatcher;
    DatabaseHelper mDatabaseHelper;

    public TripBookProvider() {
        // Create and initialize URI matcher.
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_NAME_ITEM, ITEMS);
        mUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_NAME_ITEM + "/#", ITEM_ID);

        // to get suggestions...
        mUriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        mUriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseHelper.TABLE_NAME_ITEM);

        switch (mUriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                selectionArgs = new String[]{"%" + selectionArgs[0] + "%", "%" + selectionArgs[0] + "%"};
                queryBuilder.setProjectionMap(SEARCH_SUGGEST_PROJECTION_MAP);
                break;
            case ITEMS:
                // no filter
                break;
            case ITEM_ID:
                queryBuilder.appendWhere(DatabaseHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                Log.d("Provider", uri.toString());
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDatabaseHelper.getReadableDatabase(), projection,
                selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case ITEMS:
                return CONTENT_TYPE;
            case ITEM_ID:
                return CONTENT_ITEM_TYPE;
            case SEARCH_SUGGEST:
                return null;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}