import com.sun.pdfview.PDFFile;
import de.westwing.printer.ninja.lib.*;
import de.westwing.printer.ninja.lib.document.ByteDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.print.PrintService;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class PdfPrinterTest {
    private PDFFileFactory pdfFileFactoryMock;
    private PDFPrintA4PageFactory pdfPrintA4PageFactoryMock;
    private ByteDocument docMock;
    private PrintService printServiceMock;
    private PrinterJob printerJobMock;
    private PdfPrinter sut;

    @Before
    public void setUp() throws IOException{
        String expectedMessage = "dummy!";

        pdfFileFactoryMock = Mockito.mock(PDFFileFactory.class);
        pdfPrintA4PageFactoryMock = Mockito.mock(PDFPrintA4PageFactory.class);
        docMock = new ByteDocument(expectedMessage.getBytes());
        printServiceMock = Mockito.mock(PrintService.class);
        printerJobMock = Mockito.mock(PrinterJob.class);

        PDFFile pdfFile = Mockito.mock(PDFFile.class);
        PDFPrintA4Page pdfPrintA4Page = Mockito.mock(PDFPrintA4Page.class);

        sut = new PdfPrinter(printServiceMock, printerJobMock, pdfFileFactoryMock, pdfPrintA4PageFactoryMock);

        when(pdfFileFactoryMock.factory(docMock)).thenReturn(pdfFile);
        when(pdfPrintA4PageFactoryMock.factory(pdfFile)).thenReturn(pdfPrintA4Page);

        when(printerJobMock.defaultPage()).thenReturn(Mockito.mock(PageFormat.class));
        when(printerJobMock.validatePage(printerJobMock.defaultPage())).thenReturn(Mockito.mock(PageFormat.class));
    }

    @Test
    public void testPrintSuccess() throws Exception {
        sut.enqueue(docMock);
        sut.print();

        verify(printerJobMock, times(1)).print(null);
    }

    @Test(expected=PrintException.class)
    public void testPrintFailsOnEmptyDocument() throws Exception {
        sut.print();
    }

    @Test(expected=PrintException.class)
    public void testPrintFailsWithPrintException() throws Exception {
        when(printerJobMock.defaultPage()).thenThrow(Exception.class);
        sut.print();
    }
}