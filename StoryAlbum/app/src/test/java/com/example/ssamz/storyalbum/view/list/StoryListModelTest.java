package com.example.ssamz.storyalbum.view.list;

import android.util.Log;

import com.example.ssamz.storyalbum.file.FileManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoryListModelTest {

    @Test
    public void convertDate() throws Exception {
        StoryListModel model = new StoryListModel();
        String longDate = FileManager.getinstance().convertDate("20170122_212815", true);
        String shortDate = FileManager.getinstance().convertDate("20170122_212815", false);

        assertEquals(longDate, "2017년 01월 22일 21시 28분 15초");
        assertEquals(shortDate, "2017년 01월");
    }

    @Test
    public void getFileName() throws Exception {
        String path = FileManager.getinstance().getPath("abc", "1.jpg");

    }

}