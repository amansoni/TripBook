package com.amansoni.tripbook.db;

/**
 * Created by Aman on 11/02/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookImage;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.model.TripBookLink;

import java.util.ArrayList;
import java.util.List;

public class TripBookItemData extends TripBookCommonData {
    private static final String TAG = "TripBookItemData";
    private String mItemType = null;
    private String[] allColumns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_ITEM_TITLE, DatabaseHelper.COLUMN_ITEM_TYPE,
            DatabaseHelper.COLUMN_CREATED_AT};

    public TripBookItemData(Context context) {
        super(context);
    }

    /**
     * Use this constructor for the DataAdaptor to only return lists of a particular itemType
     *
     * @param context
     * @param itemType
     */
    public TripBookItemData(Context context, String itemType) {
        this(context);
        mItemType = itemType;
    }

    public TripBookItem add(TripBookCommon tripBookCommon) {
        TripBookItem tripBookItem = (TripBookItem) tripBookCommon;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_TITLE, tripBookItem.getTitle());
        values.put(DatabaseHelper.COLUMN_ITEM_TYPE, tripBookItem.getItemType());
        values.put(DatabaseHelper.COLUMN_CREATED_AT, tripBookItem.getCreatedAt());
        open();
        long insertId = database.insert(DatabaseHelper.TABLE_NAME_ITEM, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_ITEM,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TripBookItem TripBookItem = cursorToTripBookItem(cursor);
        cursor.close();
        close();
        return TripBookItem;
    }

    public TripBookCommon update(TripBookCommon tripBookCommon) {
        TripBookItem tripBookItem = (TripBookItem) tripBookCommon;
        // save images
        TripBookImageData tripBookImageData = new TripBookImageData(mContext);
        TripBookLinkData tripBookLinkData = new TripBookLinkData(mContext);
        for (TripBookImage image : ((TripBookItem)tripBookCommon).getTripBookImages()) {
            TripBookImage tripBookImage = tripBookImageData.add(image);
            TripBookLink tripBookLink = new TripBookLink(tripBookCommon, tripBookImage);
            tripBookLinkData.add(tripBookLink);
        }
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_TITLE, tripBookItem.getTitle());
        values.put(DatabaseHelper.COLUMN_ITEM_TYPE, tripBookItem.getItemType());
        open();
        long updateId = database.update(DatabaseHelper.TABLE_NAME_ITEM, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(tripBookItem.getId())});
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_ITEM,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + updateId, null,
                null, null, null);
        cursor.moveToFirst();
        tripBookItem = cursorToTripBookItem(cursor);
        cursor.close();
        close();
        return tripBookItem;
    }

    public void delete(TripBookCommon tripBookCommon) {
        long id = tripBookCommon.getId();
        System.out.println("TripBookItem deleted with id: " + id);
        open();
        database.delete(DatabaseHelper.TABLE_NAME_ITEM, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
        close();
    }

    public List<TripBookCommon> getAllRows() {
        List<TripBookCommon> list = new ArrayList<>();
        open();

        Cursor cursor = null;
        if (mItemType == null) {
            cursor = database.query(DatabaseHelper.TABLE_NAME_ITEM,
                    allColumns, null, null, null, null, null);
        } else {
            cursor = database.query(DatabaseHelper.TABLE_NAME_ITEM,
                    allColumns, DatabaseHelper.COLUMN_ITEM_TYPE + " = ?", new String[]{mItemType}, null, null, null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TripBookItem TripBookItem = cursorToTripBookItem(cursor);
            list.add(TripBookItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return list;
    }

    private TripBookItem cursorToTripBookItem(Cursor cursor) {
        TripBookItem TripBookItem = new TripBookItem();
        TripBookItem.setId(cursor.getLong(0));
        TripBookItem.setTitle(cursor.getString(1));
        TripBookItem.setItemType(cursor.getString(2));
        TripBookItem.setCreatedAt(cursor.getString(3));
//        Log.d(TAG, "cursorToTripBookItem" + TripBookItem.toString());
        return TripBookItem;
    }

    public TripBookItem getItem(long id) {
        Log.d(TAG, "TripBookItem select by id: " + id);
        TripBookItem tripBookItem = null;
        open();

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_ITEM,
                allColumns, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            Log.d(TAG, "TripBookItem found select by id: " + id);
            tripBookItem = cursorToTripBookItem(cursor);
        }
        // make sure to close the cursor
        cursor.close();
        close();
//        List<TripBookImage> tripBookImages = new ArrayList<>();
//
//        tripBookImages.add(new TripBookImageData(mContext).getItem(1));
//        tripBookItem.setTripBookImages(tripBookImages);
//        Log.d(TAG, "TripBookItem retrieved" + TripBookItem.toString());

        return tripBookItem;
    }

    public void createTestData() {
        add(new TripBookItem("Birmingham 2015", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Lake District", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Peak District Camping", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("London Attractions", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Birmingham 2014", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("South Africa Summer 2014", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("London SuperTechies", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Cheltenham Weekend away", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Cannon Hill Park", TripBookItem.TYPE_TRIP));
    }
}
//    add("test 1 ", "desc 1", null, null);
//    add("test 2 ", "desc 2", null, null);
//    add("test 3 ", "desc 3", null, null);
//    add("test 4 ", "desc 4", null, null);
//    add("test 11 ", "desc 1", null, null);
//    add("test 12 ", "desc 2", null, null);
//    add("test 13 ", "desc 3", null, null);
//    add("test 14 ", "desc 4", null, null);
//    add("test 21 ", "desc 1", null, null);
//    add("test 22 ", "desc 2", null, null);
//    add("test 23 ", "desc 3", null, null);
//    add("test 24 ", "desc 4", null, null);
//    add("test 31 ", "desc 1", null, null);
//    add("test 32 ", "desc 2", null, null);
//    add("test 33 ", "desc 3", null, null);
//    add("test 34 ", "desc 4", null, null);
//    add("test 41 ", "desc 1", null, null);
//    add("test 42 ", "desc 2", null, null);
//    add("test 43 ", "desc 3", null, null);
//    add("test 44 ", "desc 4", null, null);
