package de.westwing.printer.ninja.lib;

import com.sun.pdfview.PDFFile;
import java.io.IOException;

public class PDFPrintA4PageFactory {
    public PDFPrintA4Page factory(PDFFile pdfFile) throws IOException {
        return new PDFPrintA4Page(pdfFile);
    }
}
