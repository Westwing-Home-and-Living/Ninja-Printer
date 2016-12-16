package de.westwing.printer.ninja.lib;

import javax.print.PrintService;
import java.io.UnsupportedEncodingException;

/**
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public class PrintRawFactory {

    /**
     * @param printService
     * @param printString
     * @return Utf8PrintRaw
     * @throws UnsupportedEncodingException
     */
    public Utf8PrintRaw createUtf8PrintRaw(PrintService printService, String printString) throws UnsupportedEncodingException {
        return new Utf8PrintRaw(printService, printString);
    }

}
