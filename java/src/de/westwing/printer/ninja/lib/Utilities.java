package de.westwing.printer.ninja.lib;

import java.awt.print.PrinterJob;
import javax.print.PrintService;
import javax.xml.bind.DatatypeConverter;

/**
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public class Utilities
{
	/**
	 * @param printerName
	 * 
	 * @throws PrintException
	 */
	public PrintService lookupPrinterServiceByName(String printerName) throws Exception {
		// list of printers
		PrintService[] services = PrinterJob.lookupPrintServices();

		for (PrintService service : services) {
			if (service.getName().contains(printerName)) {
				return service;
			}
		}

		throw new PrintException("Printer not found: " + printerName);
	}
}
