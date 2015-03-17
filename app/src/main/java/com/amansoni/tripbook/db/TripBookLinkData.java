package com.amansoni.tripbook.db;

/**
 * Created by Aman on 11/02/2015.
 */

import android.content.ContentValues;
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
        values.put(DatabaseHelper.COLUMN_ITEM_TYPE, ((TripBookItem) tripBookLink.getChild()).getItemType());
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

    public void removeLinks(TripBookCommon tripBookCommon) {
        long id = tripBookCommon.getId();
        System.out.println("TripBookLink data deleted for item id: " + id);
        final String sql = "DELETE FROM " + DatabaseHelper.TABLE_NAME_LINKS
                + " WHERE " + DatabaseHelper.COLUMN_LINKS_PARENTID + " = ?";
        open();
        database.execSQL(sql, new String[]{String.valueOf(id)});
        close();
    }

}
