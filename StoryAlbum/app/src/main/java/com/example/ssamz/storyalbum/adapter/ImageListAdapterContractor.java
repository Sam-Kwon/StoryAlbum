package com.example.ssamz.storyalbum.adapter;

import android.content.Context;
import android.widget.ImageView;

public interface ImageListAdapterContractor {
    interface View {
        void setPresenter(Presenter presenter);
    }
    interface Presenter{
        void setModel(Model model);
        void setImage(Context context, String fileName, ImageView view);
    }

    interface Model {
        void setImage(Context context, String fileName, ImageView view);
    }
}
