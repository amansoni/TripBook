package com.amansoni.tripbook.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.amansoni.tripbook.model.TripBookCommon;

import java.util.List;

/**
 * Created by Aman on 15/02/2015.
 */
public abstract class TripBookAbstractData {
    // Database fields
    protected SQLiteDatabase database;
    protected DatabaseHelper dbHelper;

    public TripBookAbstractData() {
        dbHelper = DatabaseHelper.getInstance();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public abstract TripBookCommon add(TripBookCommon item);
    public abstract void delete(TripBookCommon item);
    public abstract List<TripBookCommon> getAllRows();
    public abstract TripBookCommon update(TripBookCommon item);

}