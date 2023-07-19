package de.westwing.printer.ninja.lib.message;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import de.westwing.printer.ninja.lib.Debug;
import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;
import de.westwing.printer.ninja.lib.document.Base64Document;
import de.westwing.printer.ninja.lib.document.ByteDocument;
import de.westwing.printer.ninja.lib.document.DocumentInterface;

public class JsonMessageParser {
	
	public static final String JSON_PRINTER_NAME = "printerName";
	public static final String JSON_PRINTER_TYPE = "printerType";
	public static final String JSON_PRINTER_FILE_CONTENT = "fileContent";
	public static final String JSON_PRINTER_FILE_PATH = "filePath";
	public static final String JSON_REQUEST_ID = "requestId";
	
	private static JsonMessageParser instance;
	
	/**
	 * This is meant to be used as Singleton.
	 * @see JsonMessageParser.getInstance()
	 */
	private JsonMessageParser() {
		
	}

	/**
	 * Disable cloning of the Singleton object.
	 */
	@Override public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}
	
	/**
	 * @return The singleton instance of the JsonMessageParser.
	 */
	public static JsonMessageParser getInstance() {
		if (null == instance) {
			instance = new JsonMessageParser();
		}
		
		return instance;
	}
	
	/**
	 * Parses a chrome message into a JsonPrintMessageInterface object.
	 * 
	 * @param message
	 * @return a concrete JsonPrintMessageInterface object.
	 * @throws JSONException
	 * @throws IOException
	 */
	public JsonPrintMessageInterface parse(MessageInterface message) throws JSONException, IOException {
		
		JSONObject json = new JSONObject(message.getBody());
		String printerName = json.getString("printerName");
		String printerType = json.getString("printerType");
		String secondaryPrinterName = null;
		DocumentInterface document = null;
		
		if (json.has("filePath")) {
			String filePath = json.getString("filePath");
			try {
				URL fileUrl = new URL(filePath);
				document = loadRemoteFile(fileUrl, json.getString("cookie"));
			} catch (MalformedURLException $ex) {
				document = loadLocalFile(filePath);
			}
		} else {
			document = new Base64Document(json.getString("fileContent"));
		}

		if (json.has("secondaryPrinterName") && !json.isNull("secondaryPrinterName")) {
			secondaryPrinterName = json.getString("secondaryPrinterName");
		}
		
		JsonPrintMessage  printMessage = new JsonPrintMessage(message);
		printMessage.setPrinterName(printerName);
		printMessage.setPrinterType(printerType);
		printMessage.setSecondaryPrinterName(secondaryPrinterName);
		printMessage.setDocument(document);
		
		if(json.has("requestId")) {
			printMessage.setRequestId(json.getString("requestId"));
		}
		return printMessage;
		
	}
	
	/**
	 * @param filePath
	 * @return A concrete DocumentInterface object.
	 * @throws IOException
	 */
	protected static DocumentInterface loadLocalFile(String filePath) throws IOException
	{
		DataInputStream dis = null;
		File file = null;
		byte[] fileContent = null;
		
		try {
			file = new File(filePath);
			fileContent = new byte[(int) file.length()];
			dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(fileContent);
			
			return new ByteDocument(fileContent);
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != dis) {
				dis.close();
			}
		}
	}
	
	/**
	 * @param url
	 * @param cookie
	 * @return A concrete DocumentInterface object.
	 * @throws IOException
	 */
	protected static DocumentInterface loadRemoteFile(URL url, String cookie) throws IOException
	{
		URLConnection urlConnection = null;
		BufferedInputStream ins = null;
		ByteArrayOutputStream out = null;
		
		try {
			urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Cookie", cookie);
			urlConnection.connect();
			ins = new BufferedInputStream(urlConnection.getInputStream());
			out = new ByteArrayOutputStream();
			
			byte[] byteChunk = new byte[1024];
			int n;

			while ( (n = ins.read(byteChunk)) > 0 ) {
			  out.write(byteChunk, 0, n);
			}
			
			return new ByteDocument(out.toByteArray());
			
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != ins) ins.close();
			if (null != out) out.close();
		}
	}

	/**
	 * @return Debug
	 */
	protected Debug getDebugService() {
		return Debug.getInstance();
	}
}
