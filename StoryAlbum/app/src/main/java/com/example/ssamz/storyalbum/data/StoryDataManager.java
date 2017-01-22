package com.example.ssamz.storyalbum.data;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ssamz.storyalbum.database.DbManager;
import com.example.ssamz.storyalbum.view.list.StoryListContractor;

import java.util.ArrayList;

public class StoryDataManager {
    static final String TAG = StoryDataManager.class.getSimpleName();

    private static StoryDataManager _instance;

    ArrayList<ArrayList<StoryData>> mData;

    StoryData mCurrentData;

    StoryListContractor.Callback mCallback;

    public static StoryDataManager getInstance() {
        if (_instance == null) {
            _instance = new StoryDataManager();
        }
        return _instance;
    }

    StoryDataManager() {
        getData(null);
    }

    AsyncTask<Void, Void, ArrayList<ArrayList<StoryData>>> mTask;

    void getData(String searchText) {
        mTask = new GetListThread(searchText);
        mData = null;
        mTask.execute();
    }

    public StoryData CreateData(String folderName) {
        mCurrentData = new StoryData(folderName, new ArrayList<String>(), null, null, folderName);
        return mCurrentData;
    }

    public StoryData getCurrentData() {
        return mCurrentData;
    }

    public void setCurrentData(StoryData data) {
        mCurrentData = data;
    }

    public void addCurrentData() {
        for (int i = 0; i < mData.size(); i++) {
            boolean bFound = false;
            ArrayList<StoryData> datas = mData.get(i);
            for (int j = 0; j < datas.size(); j++) {
                StoryData story = datas.get(j);
                if (story.getPath().equals(mCurrentData.getPath())) {
                    bFound = true;
                    datas.remove(j);
                    break;
                }
            }
            if (bFound)
                break;
        }

        if (mData != null && mData.size() != 0 && StoryData.isSameGroup(mData.get(0).get(0).getDate(), mCurrentData.getDate()))
            mData.get(0).add(0, mCurrentData);
        else {
            mData.add(new ArrayList<StoryData>());
            mData.get(0).add(0, mCurrentData);
        }
        mCurrentData = null;
    }

    public void getStoryList(StoryListContractor.Callback callback) {

        if (mData != null)
            callback.onSuccess(mData);

        mCallback = callback;

        if (mTask == null || mTask.getStatus() == AsyncTask.Status.FINISHED) {
            getData(null);
        }
    }

    public void getStoryList(String searchTxt, StoryListContractor.Callback callback) {
        mData = null;
        mCallback = callback;

        if (mTask == null || mTask.getStatus() == AsyncTask.Status.FINISHED) {
            getData(searchTxt);
        } else {
            mTask.cancel(true);
            getData(searchTxt);
        }
    }

    class GetListThread extends AsyncTask<Void, Void, ArrayList<ArrayList<StoryData>>> {
        String search;

        GetListThread(String searchTxt) {
            search = searchTxt;
        }

        @Override
        protected ArrayList<ArrayList<StoryData>> doInBackground(Void... params) {
            return DbManager.getInstance().getStoryList(search);
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<StoryData>> arrayLists) {
            if (arrayLists != null) {
                mData = arrayLists;
            } else {
                mData = new ArrayList<ArrayList<StoryData>>();
            }

            if (mCallback != null) {
                mCallback.onSuccess(mData);
            }
        }
    };
}
