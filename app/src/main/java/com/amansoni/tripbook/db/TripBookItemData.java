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

public class TripBookItemData extends TripBookAbstractData {
    private static final String TAG = "TripBookItemData";
    private String mItemType = null;
    private String[] allColumns = {DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_ITEM_TITLE, DatabaseHelper.COLUMN_ITEM_TYPE,
            DatabaseHelper.COLUMN_ITEM_DESCRIPTION,
            DatabaseHelper.COLUMN_ITEM_STARRED,
            DatabaseHelper.COLUMN_END_DATE, DatabaseHelper.COLUMN_CREATED_AT};
    private List<TripBookCommon> mList = null;
    public TripBookItemData() {
        super();
    }

    /**
     * Use this constructor for the DataAdaptor to only return lists of a particular itemType
     *
     * @param itemType
     */
    public TripBookItemData(String itemType) {
        this();
        mItemType = itemType;
    }

    public TripBookItem add(TripBookCommon tripBookCommon) {
        TripBookItem tripBookItem = (TripBookItem) tripBookCommon;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_TITLE, tripBookItem.getTitle());
        values.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, tripBookItem.getDescription());
        values.put(DatabaseHelper.COLUMN_ITEM_TYPE, tripBookItem.getItemType());
        values.put(DatabaseHelper.COLUMN_ITEM_STARRED, tripBookItem.isStarred());
        values.put(DatabaseHelper.COLUMN_END_DATE, tripBookItem.getEndDate().toString());
        values.put(DatabaseHelper.COLUMN_CREATED_AT, tripBookItem.getCreatedAt());
        open();
        long insertId = database.insert(DatabaseHelper.TABLE_NAME_ITEM, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_ITEM,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TripBookItem savedItem = cursorToTripBookItem(cursor);
        if (tripBookCommon.getLocation() != null){
            //todo save the location
        }
        cursor.close();
        close();
        mList = null;
        return savedItem;
    }

    public TripBookCommon update(TripBookCommon tripBookCommon) {
        TripBookItem tripBookItem = (TripBookItem) tripBookCommon;
        // save images
        TripBookImageData tripBookImageData = new TripBookImageData();
        TripBookLinkData tripBookLinkData = new TripBookLinkData();
        for (TripBookImage image : ((TripBookItem)tripBookCommon).getTripBookImages()) {
            TripBookImage tripBookImage = tripBookImageData.add(image);
            TripBookLink tripBookLink = new TripBookLink(tripBookCommon, tripBookImage);
            tripBookLinkData.add(tripBookLink);
        }
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_TITLE, tripBookItem.getTitle());
        values.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, tripBookItem.getDescription());
        values.put(DatabaseHelper.COLUMN_ITEM_TYPE, tripBookItem.getItemType());
        values.put(DatabaseHelper.COLUMN_ITEM_STARRED, tripBookItem.isStarred());
        open();
        long updateId = database.update(DatabaseHelper.TABLE_NAME_ITEM, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(tripBookItem.getId())});
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_ITEM,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + updateId, null,
                null, null, null);
        cursor.moveToFirst();
        tripBookItem = cursorToTripBookItem(cursor);
        cursor.close();
        close();
        mList = null;
        return tripBookItem;
    }

    public void delete(TripBookCommon tripBookCommon) {
        long id = tripBookCommon.getId();
        System.out.println("TripBookItem deleted with id: " + id);
        open();
        database.delete(DatabaseHelper.TABLE_NAME_ITEM, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
        close();
        mList.remove(tripBookCommon);
    }

    public List<TripBookCommon> getAllRows() {
        if (mList == null) {
            mList = new ArrayList<>();
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
                mList.add(TripBookItem);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            close();
        }
        return mList;
    }

    private TripBookItem cursorToTripBookItem(Cursor cursor) {
        TripBookItem TripBookItem = new TripBookItem();
        TripBookItem.setId(cursor.getLong(0));
        TripBookItem.setTitle(cursor.getString(1));
        TripBookItem.setItemType(cursor.getString(2));
        TripBookItem.setDescription(cursor.getString(3));
        TripBookItem.setStarred((cursor.getInt(4) == 0 ? false : true));
        TripBookItem.setEndDate(cursor.getString(6));
        TripBookItem.setCreatedAt(cursor.getString(6));
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
        // create trips
        TripBookItem loader =  add(new TripBookItem("Birmingham 2015", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Vicki", TripBookItem.TYPE_FRIENDS, true));
        add(new TripBookItem("Farhaan", TripBookItem.TYPE_FRIENDS));
        add(new TripBookItem("Hugh", TripBookItem.TYPE_FRIENDS));
        add(new TripBookItem("Joshua", TripBookItem.TYPE_FRIENDS));
        add(new TripBookItem("Ryan", TripBookItem.TYPE_FRIENDS));
        add(new TripBookItem("Marcus", TripBookItem.TYPE_FRIENDS));
        add(new TripBookItem("Sonya", TripBookItem.TYPE_FRIENDS, true));

        add(new TripBookItem("Lake District", TripBookItem.TYPE_TRIP, true));
        add(new TripBookItem("Peak District Camping", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("London Attractions", TripBookItem.TYPE_TRIP, true));
        add(new TripBookItem("Birmingham 2014", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("South Africa Summer 2014", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("London SuperTechies", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Cheltenham Weekend away", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Cannon Hill Park", TripBookItem.TYPE_TRIP));

        // create places for trips
//        add(new TripBookItem("Birmingham 2015", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Bullring", TripBookItem.TYPE_PLACE));
        add(new TripBookItem("City Library", TripBookItem.TYPE_PLACE, true));
        add(new TripBookItem("Chinese Quarter", TripBookItem.TYPE_PLACE));
        add(new TripBookItem("Jewellery Quarter", TripBookItem.TYPE_PLACE));
//        add(new TripBookItem("Lake District", TripBookItem.TYPE_TRIP));
//        add(new TripBookItem("Peak District Camping", TripBookItem.TYPE_TRIP));
//        add(new TripBookItem("London Attractions", TripBookItem.TYPE_TRIP));
//        add(new TripBookItem("Birmingham 2014", TripBookItem.TYPE_TRIP));
        add(new TripBookItem("Bullring", TripBookItem.TYPE_PLACE));
        add(new TripBookItem("Sealife Centre", TripBookItem.TYPE_PLACE));
        add(new TripBookItem("The Mailbox", TripBookItem.TYPE_PLACE));
//        add(new TripBookItem("South Africa Summer 2014", TripBookItem.TYPE_TRIP));
//        add(new TripBookItem("London SuperTechies", TripBookItem.TYPE_TRIP));
//        add(new TripBookItem("Cheltenham Weekend away", TripBookItem.TYPE_TRIP));
//        add(new TripBookItem("Cannon Hill Park", TripBookItem.TYPE_TRIP));

    }
}
