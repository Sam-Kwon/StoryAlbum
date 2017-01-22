package com.example.ssamz.storyalbum.view.camera;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ssamz.storyalbum.R;
import com.example.ssamz.storyalbum.view.edit.EditActivity;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener, CameraContractor.View {
    private final static String TAG = CameraActivity.class.getSimpleName();

    private Camera mCamera;
    private CameraPreview mPreview;

    private CameraContractor.Presenter mPresenter;
    private CameraContractor.Model mModel;

    Button mCaptureButton;
    TextView mEndButton;
    TextView mContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        setResources();

        mPresenter = new CameraPresenter(getApplicationContext());
        mModel = new CameraModel();

        mPresenter.setView(this);
        mPresenter.setModel(mModel);

        mPreview = new CameraPreview(this);
        LinearLayout preview = (LinearLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCamera = getCameraInstance();

        if (mCamera != null) {
            mPreview.init(mCamera);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
        }
        releaseCamera();
    }

    void setResources() {
        mCaptureButton = (Button) findViewById(R.id.camera_capture);
        mCaptureButton.setOnClickListener(this);
        mEndButton = (TextView) findViewById(R.id.camera_end);
        mEndButton.setOnClickListener(this);
        mEndButton.setEnabled(false);
        mContinueButton = (TextView) findViewById(R.id.camera_continue);
        mContinueButton.setOnClickListener(this);
        mContinueButton.setEnabled(false);
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "ERROR: camera is not opened:" + e.getLocalizedMessage());
        }
        return camera;
    }

    void releaseCamera() {
        if (mCamera == null) {
            return;
        }
        try {
            mCamera.release();
        } catch (Exception e) {
            Log.e(TAG, "ERROR: camera is not released:" + e.getLocalizedMessage());
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (mPresenter == null) {
                return;
            }
            mPresenter.takePicture(data);
        }
    };

    Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.camera_capture:
                    mCaptureButton.setEnabled(true);
                    startCamera();
                    break;
            }
        }
    };

    void startCamera() {
        try {
            mCamera.startPreview();
        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_capture:
                if (mCamera == null) {
                    break;
                }
                mCamera.takePicture(null, null, mPicture);
                mCaptureButton.setEnabled(false);
                mHandler.sendEmptyMessageDelayed(R.id.camera_capture, 3000);
                break;
            case R.id.camera_end:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.MODE_EDIT, false);
                startActivity(intent);

                break;
            case R.id.camera_continue:
                break;

            default:
        }
    }

    @Override
    public void updateView(int count) {
        mEndButton.setEnabled(true);
        if (count > 0) {
            mContinueButton.setText(getResources().getString(R.string.camera_continue) + "(" + count + ")");
        }
    }
}
