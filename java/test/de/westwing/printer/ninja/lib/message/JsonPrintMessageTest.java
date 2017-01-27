package de.westwing.printer.ninja.lib.message;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;
import de.westwing.printer.ninja.lib.document.DocumentInterface;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class JsonPrintMessageTest {

    protected MessageInterface messageInterface;

    protected DocumentInterface documentInterface;

    protected JsonPrintMessage sut;

    @Before
    public void setUp() throws Exception {
        this.messageInterface = Mockito.mock(MessageInterface.class);
        this.documentInterface = Mockito.mock(DocumentInterface.class);
        this.sut = new JsonPrintMessage(messageInterface);
    }


    @Test
    public void getMessageReturnsMessageSetBefore() throws Exception {
        assertEquals(this.sut.getMessage(), this.messageInterface);
    }

    @Test
    public void getPrinterNameReturnsPrintNameSetBefore() throws Exception {
        String printName = "myPrintName";

        this.sut.setPrinterName(printName);
        assertEquals(this.sut.getPrinterName(), printName);
    }

    @Test
    public void getPrinterTypeReturnsPrintTypeSetBefore() throws Exception {
        String printType = "myPrintType";

        this.sut.setPrinterType(printType);
        assertEquals(this.sut.getPrinterType(), printType);
    }

    @Test
    public void getDocumentReturns() throws Exception {
        this.sut.setDocument(this.documentInterface);
        assertEquals(this.sut.getDocument(),this.documentInterface);
    }

    @Test
    public void getRequestIdReturnsRequestIdSetBefore() throws Exception {
        String requestId = "myRequestId";

        this.sut.setRequestId(requestId);
        assertEquals(this.sut.getRequestId(), requestId);
    }

    @Test
    public void toStringReturnsCorrectOutput() throws Exception {
        String printName = "myPrintName";
        String printType = "myPrintType";
        String requestId = "myRequestId";


        this.sut.setPrinterName(printName);
        this.sut.setPrinterType(printType);
        this.sut.setRequestId(requestId);

        assertEquals(this.sut.toString(), this.buildString(new StringBuilder()));
    }

    private String buildString(StringBuilder stringBuilder)
    {
        stringBuilder.append("[");
        stringBuilder.append("PrnterType: ");
        stringBuilder.append(this.sut.getPrinterType());
        stringBuilder.append(", PrinterName: ");
        stringBuilder.append(this.sut.getPrinterName());
        stringBuilder.append(", RequestId: ");
        stringBuilder.append(this.sut.getRequestId());
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}