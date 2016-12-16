package de.westwing.printer.ninja.lib.document;

import org.junit.Test;
import java.nio.ByteBuffer;
import org.junit.Assert;


/**
 * Created by felixwoell on 2016-12-16.
 */
public class ByteDocumentTest {
    @Test
    public void toRawStringCanDecodeUnicodeStrings() throws Exception {
        String expectedMessage = "菲利克斯最屌!";
        DocumentInterface doc = new ByteDocument(expectedMessage.getBytes());

        String actualMessage = doc.toRawString();

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void toByteBufferReturnsTheExpectedMessageWrapped() throws Exception {
        String expectedMessage = "菲利克斯最屌!";
        DocumentInterface doc = new ByteDocument(expectedMessage.getBytes());

        ByteBuffer bb = doc.toByteBuffer();

        Assert.assertEquals(expectedMessage, new String(bb.array()));
    }
}