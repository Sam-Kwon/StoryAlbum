package com.example.ssamz.storyalbum.view.list;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.ssamz.storyalbum.adapter.StoryListAdapterContractor;
import com.example.ssamz.storyalbum.data.StoryData;

import java.util.ArrayList;

public class StoryListPresenter implements StoryListContractor.Presenter, StoryListAdapterContractor.Presenter {
    static final String TAG = StoryListPresenter.class.getSimpleName();

    StoryListContractor.View mView;
    StoryListContractor.Model mModel;
    StoryListAdapterContractor.Model mAdapterModel;

    ArrayList<ArrayList<StoryData>> mData;

    @Override
    public void setView(StoryListContractor.View view) {
        mView = view;
    }

    @Override
    public void setModel(StoryListContractor.Model model) {
        mModel = model;
    }

    @Override
    public void setAdapterModel(StoryListAdapterContractor.Model model) {
        mAdapterModel = model;
    }

    @Override
    public void getData() {
        mModel.getData(new StoryListContractor.Callback() {
            @Override
            public void onSuccess(ArrayList<ArrayList<StoryData>> list) {
                //data 전달..
                mView.updateData(list);
                mData = list;
            }

            @Override
            public void onFail() {
                //아무동작X
                Log.e(TAG, "Error: getData onFailed...");
            }
        });
    }

    @Override
    public void getDataBySearch(String searchTxt) {
        mModel.getData(searchTxt, new StoryListContractor.Callback() {
            @Override
            public void onSuccess(ArrayList<ArrayList<StoryData>> list) {
                //data 전달..
                mView.updateData(list);
                mData = list;
            }

            @Override
            public void onFail() {
                //아무동작X
                Log.e(TAG, "Error: getData onFailed...");
            }
        });
    }

    @Override
    public void setCurrentData(int groupPosition, int childPosition) {
        if (mData == null) {
            return;
        }
        StoryData data = mData.get(groupPosition).get(childPosition);
        mModel.setCurrentData(data);
    }

    @Override
    public String convertDate(String date, boolean isLong) {
        return mAdapterModel.convertDate(date, isLong);
    }

    @Override
    public void setImage(Context mContext, String path, String fileName, ImageView image) {
        mAdapterModel.setImage(mContext, path, fileName, image);
    }

    @Override
    public void deleteChild(int headerPos, int childPos) {
        StoryData data = mData.get(headerPos).get(childPos);
        mAdapterModel.deleteChild(data);
        getData();
    }
}
