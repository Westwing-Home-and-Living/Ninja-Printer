package de.westwing.printer.ninja.lib.chrome.message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {
    protected Message sut;

    protected String initMessage = "dummy starting input";

    @Before
    public void setUp() throws Exception {
        sut = new Message(initMessage);
    }

    @After
    public void tearDown() throws Exception {
        sut = null;
    }

    @Test
    public void testeSetBodyOverridesPreviousValue() throws Exception {
        String expectedResult = "something else";
        String actualResult;

        sut.setBody(expectedResult);
        actualResult = sut.getBody();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetBodyRetrievesInitialMessage() throws Exception {
        String expectedResult = initMessage;
        String actualResult;

        actualResult = sut.getBody();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIdCanBeSetAndRetrieved() throws Exception {
        int expectedResult = 8988;
        int actualResult;

        sut.setId(expectedResult);
        actualResult = sut.getId();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetLengthRetrievesLength() throws Exception {
        int expectedResult = initMessage.length();
        int actualResult;

        actualResult = sut.getLength();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringRetrievesBodyAsString() throws Exception {
        String expectedResult = initMessage.toString();
        String actualResult;

        actualResult = sut.toString();

        assertEquals(expectedResult, actualResult);
    }
}
