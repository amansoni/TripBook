package com.amansoni.tripbook.db;

/**
 * Created by Aman on 11/02/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookItem;
import com.amansoni.tripbook.model.TripBookLink;

import java.util.ArrayList;
import java.util.List;

public class TripBookLinkData extends TripBookAbstractData {
    private static final String TAG = "TripBookLinkData";
    private Long mParentId;
    private String[] allColumns = {DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_LINKS_PARENTID,
            DatabaseHelper.COLUMN_LINKS_CHILDID,
            DatabaseHelper.COLUMN_ITEM_TYPE,
            DatabaseHelper.COLUMN_CREATED_AT};

    public TripBookLinkData() {
        super();
    }

    /**
     * Use this constructor for the DataAdaptor to only return lists of a particular itemType
     *
     * @param parentId
     */
    public TripBookLinkData(Long parentId) {
        this();
        mParentId = parentId;
    }

    public TripBookCommon add(TripBookCommon tripBookCommon) {
        TripBookLink tripBookLink = (TripBookLink) tripBookCommon;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LINKS_PARENTID, tripBookLink.getParent().getId());
        values.put(DatabaseHelper.COLUMN_LINKS_CHILDID, tripBookLink.getChild().getId());
        values.put(DatabaseHelper.COLUMN_ITEM_TYPE, ((TripBookItem)tripBookLink.getChild()).getItemType());
        open();
        long insertId = database.insert(DatabaseHelper.TABLE_NAME_LINKS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_LINKS,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        tripBookLink = cursorToTripBookLink(cursor);
        cursor.close();
        close();
        return tripBookLink;
    }

    public TripBookCommon update(TripBookCommon tripBookCommon) {
        return new TripBookLink();
    }

    public void delete(TripBookCommon tripBookCommon) {
        long id = tripBookCommon.getId();
        System.out.println("TripBookLink deleted with id: " + id);
        open();
        database.delete(DatabaseHelper.TABLE_NAME_LINKS, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
        close();
    }

    public List<TripBookCommon> getAllRows() {
        List<TripBookCommon> list = new ArrayList<>();
        open();

        Cursor cursor = null;
        if (mParentId == null) {
            cursor = database.query(DatabaseHelper.TABLE_NAME_LINKS,
                    allColumns, null, null, null, null, null);
        } else {
            cursor = database.query(DatabaseHelper.TABLE_NAME_LINKS,
                    allColumns, DatabaseHelper.COLUMN_LINKS_PARENTID + " = ?", new String[]{mParentId.toString()}, null, null, null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TripBookLink tripBookLink = cursorToTripBookLink(cursor);
            list.add(tripBookLink);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return list;
    }

    private TripBookLink cursorToTripBookLink(Cursor cursor) {
        TripBookItemData tripBookItemData = new TripBookItemData();
        TripBookLink tripBookLink = new TripBookLink();
        tripBookLink.setId(cursor.getLong(0));
        tripBookLink.setParent(tripBookItemData.getItem(cursor.getLong(1)));
        tripBookLink.setChild(tripBookItemData.getItem(cursor.getLong(2)));
        tripBookLink.setCreatedAt(cursor.getString(3));
        return tripBookLink;
    }

    public TripBookCommon getItem(long id) {
        Log.d(TAG, "TripBookItem select by id: " + id);
        TripBookLink tripBookLink = null;
        open();

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_LINKS,
                allColumns, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            Log.d(TAG, "TripBookItem found select by id: " + id);
            tripBookLink = cursorToTripBookLink(cursor);
        }
        // make sure to close the cursor
        cursor.close();
        close();
//        Log.d(TAG, "TripBookItem retrieved" + TripBookItem.toString());

        return tripBookLink;
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
