package com.example.ssamz.storyalbum.file;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by ssamz on 2017-01-19.
 */

public class FileManager {
    static final String TAG = FileManager.class.getSimpleName();

    private static FileManager _instance;
    private String mDir;
    private int mCount;

    private static File _mediaFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "StoryAlbum");

    static public FileManager getinstance() {
        if (_instance == null) {
            _instance = new FileManager();
        }
        return _instance;
    }

    public void init(String dir) {
        mDir = dir;
        mCount = 0;
    }

    public String makeImage(final byte[] data) {
        if (mDir == null) {
            return null;
        }

        final String fileName = getFileName(MEDIA_TYPE_IMAGE);
        if (fileName == null) {
            return null;
        }

        //아래는 Thread로 전환하고 위에서 fileName return하여 StoryData에 저장
        new Thread() {
            @Override
            public void run() {

                File pictureFile = makeFile(fileName);
                if (pictureFile == null) {
                    Log.d(TAG, "Error creating media file, check storage permissions: ");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        }.start();

        return fileName;
    }

    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String DATE_FOR_TITLE = "yyyy년 MM월";
    public static final String DATE_FOR_TEXT = "yyyy년 MM월 dd일 HH시 mm분";
    public String getCurrentDate() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date());
    }

    public String convertDate(String src, boolean isLong) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = format.parse(src);
        } catch (ParseException e) {
            Log.e(TAG, "ERROR 1: " + e.getLocalizedMessage());
        }

        String toFormat;
        if (isLong) {
            toFormat = DATE_FOR_TEXT;
        } else {
            toFormat = DATE_FOR_TITLE;
        }

        if (date != null) {
            return new SimpleDateFormat(toFormat).format(date);
        }
        return null;
    }

    @Nullable
    private String getFileName(int type) {
        String fileName = null;
        if (type == MEDIA_TYPE_IMAGE) {
            fileName = "IMG_" + (++mCount) + ".jpg";
        } else if (type == MEDIA_TYPE_VIDEO) {
            fileName = "VID_" + (++mCount) + ".mp4";
        }

        return fileName;
    }

    private File makeFile(String fileName) {


        if (!_mediaFilePath.exists()) {
            if (!_mediaFilePath.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        File folder = new File(_mediaFilePath.getPath() + File.separator + mDir);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Log.d(TAG, "failed to create dir 2");
            }
        }

        File mediaFile;
        mediaFile = new File(_mediaFilePath.getPath() + File.separator + mDir + File.separator + fileName);

        return mediaFile;
    }

    public String getPath(String path, String fileName) {

        return _mediaFilePath.getPath() + File.separator + path + File.separator + fileName;
    }

    public void removeFile(String path) {
        File folder = new File(_mediaFilePath.getPath() + File.separator + path);
        if (folder.exists()) {
            folder.delete();
        }

    }
}
