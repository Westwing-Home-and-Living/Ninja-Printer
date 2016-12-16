package de.westwing.printer.ninja.lib;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;

import jzebra.PrintRaw;
import de.westwing.printer.ninja.NinjaPrinter;
import de.westwing.printer.ninja.lib.document.DocumentInterface;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 *
 */
public class LabelPrinter extends AbstractPrinter {

	protected Debug debugService;

	/**
	 * @param printService
	 */
	public LabelPrinter(PrintService printService) {
		this.setPrintService(printService);
		this.disableConsoleLogging();
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
				PrintRaw p = new Utf8PrintRaw(this.printService, temp);
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
		rootLogger.removeHandler(rootLogger.getHandlers()[0]);
	}

	/**
	 * 
	 * @author o.tchokhani
	 *
	 */
	public static class Utf8PrintRaw extends PrintRaw {
		/**
		 * 
		 * @param ps
		 * @param printString
		 * @throws UnsupportedEncodingException
		 */
		public Utf8PrintRaw(PrintService ps, String printString) throws UnsupportedEncodingException {
			super(ps, printString, DocFlavor.BYTE_ARRAY.AUTOSENSE, null, new HashPrintRequestAttributeSet(),
					java.nio.charset.Charset.forName("UTF-8"));
		}
	}

	/**
	 * @return Debug
	 */
	protected Debug getDebugService() {
		if (debugService == null) {
			debugService = new Debug();
		}

		return debugService;
	}
}
