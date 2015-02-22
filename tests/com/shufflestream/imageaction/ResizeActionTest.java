package com.shufflestream.imageaction;

import static org.junit.Assert.*;
import java.io.File;

public class ResizeActionTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testExecuteAction() throws Exception {

        // Get location for our test resource
        String path = getClass().getResource("/endless.jpg").getPath();

        // Set destination somewhere in home for the sake of this test
        String home = System.getProperty("user.home");
        String destination = home + "/outtests/out.jpg";

        ResizeAction resizeAction = new ResizeAction(path,destination,800,0);
        resizeAction.executeAction();
    }
}