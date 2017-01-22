package com.example.ssamz.storyalbum.view.camera;

/**
 * Created by ssamz on 2017-01-16.
 */

public interface CameraContractor {
    public interface View  {
        void updateView(int count);
    }
    public interface Presenter {
        void setView(CameraContractor.View view);
        void setModel(CameraContractor.Model model);
        void takePicture(byte[] data);
    }

    public interface Model {
        void setDir(String dir);
        public String getCurrentDate();
        void takePicture(byte[] data);
    }
}
