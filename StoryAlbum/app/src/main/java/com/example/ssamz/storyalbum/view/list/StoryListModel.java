package com.example.ssamz.storyalbum.view.list;

import android.content.Context;
import android.widget.ImageView;

import com.example.ssamz.storyalbum.adapter.StoryListAdapterContractor;
import com.example.ssamz.storyalbum.data.StoryData;
import com.example.ssamz.storyalbum.data.StoryDataManager;
import com.example.ssamz.storyalbum.database.DbManager;
import com.example.ssamz.storyalbum.file.FileManager;
import com.example.ssamz.storyalbum.file.ImageManager;

public class StoryListModel implements StoryListContractor.Model, StoryListAdapterContractor.Model{

    @Override
    public void getData(StoryListContractor.Callback callback) {
        StoryDataManager.getInstance().getStoryList(callback);
    }

    @Override
    public void getData(String searchTxt, StoryListContractor.Callback callback) {
        StoryDataManager.getInstance().getStoryList(searchTxt, callback);
    }

    @Override
    public void setCurrentData(StoryData data) {
        StoryDataManager.getInstance().setCurrentData(data);
    }

    @Override
    public String convertDate(String src, boolean isLong) {
        return FileManager.getinstance().convertDate(src, isLong);
    }

    @Override
    public void setImage(Context context, String path, String fileName, ImageView view) {
        ImageManager.getInstance().setImage(context, path, fileName, view);
    }

    @Override
    public void deleteChild(StoryData data) {
        // file  삭제
        FileManager.getinstance().removeFile(data.getPath());

        // db 삭제
        DbManager.getInstance().deleteStory(data.getPath());
    }
}
