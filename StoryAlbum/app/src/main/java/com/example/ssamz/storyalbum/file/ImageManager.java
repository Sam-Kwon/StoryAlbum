package com.example.ssamz.storyalbum.file;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageManager {
    static final String TAG = ImageManager.class.getSimpleName();

    private static ImageManager _instance;

    public static ImageManager getInstance() {
        if (_instance == null) {
            return new ImageManager();
        }
        return _instance;
    }

    public void setImage(Context context, String path, String fileName, ImageView view) {
        Glide.with(context).load(FileManager.getinstance().getPath(path, fileName)).into(view);
    }
}
