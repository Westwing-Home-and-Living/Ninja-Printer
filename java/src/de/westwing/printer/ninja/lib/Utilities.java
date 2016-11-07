package de.westwing.printer.ninja.lib;

import de.westwing.printer.ninja.NinjaPrinter;

import java.awt.print.PrinterJob;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.print.PrintService;
import javax.xml.bind.DatatypeConverter;

/**
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public final class Utilities {

	/**
	 * @param printerName
	 * 
	 * @throws PrintException
	 */
	public static PrintService lookupPrinterServiceByName(String printerName) throws Exception {
		// list of printers
		PrintService[] services = PrinterJob.lookupPrintServices();

		for (PrintService service : services) {
			if (service.getName().contains(printerName)) {
				return service;
			}
		}

		throw new PrintException("Printer not found: " + printerName);
	}

	/**
	 * Returns the build version from the manifest file.
	 *
	 * @return String
	 */
	public static String getBuildVersion() {
		String buildVersion;

		try {
			String jarLocation = NinjaPrinter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

			Manifest m = new JarFile(jarLocation).getManifest();

			buildVersion = m.getMainAttributes().getValue("Build-Version");
		}
		catch (Exception ex)
		{
			buildVersion = "";
		}

		return buildVersion;
	}
}
