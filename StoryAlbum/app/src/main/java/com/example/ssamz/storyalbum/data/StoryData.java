package com.example.ssamz.storyalbum.data;

import android.util.Log;

import java.util.ArrayList;

public class StoryData {
    static final String TAG = StoryData.class.getSimpleName();

    String mPath;
    ArrayList<String> mUrls;
    String mTitle;
    String mMemo;
    String mEditedDate;

    public StoryData(String path, ArrayList<String> urls, String title, String memo, String date) {
        mPath = path;
        mUrls = new ArrayList<String>();
        if (urls != null) {
            mUrls.addAll(urls);
        }
        mTitle = title;
        mMemo = memo;
        mEditedDate = date;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    public void setUrls(ArrayList<String> urls) {
        mUrls.clear();
        mUrls.addAll(urls);
    }

    public void addUrl(String url) {
        mUrls.add(url);
    }

    public void removeUrl(String url) {
        mUrls.remove(url);
    }

    public ArrayList<String> getUrls() {
        return mUrls;
    }

    public int getImageSize() {
        if (mUrls == null) {
            return 0;
        }
        return mUrls.size();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setMemo(String memo) {
        mMemo = memo;
    }

    public String getMemo() {
        return mMemo;
    }

    public void setDate(String date) {
        mEditedDate = date;
    }

    public String getDate() {
        return mEditedDate;
    }

    public static boolean isSameGroup(String date1, String date2) {
        if (date1.substring(0, 6).equals(date2.substring(0, 6)) )
            return true;
        return false;
    }
}
