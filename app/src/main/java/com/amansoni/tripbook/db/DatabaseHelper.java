package com.amansoni.tripbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amansoni.tripbook.MainActivity;

/**
 * Created by Aman on 11/02/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;     // instance for singleton pattern
    private Context mContext;
    private static final String LOG = "DatabaseHelper"; // Logcat tag
    private static final int DATABASE_VERSION = 1; // Database Version
    private static final String DATABASE_NAME = "tripBook"; // Database Name

    // Table Names
    public static final String TABLE_NAME_ITEM = "tb_item";
    public static final String TABLE_NAME_IMAGES = "tb_images";
    public static final String TABLE_NAME_EXTRA = "tb_extra";
    public static final String TABLE_NAME_LOCATION = "tb_location";
    public static final String TABLE_NAME_LINKS = "tb_links";

    // Common column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";

    // TABLE_NAME_ITEM column names
    public static final String COLUMN_ITEM_TITLE = "title";
    public static final String COLUMN_ITEM_DESCRIPTION = "description";
    public static final String COLUMN_ITEM_TYPE = "item_type";
    public static final String COLUMN_ITEM_STARRED = "starred";
    public static final String COLUMN_END_DATE = "end_date";

    // TABLE_NAME_IMAGES column names
    public static final String COLUMN_IMAGE_THUMBNAIL = "thumbnail";
    public static final String COLUMN_IMAGE_PATH = "file_path";

    // TABLE_NAME_EXTRA column names
    public static final String COLUMN_EXTRA_NAME = "extra_name";
    public static final String COLUMN_EXTRA_VALUE = "extra_value";

    // TABLE_NAME_LOCATION column names
    public static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    public static final String COLUMN_LOCATION_LATITUDE = "latitude";

    // TABLE_NAME_LINKS column names
    public static final String COLUMN_LINKS_PARENTID = "parent_id";
    public static final String COLUMN_LINKS_CHILDID = "child_id";

    // Table Create Statements
    private static final String CREATE_TABLE_TBITEMS = "CREATE TABLE " + TABLE_NAME_ITEM
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + COLUMN_ITEM_TITLE + " TEXT,"
            + COLUMN_ITEM_TYPE + " TEXT,"
            + COLUMN_ITEM_DESCRIPTION + " TEXT,"
            + COLUMN_ITEM_STARRED + " INTEGER,"
            + COLUMN_CREATED_AT + " DATETIME,"
            + COLUMN_END_DATE + " DATETIME" + ")";

    private static final String CREATE_TABLE_TBIMAGES = "CREATE TABLE " + TABLE_NAME_IMAGES
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + COLUMN_IMAGE_PATH + " TEXT,"
            + COLUMN_IMAGE_THUMBNAIL + " BLOB,"
            + COLUMN_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TBEXTRA = "CREATE TABLE " + TABLE_NAME_EXTRA
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + COLUMN_EXTRA_NAME + " TEXT,"
            + COLUMN_EXTRA_VALUE + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TBLOCATION = "CREATE TABLE " + TABLE_NAME_LOCATION
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + COLUMN_LOCATION_LATITUDE + " REAL,"
            + COLUMN_LOCATION_LONGITUDE + " REAL,"
            + COLUMN_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TBLINKS = "CREATE TABLE " + TABLE_NAME_LINKS
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + COLUMN_LINKS_PARENTID + " INTEGER,"
            + COLUMN_LINKS_CHILDID + " INTEGER,"
            + COLUMN_ITEM_TYPE + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME" + ")";

    /**
     * use the application context as suggested by CommonsWare.
     * this will ensure that you dont accidentally leak an Activitys
     * context (see this article for more information:
     * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
     */
    public static DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(MainActivity.getContext().getApplicationContext());
        }
        return mInstance;
    }

    /**
     * constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_TBITEMS);
        db.execSQL(CREATE_TABLE_TBEXTRA);
        db.execSQL(CREATE_TABLE_TBIMAGES);
        db.execSQL(CREATE_TABLE_TBLINKS);
        db.execSQL(CREATE_TABLE_TBLOCATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TBITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TBEXTRA);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TBIMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TBLINKS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TBLOCATION);
        // create new tables
        onCreate(db);
    }
}
