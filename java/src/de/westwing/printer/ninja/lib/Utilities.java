package de.westwing.printer.ninja.lib;

import java.util.Optional;
import java.awt.print.PrinterJob;
import javax.print.PrintService;

import de.westwing.printer.ninja.lib.message.JsonPrintMessageInterface;

/**
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public class Utilities
{
	protected PrintService[] printServices;

	/*
	 * @param printMessage
	 * 
	 * @throws Exception
	 */
	public PrintService lookupPrinterService(JsonPrintMessageInterface printMessage) throws Exception {
		Optional<PrintService> printService = this.lookupPrinterServiceByName(printMessage.getPrinterName());

		if (printService.isPresent()) {
			getDebugService().print("Primary printer found: " + printMessage.getPrinterName());
			return printService.get();
		}

		getDebugService().print("Primary printer: " + printMessage.getPrinterName() + " not found");

		if (printMessage.getSecondaryPrinterName() != null) {
			getDebugService().print("Trying secondary printer: " + printMessage.getSecondaryPrinterName());
			printService = this.lookupPrinterServiceByName(printMessage.getSecondaryPrinterName());

			if (printService.isPresent()) {
				getDebugService().print("Secondary printer found: " + printMessage.getSecondaryPrinterName());
				return printService.get();
			}
		}

		throw new PrintException("Printer not found: " + printMessage.getPrinterName());
	}

	/**
	 * @param printerName
	 */
	protected Optional<PrintService> lookupPrinterServiceByName(String printerName) {
		// list of printers
		PrintService[] services = this.getPrintServices();

		for (PrintService service : services) {
			if (service.getName().contains(printerName)) {
				return Optional.of(service);
			}
		}

		return Optional.ofNullable(null);
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

	/**
	 * @return Debug
	 */
	protected Debug getDebugService() {
		return Debug.getInstance();
	}
}
