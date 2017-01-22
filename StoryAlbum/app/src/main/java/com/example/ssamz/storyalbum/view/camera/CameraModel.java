package com.example.ssamz.storyalbum.view.camera;

import com.example.ssamz.storyalbum.data.StoryData;
import com.example.ssamz.storyalbum.data.StoryDataManager;
import com.example.ssamz.storyalbum.file.FileManager;

import java.util.ArrayList;

public class CameraModel implements CameraContractor.Model {
    private static final String TAG = CameraModel.class.getSimpleName();

    private String mDir;

    private StoryData mData;

    @Override
    public void setDir(String dir) {
        mDir = dir;
        // StoryDataManager에 data 생성
        mData = StoryDataManager.getInstance().CreateData(mDir);
        FileManager.getinstance().init(mDir);
    }

    @Override
    public String getCurrentDate() {
        return FileManager.getinstance().getCurrentDate();
    }

    @Override
    public void takePicture(byte[] data) {
        String file = FileManager.getinstance().makeImage(data);
        /// current StoryData에 저장
        mData.addUrl(file);
    }
}
