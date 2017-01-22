package com.example.ssamz.storyalbum.view.list;

import com.example.ssamz.storyalbum.adapter.StoryListAdapterContractor;
import com.example.ssamz.storyalbum.data.StoryData;

import java.util.ArrayList;

public interface StoryListContractor {
    interface View {
        void updateData(ArrayList<ArrayList<StoryData>> list);
    }

    interface Presenter {
        void setView(StoryListContractor.View view);
        void setModel(StoryListContractor.Model model);
        void getData();

        void setCurrentData(int groupPosition, int childPosition);

        void getDataBySearch(String searchTxt);
    }
    interface Model {
        void setCurrentData(StoryData data);
        void getData(Callback callback);
        void getData(String searchTxt, Callback callback);
    }

    interface Callback {
        void onSuccess(ArrayList<ArrayList<StoryData>> list);
        void onFail();
    }

}
