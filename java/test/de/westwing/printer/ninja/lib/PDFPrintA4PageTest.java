package de.westwing.printer.ninja.lib;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.print.PageFormat;

import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PDFPrintA4PageTest {
    protected PDFPrintA4Page sut;
    protected PDFFile pdfFileMock;
    protected PageFormat pageFormatMock;
    protected Graphics2D graphicsMock;
    protected PDFPage pdfPageMock;

    @Before
    public void setUp() throws Exception {
        graphicsMock = Mockito.spy(Graphics2D.class);
        pageFormatMock = Mockito.mock(PageFormat.class);
        pdfFileMock = Mockito.mock(PDFFile.class);
        sut = new PDFPrintA4Page(pdfFileMock);
        pdfPageMock = Mockito.mock(PDFPage.class);

        when(pdfFileMock.getPage(3)).thenReturn(pdfPageMock);
        when(pdfFileMock.getNumPages()).thenReturn(3);
        when(pageFormatMock.getImageableWidth()).thenReturn((double)800);
        when(pageFormatMock.getImageableHeight()).thenReturn((double)600);
        when(pdfPageMock.getAspectRatio()).thenReturn((float)3);
        when(pdfPageMock.isFinished()).thenReturn(true);
    }

    @Test
    public void testPrintReturnsNoSuchPageWhenRequestIsOutOfBoundaries() throws Exception {
        when(pdfFileMock.getPage(1)).thenReturn(pdfPageMock);
        when(pdfFileMock.getNumPages()).thenReturn(0);
        assertEquals(NO_SUCH_PAGE, sut.print(graphicsMock, pageFormatMock, 0));
    }

    @Test
    public void testPrintReturnsPageExists() throws Exception {
        assertEquals(PAGE_EXISTS, sut.print(graphicsMock, pageFormatMock, 2));
    }

    @Test
    public void testPrintReturnsPageExistsWhenInterruptedExceptionIsThrown() throws Exception {
        Mockito.doThrow(InterruptedException.class).when(pdfPageMock).waitForFinish();

        assertEquals(PAGE_EXISTS, sut.print(graphicsMock, pageFormatMock, 2));
    }

    @Test
    public void testComputeImaginableAreaWherePageAspectIsHigerThenPaperAspect() throws Exception {
        Rectangle expectedResult = new Rectangle();
        expectedResult.setBounds(0, 167, 800, 266);

        Rectangle actualResult = sut.computeImaginableArea(pdfPageMock, pageFormatMock);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testComputeImaginableAreaWherePaperAspectIsHigherThenPageAspect() throws Exception {
        Rectangle expectedResult = new Rectangle();
        expectedResult.setBounds(397, 0, 5, 600);

        when(pdfPageMock.getAspectRatio()).thenReturn((float)0.01);

        Rectangle actualResult = sut.computeImaginableArea(pdfPageMock, pageFormatMock);

        assertEquals(expectedResult, actualResult);
    }
}