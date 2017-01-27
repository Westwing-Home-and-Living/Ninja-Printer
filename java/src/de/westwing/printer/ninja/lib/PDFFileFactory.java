package de.westwing.printer.ninja.lib;

import com.sun.pdfview.PDFFile;
import de.westwing.printer.ninja.lib.document.DocumentInterface;

import java.io.IOException;

public class PDFFileFactory {
    public PDFFile factory(DocumentInterface document) throws IOException {
        return new PDFFile(document.toByteBuffer());
    }
}
