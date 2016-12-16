package de.westwing.printer.ninja.lib;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

import de.westwing.printer.ninja.lib.document.DocumentInterface;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public class PdfPrinter extends AbstractPrinter {

	protected PrinterJob printerJob;

	/**
	 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
	 * @param printService
	 */
	public PdfPrinter(PrintService printService) {
		this(printService, PrinterJob.getPrinterJob());
	}

	/**
	 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
	 * @param printService
	 * @param printerJob
	 */
	public PdfPrinter(PrintService printService, PrinterJob printerJob) {
		setPrintService(printService);
		setPrinterJob(printerJob);
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
				PDFFile pdfFile = new PDFFile(document.toByteBuffer());
				PdfPrinter.PDFPrintA4Page pages = new PdfPrinter.PDFPrintA4Page(pdfFile);
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
	 * PDFPrintA4Page force correct paper size. This class will rasterize a
	 * PDFPage into a Graphics2D
	 * 
	 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
	 */
	public static class PDFPrintA4Page implements Printable {

		protected PDFFile pdfFile;

		protected Paper paper;

		/**
		 * 
		 * @param pdfFile
		 */
		public PDFPrintA4Page(PDFFile pdfFile) {
			this.pdfFile = pdfFile;
		}

		/**
		 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
		 */
		public PrintRequestAttributeSet getRequestAttributes() {
			PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
			attributes.add(OrientationRequested.PORTRAIT);
			attributes.add(MediaSizeName.ISO_A4);

			return attributes;
		}

		/**
		 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
		 * @return A4 configured Paper.
		 */
		public Paper getPaper() {
			if (null == paper) {
				paper = new Paper();
				paper.setSize(595, 842); // A4 dimensions in font points
				paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
			}

			return paper;
		}

		/**
		 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
		 */
		@Override
		public int print(Graphics g, PageFormat format, int index) throws PrinterException {
			int pagenum = index + 1;

			// don't bother if the page number is out of range.
			if ((pagenum >= 1) && (pagenum <= pdfFile.getNumPages())) {
				// fit the PDFPage into the printing area
				Graphics2D g2 = (Graphics2D) g;
				PDFPage page = pdfFile.getPage(pagenum);

				Rectangle imgbounds = this.computeImaginableArea(page, format);
				// render the page
				PDFRenderer pgs = new PDFRenderer(page, g2, imgbounds, null, null);
				try {
					page.waitForFinish();
					pgs.run();
				} catch (InterruptedException ie) {
				}

				return PAGE_EXISTS;
			} else {
				return NO_SUCH_PAGE;
			}
		}

		/**
		 * 
		 * @param page
		 * @param format
		 * @return Rectangle the image bounds.
		 */
		protected Rectangle computeImaginableArea(PDFPage page, PageFormat format) {
			double pwidth = format.getImageableWidth();
			double pheight = format.getImageableHeight();

			double aspect = page.getAspectRatio();
			double paperaspect = pwidth / pheight;

			if (aspect > paperaspect) {
				// paper is too tall / pdfpage is too wide
				int height = (int) (pwidth / aspect);

				return new Rectangle((int) format.getImageableX(),
						(int) (format.getImageableY() + ((pheight - height) / 2)), (int) pwidth, height);
			}

			// paper is too wide / pdfpage is too tall
			int width = (int) (pheight * aspect);

			return new Rectangle((int) (format.getImageableX() + ((pwidth - width) / 2)), (int) format.getImageableY(),
					width, (int) pheight);

		}
	}
}
