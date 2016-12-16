package de.westwing.printer.ninja.lib.document;

import org.junit.Test;
import org.mockito.Mockito;

import java.nio.ByteBuffer;
import java.util.Base64.Decoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Base64DocumentTest {
    @Test
    public void toRawStringCanDecodeUnicodeStrings() throws Exception {
        String expectedMessage = "Hello, 世界!";
        DocumentInterface doc = new Base64Document("SGVsbG8sIOS4lueVjCE=");

        String actualMessage = doc.toRawString();

        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void toBytesCachesResult() throws Exception {
        String inputMessage = "asds";
        String expectedMessage = "Hello, 世界!";
        Decoder mockDecoder = Mockito.mock(Decoder.class);

        when(mockDecoder.decode(inputMessage)).thenReturn(expectedMessage.getBytes());

        Base64Document doc = new Base64Document(inputMessage);
        doc.setDecoder(mockDecoder);
        
        doc.toBytes();
        doc.toBytes();
        doc.toBytes();
        
        verify(mockDecoder, times(1)).decode(inputMessage);
    }

    @Test
    public void toByteBufferReturnsTheExpectedMessageWrapped() throws Exception {
        String expectedMessage = "Hello, 世界!";
        DocumentInterface doc = new Base64Document("SGVsbG8sIOS4lueVjCE=");
        
        ByteBuffer bb = doc.toByteBuffer();
        
        assertEquals(expectedMessage, new String(bb.array()));
    }

}