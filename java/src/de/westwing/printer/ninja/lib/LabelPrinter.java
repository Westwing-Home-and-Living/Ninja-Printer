package de.westwing.printer.ninja.lib;

import java.util.logging.Logger;

import javax.print.PrintService;

import jzebra.PrintRaw;
import de.westwing.printer.ninja.NinjaPrinter;
import de.westwing.printer.ninja.lib.document.DocumentInterface;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 *
 */
public class LabelPrinter extends AbstractPrinter {

	private PrintRawFactory printRawFactory;
	/**
	 * @param printService
	 */
	public LabelPrinter(PrintService printService, PrintRawFactory printRawFactory) {
		this.setPrintService(printService);
		this.disableConsoleLogging();
		this.printRawFactory = printRawFactory;
	}

	@Override
	public void print() throws PrintException {
		if (this.documentsQueue.isEmpty()) {
			throw new PrintException("Printer queue is empty.");
		}

		try {
			NinjaPrinter.debug("DocQueue:" + this.documentsQueue.size());
			NinjaPrinter.debug("DocQueue:" + this.documentsQueue.toString());
			
			for (DocumentInterface document : this.documentsQueue) {
				NinjaPrinter.debug("Print begins");
				
				String temp = document.toRawString();
				NinjaPrinter.debug("RawString in print:" + temp);
				PrintRaw p = this.printRawFactory.createUtf8PrintRaw(this.printService, temp);
				NinjaPrinter.debug("UTF-8 print row:" + p.toString());
				p.print();
				NinjaPrinter.debug("Print done");
			}
		} catch (Exception ex) {
			NinjaPrinter.debug(ex.getMessage());
			throw new PrintException(ex.getMessage(), ex);
		}
	}

	/**
	 * Remove default ConsoleHandler to disable default logging to the console
	 * Used by JZEBRA or other libraries.
	 */
	protected void disableConsoleLogging() {
		Logger rootLogger = Logger.getLogger("");

		if (rootLogger.getHandlers().length > 0) {
			rootLogger.removeHandler(rootLogger.getHandlers()[0]);
		}
	}

}
