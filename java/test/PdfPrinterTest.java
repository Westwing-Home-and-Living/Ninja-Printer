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
    private PDFFileFactory pdfFileFactory;
    private PDFPrintA4PageFactory pdfPrintA4PageFactory;
    private ByteDocument doc1;
    private PrintService printService;
    private PrinterJob printerJob;

    @Before
    public void setUp() throws IOException{
        String expectedMessage = "Hello!";

        pdfFileFactory = Mockito.mock(PDFFileFactory.class);
        pdfPrintA4PageFactory = Mockito.mock(PDFPrintA4PageFactory.class);
        doc1 = new ByteDocument(expectedMessage.getBytes());
        printService = Mockito.mock(PrintService.class);
        printerJob = Mockito.mock(PrinterJob.class);

        PDFFile pdfFile = Mockito.mock(PDFFile.class);
        PDFPrintA4Page pdfPrintA4Page = Mockito.mock(PDFPrintA4Page.class);

        when(pdfFileFactory.factory(doc1)).thenReturn(pdfFile);
        when(pdfPrintA4PageFactory.factory(pdfFile)).thenReturn(pdfPrintA4Page);

        when(printerJob.defaultPage()).thenReturn(Mockito.mock(PageFormat.class));
        when(printerJob.validatePage(printerJob.defaultPage())).thenReturn(Mockito.mock(PageFormat.class));
    }

    @Test
    public void testBasicPrintingWorkflowOneDocument() throws Exception {
        PdfPrinter pdfPrinter = new PdfPrinter(printService, printerJob, pdfFileFactory, pdfPrintA4PageFactory);
        pdfPrinter.enqueue(doc1);
        pdfPrinter.print();

        verify(printerJob, times(1)).print(null);
    }

    @Test(expected=PrintException.class)
    public void testBasicPrintingWorkflowNoDocuments() throws Exception {
        PdfPrinter pdfPrinter = new PdfPrinter(printService, printerJob, pdfFileFactory, pdfPrintA4PageFactory);
        pdfPrinter.print();
    }
}