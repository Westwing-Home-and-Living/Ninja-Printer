package de.westwing.printer.ninja.lib;

import de.westwing.printer.ninja.lib.message.JsonPrintMessageInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.print.PrintService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PrinterFactoryTest
{
    protected PrinterFactory sut;

    protected PrintService printServiceMock;
    protected Utilities utilitiesServiceMock;
    protected JsonPrintMessageInterface printMessageMock;

    @Before
    public void setUp() throws Exception {
        printServiceMock = Mockito.mock(PrintService.class);
        utilitiesServiceMock = Mockito.mock(Utilities.class);
        printMessageMock = Mockito.mock(JsonPrintMessageInterface.class);

        sut = new PrinterFactory();
        sut.setUtilitiesService(utilitiesServiceMock);
    }

    @After
    public void tearDown() throws Exception {
        printServiceMock = null;
        sut = null;
    }

    @Test(expected=PrintException.class)
    public void testFactoryThrowsExceptionOnUnknownPrinterType() throws Exception {
        sut.factory("dummy", printServiceMock);
    }

    @Test
    public void testFactoryReturnsPdfPrinter() throws Exception {
        Object actualResult;
        Class expectedResult = PdfPrinter.class;

        actualResult = sut.factory(PrinterFactory.PrinterType.PDF.toString(), printServiceMock);

        assertEquals(expectedResult, actualResult.getClass());
    }

    @Test
    public void testFactoryReturnsLabelPrinter() throws Exception {
        Object actualResult;
        Class expectedResult = LabelPrinter.class;

        actualResult = sut.factory(PrinterFactory.PrinterType.LABEL.toString(), printServiceMock);

        assertEquals(expectedResult, actualResult.getClass());
    }
}
