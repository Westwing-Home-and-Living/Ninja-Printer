
package de.westwing.printer.ninja.lib;

import java.util.ArrayList;

import javax.print.PrintService;

import de.westwing.printer.ninja.lib.document.DocumentInterface;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 *
 */
public abstract class AbstractPrinter implements PrinterInterface {

	protected ArrayList<DocumentInterface> documentsQueue = new ArrayList<DocumentInterface>();

	protected PrintService printService;

	/**
	 * @param document
	 */
	@Override
	public PrinterInterface enqueue(DocumentInterface document) {
		this.documentsQueue.add(document);
		
		return this;
	}

	/**
	 * @param document
	 * @return DocumentInterface
	 */
	@Override
	public DocumentInterface dequeue(DocumentInterface document) {
		int index = this.documentsQueue.indexOf(document);

		if (-1 != index) {
			return this.documentsQueue.remove(index);
		}

		return null;
	}

	/**
	 * @param printService
	 */
	public void setPrintService(PrintService printService) {
		this.printService = printService;
	}

	/**
	 * 
	 * @return PrintService
	 */
	public PrintService getPrintService() {
		return printService;
	}
}
