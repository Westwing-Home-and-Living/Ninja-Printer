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
			getDebugService().print("DocQueue:" + this.documentsQueue.size());
			getDebugService().print("DocQueue:" + this.documentsQueue.toString());
			
			for (DocumentInterface document : this.documentsQueue) {
				getDebugService().print("Print begins");
				
				String temp = document.toRawString();
				getDebugService().print("RawString in print:" + temp);
				PrintRaw p = this.printRawFactory.createUtf8PrintRaw(this.printService, temp);
				getDebugService().print("UTF-8 print row:" + p.toString());
				p.print();
				getDebugService().print("Print done");
			}
		} catch (Exception ex) {
			getDebugService().print(ex.getMessage());
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

	/**
	 * @return Debug
	 */
	protected Debug getDebugService() {
		return Debug.getInstance();
	}
}
