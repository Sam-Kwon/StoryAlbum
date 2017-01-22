package com.example.ssamz.storyalbum.view.camera;

import android.content.Context;

import com.example.ssamz.storyalbum.database.DbManager;

import java.util.ArrayList;


public class CameraPresenter implements CameraContractor.Presenter {
    CameraContractor.View mView;
    CameraContractor.Model mModel;

    Context mContext;
    int mCount;

    String mDir;
    ArrayList<String> mFiles;

    public CameraPresenter(Context context) {
        mContext = context;
        mCount = 0;

        mFiles = new ArrayList<String>();
    }

    @Override
    public void setView(CameraContractor.View view) {
        mView = view;
    }

    @Override
    public void setModel(CameraContractor.Model model) {
        mModel = model;
    }

    @Override
    public void takePicture(byte[] data) {
        mCount++;
        String date = mModel.getCurrentDate();
        if (mCount == 1) {
            mModel.setDir(date);
            mDir = date;
        }

        // takePicture 이후 files를 callback을 통해 전달받아 편집 화면으로 전달해줘야 함.
        mModel.takePicture(data);

        // View에 update..
        mView.updateView(mCount);
    }


}
