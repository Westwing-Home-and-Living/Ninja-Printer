package de.westwing.printer.ninja.lib;

import java.awt.print.PrinterJob;
import javax.print.PrintService;

/**
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public class Utilities
{
	protected PrintService[] printServices;

	/**
	 * @param printerName
	 * 
	 * @throws PrintException
	 */
	public PrintService lookupPrinterServiceByName(String printerName) throws Exception {
		// list of printers
		PrintService[] services = this.getPrintServices();

		for (PrintService service : services) {
			if (service.getName().contains(printerName)) {
				return service;
			}
		}

		throw new PrintException("Printer not found: " + printerName);
	}

	/**
	 * @return PrintService[]
	 */
	protected PrintService[] getPrintServices() {
		if (null == this.printServices) {
			this.printServices = PrinterJob.lookupPrintServices();
		}

		return this.printServices;
	}

	/**
	 * @param printServices
	 */
	public void setPrintServices(PrintService[] printServices) {
		this.printServices = printServices;
	}
}
