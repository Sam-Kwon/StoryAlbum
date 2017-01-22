package com.example.ssamz.storyalbum.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.ssamz.storyalbum.data.StoryData;

import java.util.ArrayList;

public class DbManager {
    static final String TAG = DbManager.class.getSimpleName();

    Context mContext;
    DbHelper mDbHelper;

    static DbManager _instance;
    SQLiteDatabase db;

    public static DbManager getInstance() {
        if (_instance == null) {
            _instance = new DbManager();
        }
        return _instance;
    }

    public void init(Context context) {
        mContext = context;
        mDbHelper = new DbHelper(context);
    }

    SQLiteDatabase getWritableDB() {
        if (db == null) {
            db = mDbHelper.getWritableDatabase();
        }

        if (db == null) {
            Log.e(TAG, "<getWritableDB> ERROR writer: db is null...");
            return null;
        }
        return db;
    }

    ArrayList<ArrayList<StoryData>> getStoryGroup(String search) {
        SQLiteDatabase db = getWritableDB();
        if (db == null) {
            return null;
        }

        String[] projection = {
                DbHelper.DB_ITEMS_PATH,
                DbHelper.DB_TITLE,
                DbHelper.DB_MEMO,
                DbHelper.DB_DATE,
        };

        String selection = null;
        String[] selectionArgs = null;
        if (search != null){
            selection = DbHelper.DB_TITLE + " LIKE ? OR " + DbHelper.DB_MEMO + " LIKE ? ";
            String[] args = { "%"+search+"%", "%"+search+"%" };
            selectionArgs = args;
        }


        String orderBy = DbHelper.DB_DATE + " DESC ";
        Cursor cur = null;
        ArrayList<ArrayList<StoryData>> list = null;
        try {
            cur = db.query(
                    DbHelper.DB_TABLE_STORY,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    orderBy
            );

            if (cur != null && cur.moveToFirst() && cur.getCount() > 0) {

                ArrayList<StoryData> group = new ArrayList<StoryData>();
                list = new ArrayList<ArrayList<StoryData>>();
                list.add(group);

                String lastDate = cur.getString(3);

                // group 나누기.
                do {
                    StoryData storyData = new StoryData(cur.getString(0), null, cur.getString(1), cur.getString(2), cur.getString(3));
                    if (StoryData.isSameGroup(lastDate, storyData.getDate())) {
                        group.add(storyData);
                    } else {
                        group = new ArrayList<StoryData>();
                        group.add(storyData);

                        list.add(group);
                        lastDate = storyData.getDate();
                    }

                } while(cur.moveToNext());


            }

        } catch (SQLiteException e) {
            Log.e(TAG, "<getStoryListCursor> Error:" + e.getLocalizedMessage());
        } finally {
            if (cur != null)
                cur.close();
        }
        return list;
    }

    ArrayList<String> getUrls(String path) {
        if (path == null) {
            return null;
        }
        SQLiteDatabase db = getWritableDB();
        if (db == null) {
            return null;
        }

        String[] projection = {
                DbHelper.DB_URL,
        };

        Cursor cur = null;
        ArrayList<String> urls = new ArrayList<String>();

        try {

            String selection = DbHelper.DB_ITEMS_PATH + " LIKE ? ";
            String[] selectionArgs = { path };
            cur = db.query(
                    DbHelper.DB_TABLE_URLS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cur != null && cur.moveToFirst() && cur.getCount() > 0) {
                do {
                    urls.add(cur.getString(0));
                }
                while(cur.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "<getUrls> Error:" + e.getLocalizedMessage());
        } finally {
            if (cur != null)
                cur.close();
        }

        return urls;
    }

    public ArrayList<ArrayList<StoryData>> getStoryList(String search) {
        ArrayList<ArrayList<StoryData>> list = getStoryGroup(search);
        if (list != null && list.size() > 0) {
            // urls 가져오기
            for (int x = 0; x < list.size() ; x++) {
                ArrayList<StoryData> group = list.get(x);
                for (int y = 0; y < group.size(); y++) {
                    StoryData data = group.get(y);
                    ArrayList<String> urls = getUrls(data.getPath());
                    data.setUrls(urls);
                }
            }
        }
        return list;
    }

    public void insertStory(String path, String title, String memo, String date) {
        SQLiteDatabase db = getWritableDB();
        if (db == null) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DbHelper.DB_ITEMS_PATH, path);
        values.put(DbHelper.DB_TITLE, title);
        values.put(DbHelper.DB_MEMO, memo);
        values.put(DbHelper.DB_DATE, date);

        try {
            Log.d(TAG, "insertStory DB: path: " + path + ", date:" + date);
            long rowId = db.insert(DbHelper.DB_TABLE_STORY, null, values);
            if (rowId == -1) {
                String selection = DbHelper.DB_ITEMS_PATH + " LIKE ?";
                String[] args = {path};
                Log.d(TAG, "update DB: path: " + path + ", date:" + date);
                db.update(DbHelper.DB_TABLE_STORY, values, selection, args);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "ERROR 0: " + e.getLocalizedMessage());
        } finally {
//            db.close();
        }
    }

    public void insertURLs(String path, ArrayList<String> urls) {
        SQLiteDatabase db = getWritableDB();
        if (db == null) {
            return;
        }

        ContentValues values = new ContentValues();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            values.put(DbHelper.DB_ITEMS_PATH, path);
            values.put(DbHelper.DB_URL, url);

            try {
                Log.d(TAG, "insert DB: path: " + path + ", url:" + url);
                long rowId = db.insert(DbHelper.DB_TABLE_URLS, null, values);
                if (rowId == -1) {
                    Log.w(TAG, "Failed to insert DB: " + url);
                }
            } catch (SQLiteException e) {
                Log.e(TAG, "ERROR 1: " + e.getLocalizedMessage());
            } finally {
//                db.close();
            }

            values.clear();
        }
    }

    public void deleteStory(String path) {
        SQLiteDatabase db = getWritableDB();
        if (db == null) {
            return;
        }

        String selection = DbHelper.DB_ITEMS_PATH + " LIKE ?";
        String[] args = {path};
        Log.d(TAG, "delete DB: path: " + path);
        try {
            db.delete(DbHelper.DB_TABLE_STORY, selection, args);
            db.delete(DbHelper.DB_TABLE_URLS, selection, args);
        } catch (SQLiteException e) {
            Log.e(TAG, "ERROR 2: " + e.getLocalizedMessage());
        } finally {
//            db.close();
        }
    }
}
