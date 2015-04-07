package com.shufflestream.imageaction;

import junit.framework.TestCase;

public class ValidateActionTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testExecuteAction() throws Exception {

        // Some image path
       final String SOURCE = getClass().getResource("/endless.jpg").getPath();

        ValidateAction validateAction = new ValidateAction(SOURCE,"");
        validateAction.executeAction();
    }
}