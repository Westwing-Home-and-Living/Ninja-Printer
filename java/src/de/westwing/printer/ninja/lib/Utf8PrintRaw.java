package de.westwing.printer.ninja.lib;

import jzebra.PrintRaw;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.io.UnsupportedEncodingException;

/**
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
class Utf8PrintRaw extends PrintRaw {

    /**
     * @param printService
     * @param printString
     * @throws UnsupportedEncodingException
     */
    public Utf8PrintRaw(PrintService printService, String printString) throws UnsupportedEncodingException {
        super(
                printService,
                printString,
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null,
                new HashPrintRequestAttributeSet(),
                java.nio.charset.Charset.forName("UTF-8")
        );
    }
}
