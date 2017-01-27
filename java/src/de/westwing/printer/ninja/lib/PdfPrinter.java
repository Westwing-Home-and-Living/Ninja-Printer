package de.westwing.printer.ninja.lib;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import com.sun.pdfview.PDFFile;
import de.westwing.printer.ninja.lib.document.DocumentInterface;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public class PdfPrinter extends AbstractPrinter {

	protected PrinterJob printerJob;

	protected PDFFileFactory pdfFileFactory;

	protected PDFPrintA4PageFactory pdfPrintA4PageFactory;

	/**
	 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
	 * @param printService
	 */
	public PdfPrinter(PrintService printService, PDFFileFactory pdfFileFactory, PDFPrintA4PageFactory pdfPrintA4PageFactory) {
		this(printService, PrinterJob.getPrinterJob(), pdfFileFactory, pdfPrintA4PageFactory);
	}

	/**
	 *
	 * @param printService
	 * @param printerJob
	 * @param pdfFileFactory
	 * @param pdfPrintA4PageFactory
	 */
	public PdfPrinter(PrintService printService, PrinterJob printerJob, PDFFileFactory pdfFileFactory, PDFPrintA4PageFactory pdfPrintA4PageFactory) {
		setPrintService(printService);
		setPrinterJob(printerJob);
		setPdfFileFactory(pdfFileFactory);
		setPdfPrintA4PageFactory(pdfPrintA4PageFactory);
	}

	/**
	 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
	 */
	@Override
	public void print() throws PrintException {
		if (this.documentsQueue.isEmpty()) {
			throw new PrintException("Printer queue is empty.");
		}

		try {
			this.printerJob.setPrintService(this.getPrintService());
			PageFormat pageFormat = this.printerJob.defaultPage();
			pageFormat = this.printerJob.validatePage(pageFormat);

			for (DocumentInterface document : this.documentsQueue) {
				PDFFile pdfFile = this.getPdfFileFactory().factory(document);
				PDFPrintA4Page pages = this.getPdfPrintA4PageFactory().factory(pdfFile);
				pageFormat.setPaper(pages.getPaper());
				// Setting up book for papers
				Book book = new Book();
				book.append(pages, pageFormat, pdfFile.getNumPages());

				printerJob.setPageable(book);

				printerJob.print(pages.getRequestAttributes());
			}

		} catch (Exception ex) {
			throw new PrintException(ex.getMessage(), ex);
		}
	}

	/**
	 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
	 * @param printerJob
	 */
	public void setPrinterJob(PrinterJob printerJob) {
		this.printerJob = printerJob;
	}

	/**
	 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
	 * @return The printer job to be used.
	 */
	public PrinterJob getPrinterJob() {
		return printerJob;
	}

	/**
	 *
	 * @return PDFFileFactory
	 */
	public PDFFileFactory getPdfFileFactory() {
		return pdfFileFactory;
	}

	/**
	 *
	 * @param pdfFileFactory
	 */
	public void setPdfFileFactory(PDFFileFactory pdfFileFactory) {
		this.pdfFileFactory = pdfFileFactory;
	}

	/**
	 *
	 * @return PDFPrintA4PageFactory
	 */
	public PDFPrintA4PageFactory getPdfPrintA4PageFactory() {
		return pdfPrintA4PageFactory;
	}

	/**
	 *
	 * @param pdfPrintA4PageFactory
	 */
	public void setPdfPrintA4PageFactory(PDFPrintA4PageFactory pdfPrintA4PageFactory) {
		this.pdfPrintA4PageFactory = pdfPrintA4PageFactory;
	}
}
