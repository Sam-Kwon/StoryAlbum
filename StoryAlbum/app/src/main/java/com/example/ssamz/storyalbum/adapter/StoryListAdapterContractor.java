package com.example.ssamz.storyalbum.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.example.ssamz.storyalbum.data.StoryData;

import java.util.ArrayList;

public interface StoryListAdapterContractor {
    interface View {
        void setPresenter(Presenter presenter);
        void updateScreen();
        void updateDate(ArrayList<ArrayList<StoryData>> list);
    }

    interface Presenter {
        void setAdapterModel(Model model);
        String convertDate(String date, boolean isLong);

        void setImage(Context mContext, String path, String file, ImageView image);

        void deleteChild(int headerPos, int childPos);
    }

    interface Model {
        String convertDate(String date, boolean isLong);
        void setImage(Context context, String path, String fileName, ImageView view);

        void deleteChild(StoryData data);
    }
}
