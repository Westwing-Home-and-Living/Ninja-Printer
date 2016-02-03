package de.westwing.printer.ninja.lib;

import java.awt.print.PrinterJob;
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
	 * @param base64Content
	 */
	public static byte[] base64decode(String base64Content) {
		return DatatypeConverter.parseBase64Binary(base64Content);
	}

	/**
	 * @param decodedString
	 */
	public static String base64encode(String decodedString) {
		return DatatypeConverter.printBase64Binary(decodedString.getBytes());
	}

	/**
	 * @param decodedString
	 */
	public static String base64encode(byte[] bytes) {
		return DatatypeConverter.printBase64Binary(bytes);
	}

	/**
	 * Read the message size from Chrome.
	 * 
	 * @param bytes
	 * @return
	 */
	public static int getInt(byte[] bytes) {
		return (bytes[3] << 24) & 0xff000000 | (bytes[2] << 16) & 0x00ff0000 | (bytes[1] << 8) & 0x0000ff00
				| (bytes[0] << 0) & 0x000000ff;
	}

	/**
	 * Transform the length into the 32-bit message length.
	 * 
	 * @param length
	 * @return
	 */
	public static byte[] getBytes(int length) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (length & 0xFF);
		bytes[1] = (byte) ((length >> 8) & 0xFF);
		bytes[2] = (byte) ((length >> 16) & 0xFF);
		bytes[3] = (byte) ((length >> 24) & 0xFF);
		return bytes;
	}
}
