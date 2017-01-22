package com.example.ssamz.storyalbum.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RecycleImageView extends ImageView {
    String mPath;

    public RecycleImageView(Context context) {
        super(context);
    }

    public RecycleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }
}
