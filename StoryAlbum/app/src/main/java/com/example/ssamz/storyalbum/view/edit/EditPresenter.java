package com.example.ssamz.storyalbum.view.edit;

import android.content.Context;
import android.widget.ImageView;

import com.example.ssamz.storyalbum.adapter.ImageListAdapterContractor;
import com.example.ssamz.storyalbum.data.StoryData;

public class EditPresenter implements EditContractor.Presenter, ImageListAdapterContractor.Presenter {
    EditContractor.View mView;
    EditContractor.Model mModel;

    ImageListAdapterContractor.Model mAdapterModel;

    StoryData mData;

    @Override
    public void setView(EditContractor.View view) {
        mView = view;
    }

    @Override
    public void setModel(EditContractor.Model model) {
        mModel = model;
    }

    @Override
    public void setModel(ImageListAdapterContractor.Model model) {
        mAdapterModel = model;
    }

    @Override
    public StoryData getData() {
        if (mModel == null) {
            return null;
        }
        return mModel.getData();
    }

    @Override
    public void updateData(String title, String memo) {
        mModel.updateData(title, memo);
    }

    @Override
    public void setImage(Context context, String fileName, ImageView view) {
        mAdapterModel.setImage(context, fileName, view);
    }

    @Override
    public String convertDate(String src, boolean isLong) {
        return mModel.convertDate(src, isLong);
    }
}
