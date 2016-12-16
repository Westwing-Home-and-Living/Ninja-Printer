package de.westwing.printer.ninja.lib.message;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;
import de.westwing.printer.ninja.lib.document.ByteDocument;
import de.westwing.printer.ninja.lib.document.ByteDocumentFactory;
import de.westwing.printer.ninja.lib.document.Base64DocumentFactory;
import org.json.JSONException;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by felixwoell on 2016-12-16.
 */
public class JsonMessageParserTest {
    @Test
    public void getPrintMessageInvokesByteDocumentFactoryWhenFilePathIsProvided() throws JSONException, IOException {
        MessageInterface mockMessage = Mockito.mock(MessageInterface.class);
        ByteDocumentFactory mockByteDocumentFactory = Mockito.mock(ByteDocumentFactory.class);
        Base64DocumentFactory mockBase64DocumentFactory = Mockito.mock(Base64DocumentFactory.class);

        String messageBody = "{\"printerName\" : \"dummy\", \"printerType\" : \"dummy\", \"filePath\" : \"dummy\",\"cookie\" : \"dummy\"}";
        when(mockMessage.getBody()).thenReturn(messageBody);
        when(mockByteDocumentFactory.createByteDocumentFromFileContent("dummy")).thenReturn(new ByteDocument(new byte[] {1} ));
        when(mockByteDocumentFactory.createByteDocumentFromUrl("dummy", "dummy")).thenThrow(MalformedURLException.class);


        JsonMessageParser messageParser = JsonMessageParser.getInstance(mockByteDocumentFactory, mockBase64DocumentFactory);
        messageParser.getPrintMessage(mockMessage);

        verify(mockByteDocumentFactory, times(1)).createByteDocumentFromFileContent("dummy");
    }

    @Test
    public void getPrintMessageInvokesByteDocumentFactoryWhenFilePathAndCookieAreProvided() throws JSONException, IOException {
        MessageInterface mockMessage = Mockito.mock(MessageInterface.class);
        ByteDocumentFactory mockByteDocumentFactory = Mockito.mock(ByteDocumentFactory.class);
        Base64DocumentFactory mockBase64DocumentFactory = Mockito.mock(Base64DocumentFactory.class);

        String messageBody = "{\"printerName\" : \"dummy\", \"printerType\" : \"dummy\", \"filePath\" : \"dummy\", \"cookie\" : \"dummy\"}";
        when(mockMessage.getBody()).thenReturn(messageBody);
        when(mockByteDocumentFactory.createByteDocumentFromUrl("dummy", "dummy")).thenReturn(new ByteDocument(new byte[] {1} ));

        JsonMessageParser messageParser = JsonMessageParser.getInstance(mockByteDocumentFactory, mockBase64DocumentFactory);
        messageParser.getPrintMessage(mockMessage);

        verify(mockByteDocumentFactory, times(1)).createByteDocumentFromUrl("dummy", "dummy");
    }

    @Test
    public void getPrintMessageInvokesBase64DocumentFactoryWhenFilePathIsNotProvided() throws JSONException, IOException {
        MessageInterface mockMessage = Mockito.mock(MessageInterface.class);
        ByteDocumentFactory mockByteDocumentFactory = Mockito.mock(ByteDocumentFactory.class);
        Base64DocumentFactory mockBase64DocumentFactory = Mockito.mock(Base64DocumentFactory.class);

        String messageBody = "{\"printerName\" : \"dummy\", \"printerType\" : \"dummy\", \"fileContent\" : \"dummy\"}";
        when(mockMessage.getBody()).thenReturn(messageBody);
        when(mockBase64DocumentFactory.createBase64Document("dummy")).thenReturn(new ByteDocument(new byte[] {1} ));

        JsonMessageParser messageParser = JsonMessageParser.getInstance(mockByteDocumentFactory, mockBase64DocumentFactory);
        messageParser.getPrintMessage(mockMessage);

        verify(mockBase64DocumentFactory, times(1)).createBase64Document("dummy");
    }



}