package com.example.ssamz.storyalbum.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ssamz on 2017-01-15.
 */

public class DbHelper extends SQLiteOpenHelper {
    static final String TAG = DbHelper.class.getSimpleName();

    static final String DB_TABLE_STORY = "TABLE_STORY";
    static final String DB_ITEMS_PATH = "PATH";
    static final String DB_TITLE = "TITLE";
    static final String DB_MEMO = "MEMO";
    static final String DB_DATE = "DATE";
    static final String DB_CITY = "CITY";

    static final String DB_TABLE_URLS = "TABLE_URLS";
    static final String DB_URL = "URL";

    static final int DB_VERSION = 11;

    private static final String TABLE_CREATE_STORYDATA_QUERY =
            "CREATE TABLE " + DB_TABLE_STORY + " ( " +
                    DB_ITEMS_PATH + " TEXT PRIMARY KEY, " +
                    DB_TITLE + " TEXT, " +
                    DB_MEMO + " TEXT, " +
                    DB_DATE + " TEXT ) ";

    private static final String TABLE_CREATE_URLS_QUERY =
            "CREATE TABLE " + DB_TABLE_URLS + " ( " +
                    DB_ITEMS_PATH + " TEXT , " +
                    DB_URL + " TEXT ) ";

    private static final String TABLE_STORY_DROP_QUERY = "DROP TABLE IF EXISTS " + DB_TABLE_STORY;
    private static final String TABLE_URLS_DROP_QUERY = "DROP TABLE IF EXISTS " + DB_TABLE_URLS;

    public DbHelper(Context context) {
        super(context, DB_TABLE_STORY, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db == null)
            return;

        try {
            db.execSQL(TABLE_CREATE_STORYDATA_QUERY);
            db.execSQL(TABLE_CREATE_URLS_QUERY);

        } catch (SQLiteException e) {
            Log.e(TAG, "Error01: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db == null)
            return;

        try {
            db.execSQL(TABLE_STORY_DROP_QUERY);
            db.execSQL(TABLE_URLS_DROP_QUERY);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error02: " + e.getLocalizedMessage());
        }

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
