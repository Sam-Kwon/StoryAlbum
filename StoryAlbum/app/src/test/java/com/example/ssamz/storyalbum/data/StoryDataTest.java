package com.example.ssamz.storyalbum.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoryDataTest {
    @Test
    public void getFileName() throws Exception {
        boolean result = StoryData.isSameGroup("20170123", "20170123_11");

        assertEquals(result, true);

        result = StoryData.isSameGroup("201601", "201701");

        assertEquals(result, false);
    }

}