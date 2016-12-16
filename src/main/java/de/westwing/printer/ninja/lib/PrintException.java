package de.westwing.printer.ninja.lib;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 *
 */
public class PrintException extends Exception {

	/**
	 * @param message
	 */
	public PrintException(String message) {
		this(message, null);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PrintException(String message, Throwable cause) {
		super(message, cause);
	}
}
