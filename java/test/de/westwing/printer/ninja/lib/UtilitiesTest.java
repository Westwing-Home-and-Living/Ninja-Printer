package de.westwing.printer.ninja.lib;

import java.util.ArrayList;
import javax.print.PrintService;

import org.junit.Test;
import org.mockito.Mockito;

import de.westwing.printer.ninja.lib.message.JsonPrintMessage;
import de.westwing.printer.ninja.lib.message.JsonPrintMessageInterface;

import static org.mockito.Mockito.*;

public class UtilitiesTest {

    @Test
    public void lookupPrinterServiceReturnsPrimaryPrinterIfFound() throws Exception {
        Utilities utility = new Utilities();

        JsonPrintMessageInterface mockPrintMessage = Mockito.mock(JsonPrintMessageInterface.class);
        when(mockPrintMessage.getPrinterName()).thenReturn("primary");

        PrintService mockPrintService = Mockito.mock(PrintService.class);
        when(mockPrintService.getName()).thenReturn("primary");

        ArrayList list = new ArrayList();
        list.add(mockPrintService);
        PrintService[] printServices = (PrintService[]) (list.toArray(new PrintService[list.size()]));
        utility.setPrintServices(printServices);

        utility.lookupPrinterService(mockPrintMessage);
    }

    @Test
    public void lookupPrinterServiceReturnsSecondaryPrinterIfPrimaryPrinterIsNotFound() throws Exception {
        Utilities utility = new Utilities();

        JsonPrintMessageInterface mockPrintMessage = Mockito.mock(JsonPrintMessageInterface.class);
        when(mockPrintMessage.getPrinterName()).thenReturn("primary");
        when(mockPrintMessage.getSecondaryPrinterName()).thenReturn("secondary");

        PrintService mockPrintService = Mockito.mock(PrintService.class);
        when(mockPrintService.getName()).thenReturn("secondary");

        ArrayList list = new ArrayList();
        list.add(mockPrintService);
        PrintService[] printServices = (PrintService[]) (list.toArray(new PrintService[list.size()]));
        utility.setPrintServices(printServices);

        utility.lookupPrinterService(mockPrintMessage);
    }

    @Test(expected = PrintException.class)
    public void lookupPrinterServiceThrowsExceptionWhenPrintersAreNotFound() throws Exception {
        Utilities utility = new Utilities();

        JsonPrintMessageInterface mockPrintMessage = Mockito.mock(JsonPrintMessageInterface.class);
        when(mockPrintMessage.getPrinterName()).thenReturn("primary");
        when(mockPrintMessage.getSecondaryPrinterName()).thenReturn("secondary");

        PrintService[] printServices = new PrintService[0];
        utility.setPrintServices(printServices);

        utility.lookupPrinterService(mockPrintMessage);
    }
}
