package de.westwing.printer.ninja.lib;

import java.util.ArrayList;
import javax.print.PrintService;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class UtilitiesTest {

    @Test(expected = PrintException.class)
    public void lookupPrinterServiceByNameThrowsExceptionWhenPrinterNameIsNotFound() throws Exception {
        Utilities utility = new Utilities();

        PrintService[] printServices = new PrintService[0];

        utility.setPrintServices(printServices);

        utility.lookupPrinterServiceByName("dummy");
    }

    @Test
    public void lookupPrinterServiceByNameDoesNotThrowExceptionWhenPrinterNameIsProvided() throws Exception {
        Utilities utility = new Utilities();

        PrintService mockPrintService = Mockito.mock(PrintService.class);

        when(mockPrintService.getName()).thenReturn("dummy");

        ArrayList list = new ArrayList();

        list.add(mockPrintService);

        PrintService[] printServices = (PrintService[]) (list.toArray(new PrintService[list.size()]));

        utility.setPrintServices(printServices);

        utility.lookupPrinterServiceByName("dummy");

    }
}
