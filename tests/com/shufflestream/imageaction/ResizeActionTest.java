package com.shufflestream.imageaction;

import static org.junit.Assert.*;

public class ResizeActionTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testExecuteAction() throws Exception {
        final String SOURCE = "/Users/miguelalvarado/Dropbox/SHUFFLE/ FOR PROTOTYPE/Cole Rise/abstractions1.jpg";

        ResizeAction resizeAction = new ResizeAction(SOURCE);
        resizeAction.executeAction();
    }
}