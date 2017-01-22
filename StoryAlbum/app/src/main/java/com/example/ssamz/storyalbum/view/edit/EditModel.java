package com.example.ssamz.storyalbum.view.edit;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.ssamz.storyalbum.adapter.ImageListAdapterContractor;
import com.example.ssamz.storyalbum.data.StoryData;
import com.example.ssamz.storyalbum.data.StoryDataManager;
import com.example.ssamz.storyalbum.database.DbManager;
import com.example.ssamz.storyalbum.file.FileManager;
import com.example.ssamz.storyalbum.file.ImageManager;

public class EditModel implements EditContractor.Model, ImageListAdapterContractor.Model {
    static final String TAG = EditModel.class.getSimpleName();
    StoryData mData;

    @Override
    public StoryData getData() {
        mData = StoryDataManager.getInstance().getCurrentData();
        if (mData != null) {
            Log.d(TAG, "StoryData path:" + mData.getPath() + ", urls:" + mData.getUrls().size() + ", title:" + mData.getTitle() + ", Memo:" + mData.getMemo());
        }
        return mData;
    }

    @Override
    public void updateData(String title, String memo) {
        if (mData == null) {
            return;
        }
        final String date = FileManager.getinstance().getCurrentDate();

        mData.setTitle(title);
        mData.setMemo(memo);
        mData.setDate(date);

        // storyData에 update
        StoryDataManager.getInstance().addCurrentData();

        // db에 업데이트
        new Thread() {
            @Override
            public void run() {
                DbManager.getInstance().insertStory(mData.getPath(), mData.getTitle(), mData.getMemo(), date);
                DbManager.getInstance().insertURLs(mData.getPath(), mData.getUrls());
                mData = null;
            }
        }.start();
    }

    @Override
    public void setImage(Context context, String fileName, ImageView view) {
        ImageManager.getInstance().setImage(context, mData.getPath(), fileName, view);
    }

    @Override
    public String convertDate(String src, boolean isLong) {
        return FileManager.getinstance().convertDate(src, isLong);
    }
}
