package com.example.ssamz.storyalbum.view.edit;

import android.content.Context;
import android.widget.ImageView;

import com.example.ssamz.storyalbum.data.StoryData;

public interface EditContractor {

    interface View {
    }

    interface Presenter {
        void setView(EditContractor.View view);
        void setModel(EditContractor.Model model);

        StoryData getData();
        void updateData(String title, String memo);
        String convertDate(String src, boolean isLong);
    }

    interface Model {
        StoryData getData();
        void updateData(String title, String memo);
        String convertDate(String src, boolean isLong);
    }
}
