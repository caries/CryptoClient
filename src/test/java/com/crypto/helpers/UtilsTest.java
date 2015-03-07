package com.crypto.helpers;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UtilsTest {
    @Test
    public void testFilterElements() throws Exception {
        List<String> result = Utils.filterElements("<foo>\n" +
                "<bar/>\n" +
                "<bar/>\n" +
                "<bar/>\n" +
                "</foo>", "/foo/bar");

        assertTrue(result.size() == 3);
        assertEquals(result.toString(), "[<bar/>, <bar/>, <bar/>]");
    }
}