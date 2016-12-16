package de.westwing.printer.ninja;

import de.westwing.printer.ninja.lib.Debug;
import de.westwing.printer.ninja.lib.PrinterFactory;
import de.westwing.printer.ninja.lib.chrome.io.MessageOutput;
import de.westwing.printer.ninja.lib.chrome.io.MessageReader;
import de.westwing.printer.ninja.lib.chrome.message.Message;
import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;
import de.westwing.printer.ninja.lib.message.JsonMessageParser;
import de.westwing.printer.ninja.lib.message.JsonPrintMessageInterface;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


/**
 * 
 * @author <omar.tchokhani@westwing.de>
 */
public class NinjaPrinter {
	protected MessageReader reader;
	protected MessageOutput writer;

	protected Debug debugService;
	
	/**
	 * @param ins
	 * @param out
	 */
	public NinjaPrinter(MessageReader ins, MessageOutput out) {
		this.reader = ins;
		this.writer =  out;
	}
	
	/**
	 * Usage: java -jar NinjaPrinter.jar [--debug=on]
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		if (args.length >= 1 && args[0].equalsIgnoreCase("--debug=on")) {
			showDebugWindow();
		}
		
		MessageReader reader = new MessageReader(new BufferedInputStream(System.in));
		MessageOutput writer = new MessageOutput(System.out);
		
		new NinjaPrinter(reader, writer).start();
	}
	
	/**
	 * Starts listening for a print requests on the input stream.
	 */
	public void start() {
		getDebugService().print("START");
		
		JsonPrintMessageInterface printMessage = null;
		MessageInterface message = null;
		
		try {
			while(true) {
				getDebugService().print("Waiting for print requests...");
				if (this.reader.available() > 0) {
					break;
				}
			}

			getDebugService().print("Print request receieved.... processing...");
			
			// Read message.
			message = reader.read();
			getDebugService().print("Raw message: " + message);
			printMessage = JsonMessageParser.getInstance().parse(message);
			getDebugService().print("Parsed message: " + printMessage);
			// Send document to printer.
			PrinterFactory.factory(printMessage)
							.enqueue(printMessage.getDocument())
							.print();
			getDebugService().print("Document sent to printer");
			// Send response back.
			writer.write(this.getSuccessMessage(printMessage));

			getDebugService().print("Processing completed. ");
		} catch (Exception ex) {
			for (StackTraceElement ste : ex.getStackTrace()) {
				getDebugService().print(ste.toString());
			}

			getDebugService().print("Exception thrown:" + ex.getMessage());
			try {
				this.writer.write(this.getErrorMessage(printMessage, ex));
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
			ex.printStackTrace();
		} finally {
			getDebugService().print("END");
		}
		
	}
	
	/**
	 * @param printMessage
	 * @return A concrete message object
	 */
	protected MessageInterface getSuccessMessage(JsonPrintMessageInterface printMessage)
	{
		String response = "{\"success\":true,\"message\":\"success\", \"requestId\":\"" + printMessage.getRequestId() + "\"}";
		MessageInterface responseMessage = new Message(response);

		getDebugService().print("Response success: " + responseMessage);
		
		return responseMessage;
	}
	
	/**
	 * @param printMessage
	 * @param ex
	 * @return a concrete message object, encapsulating the error.
	 */
	protected MessageInterface getErrorMessage(JsonPrintMessageInterface printMessage, Exception ex)
	{ 
		String requestId = printMessage == null ? null : printMessage.getRequestId();
		
		String response = "{\"success\":false,\"message\":\"" + ex.getMessage() + "\", \"requestId\":\"" + requestId + "\"}";
		MessageInterface responseMessage = new Message(response);

		getDebugService().print("Response error: " + responseMessage);
		
		return responseMessage;
	}

	/**
	 * Used for debugging only.
	 * Displays a window containing debugging message.
	 * Routes the default System error output to the debugging window.
	 */
	public static void showDebugWindow() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.setTitle("Ninja Printer - Debug Console");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(800, 400);
				frame.setLocationRelativeTo(null);

				final JTextArea textArea = new JTextArea();
				JScrollPane pane = new JScrollPane(textArea);
				frame.getContentPane().add(pane, BorderLayout.CENTER);

				System.setErr(new PrintStream(new OutputStream() {
					@Override
					public void write(int b) throws IOException {
						// redirects data to the text area
						textArea.append(String.valueOf((char) b));
						// scrolls the text area to the end of data
						textArea.setCaretPosition(textArea.getDocument().getLength());
					}
				}));

				frame.setVisible(true);
			}
		});
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
