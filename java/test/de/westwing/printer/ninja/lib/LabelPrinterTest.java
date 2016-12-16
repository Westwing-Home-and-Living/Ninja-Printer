package de.westwing.printer.ninja.lib;

import de.westwing.printer.ninja.lib.document.DocumentInterface;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import javax.print.PrintService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Serhii Atiahin <serhii.atiahin@westwing.de>
 */
public class LabelPrinterTest {

    private PrintService printServiceMock;

    private PrintRawFactory printRawFactoryMock;

    private LabelPrinter printer;

    @Before
    public void setUp() throws Exception {
        this.printServiceMock = Mockito.mock(PrintService.class);
        this.printRawFactoryMock = Mockito.mock(PrintRawFactory.class);
        this.printer = new LabelPrinter(this.printServiceMock, this.printRawFactoryMock);
    }

    @Test
    public void testConstructor() throws Exception {
        assertEquals(this.printer.getPrintService(), this.printServiceMock);
    }

    @Test(expected = PrintException.class)
    public void toThrowExceptionForEmptyQueue() throws Exception {
        this.printer.print();
    }

    @Test
    public void toProcessDocumentsIfExistInOrder() throws Exception {
        DocumentInterface docMock1 = Mockito.mock(DocumentInterface.class);
        String expectedString1 = "Hello, 世界!";
        when(docMock1.toRawString()).thenReturn(expectedString1);

        DocumentInterface docMock2 = Mockito.mock(DocumentInterface.class);
        String expectedString2 = "Hello, something!";
        when(docMock2.toRawString()).thenReturn(expectedString2);

        this.printer.documentsQueue.add(docMock1);
        this.printer.documentsQueue.add(docMock2);

        assertEquals(2, this.printer.documentsQueue.size());

        Utf8PrintRaw printRawMock1 = mock(Utf8PrintRaw.class);
        when(printRawMock1.print()).thenReturn(true);

        Utf8PrintRaw printRawMock2 = mock(Utf8PrintRaw.class);
        when(printRawMock2.print()).thenReturn(true);

        when(this.printRawFactoryMock.createUtf8PrintRaw(anyObject(), eq(expectedString1)))
                .thenReturn(printRawMock1);
        when(this.printRawFactoryMock.createUtf8PrintRaw(anyObject(), eq(expectedString2)))
                .thenReturn(printRawMock2);

        this.printer.print();

        InOrder inDocOrder = inOrder(docMock1, docMock2);

        inDocOrder.verify(docMock1).toRawString();
        inDocOrder.verify(docMock2).toRawString();

        InOrder inPrintWarOrder = inOrder(printRawMock1, printRawMock2);

        inPrintWarOrder.verify(printRawMock1).print();
        inPrintWarOrder.verify(printRawMock2).print();
    }

    @Test(expected = PrintException.class)
    public void toThrowExceptionIfSomethingGoesWrong() throws Exception {
        DocumentInterface docMock = Mockito.mock(DocumentInterface.class);
        when(docMock.toRawString()).thenThrow(new RuntimeException());

        this.printer.documentsQueue.add(docMock);

        Utf8PrintRaw printRawMock = mock(Utf8PrintRaw.class);
        when(printRawMock.print()).thenReturn(true);

        when(this.printRawFactoryMock.createUtf8PrintRaw(anyObject(), anyString()))
                .thenReturn(printRawMock);

        this.printer.print();
    }
}
