package de.westwing.printer.ninja.lib;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.print.PageFormat;

import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PDFPrintA4PageTest {
    @Test
    public void printWithNoSuchPageResult() throws Exception {
        Graphics graphics = Mockito.spy(Graphics.class);
        PageFormat pageFormat = Mockito.mock(PageFormat.class);
        PDFFile pdfFile = Mockito.mock(PDFFile.class);
        PDFPrintA4Page pdfPrintA4Page = new PDFPrintA4Page(pdfFile);

        assertEquals(NO_SUCH_PAGE, pdfPrintA4Page.print(graphics, pageFormat, 0));
    }

    @Test
    public void printWithPageExistsResult() throws Exception {
        PDFFile pdfFile = Mockito.mock(PDFFile.class);
        PDFPrintA4Page pdfPrintA4Page = new PDFPrintA4Page(pdfFile);
        PDFPage pdfPage = Mockito.mock(PDFPage.class);
        PageFormat pageFormat = Mockito.mock(PageFormat.class);
        Graphics2D graphics = Mockito.mock(Graphics2D.class);

        when(pageFormat.getImageableWidth()).thenReturn((double)800);
        when(pageFormat.getImageableHeight()).thenReturn((double)600);
        when(pdfFile.getPage(1)).thenReturn(pdfPage);

        when(pdfFile.getNumPages()).thenReturn(3);
        when(pdfPage.getAspectRatio()).thenReturn((float)3);
        when(pdfPage.isFinished()).thenReturn(true);
        when(pdfFile.getPage(3)).thenReturn(pdfPage);

        assertEquals(PAGE_EXISTS, pdfPrintA4Page.print(graphics, pageFormat, 2));
    }
}