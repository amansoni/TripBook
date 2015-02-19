package com.amansoni.tripbook.db;

/**
 * Created by Aman on 11/02/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.amansoni.tripbook.model.TripBookCommon;
import com.amansoni.tripbook.model.TripBookImage;

import java.util.ArrayList;
import java.util.List;

public class TripBookImageData extends TripBookCommonData {
    private static final String TAG = "TripBookImageData";
    private String[] allColumns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_IMAGE_PATH, DatabaseHelper.COLUMN_IMAGE_THUMBNAIL,
            DatabaseHelper.COLUMN_CREATED_AT};

    public TripBookImageData(Context context) {
        super(context);
    }

    public TripBookImage add(TripBookCommon tripBookCommon) {
        TripBookImage tripBookImage = (TripBookImage) tripBookCommon;
        String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME_IMAGES +
                "(" + DatabaseHelper.COLUMN_IMAGE_PATH + "," + DatabaseHelper.COLUMN_IMAGE_THUMBNAIL
                + ") VALUES(?,?)";
        open();
        SQLiteStatement insertStmt = database.compileStatement(sql);
//        insertStmt.clearBindings();
        insertStmt.bindString(1, tripBookImage.getFilePath());
        insertStmt.bindBlob(2, tripBookImage.getImage());
        tripBookImage.setId(insertStmt.executeInsert());
        close();

        return tripBookImage;
    }

    public TripBookCommon update(TripBookCommon tripBookCommon) {
        // probably would never update an image?
        return (TripBookImage) tripBookCommon;
    }

    public void delete(TripBookCommon tripBookCommon) {
        long id = tripBookCommon.getId();
        System.out.println("TripBookImage deleted with id: " + id);
        open();
        database.delete(DatabaseHelper.TABLE_NAME_IMAGES, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
        close();
    }

    public List<TripBookCommon> getAllRows() {
        List<TripBookCommon> list = new ArrayList<>();
        open();

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_IMAGES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TripBookImage TripBookImage = cursorToTripBookImage(cursor);
            list.add(TripBookImage);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return list;
    }

    private TripBookImage cursorToTripBookImage(Cursor cursor) {
        TripBookImage tripBookImage = new TripBookImage();
        tripBookImage.setId(cursor.getLong(0));
        tripBookImage.setFilePath(cursor.getString(1));
        tripBookImage.setImage(cursor.getBlob(2));
        tripBookImage.setCreatedAt(cursor.getString(3));
//        Log.d(TAG, "cursorToTripBookImage" + TripBookImage.toString());
        return tripBookImage;
    }

    public TripBookImage getItem(long id) {
        Log.d(TAG, "TripBookImage select by id: " + id);
        TripBookImage tripBookImage = null;
        open();

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_IMAGES,
                allColumns, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            Log.d(TAG, "TripBookImage found select by id: " + id);
            tripBookImage = cursorToTripBookImage(cursor);
        }
        // make sure to close the cursor
        cursor.close();
        close();
//        Log.d(TAG, "TripBookImage retrieved" + TripBookImage.toString());

        return tripBookImage;
    }

    public void createTestData() {
//        add(new TripBookImage("Birmingham 2015", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("Lake District", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("Peak District Camping", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("London Attractions", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("Birmingham 2014", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("South Africa Summer 2014", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("London SuperTechies", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("Cheltenham Weekend away", TripBookImage.TYPE_TRIP));
//        add(new TripBookImage("Cannon Hill Park", TripBookImage.TYPE_TRIP));
    }
}
