package de.westwing.printer.ninja.lib.message;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;
import de.westwing.printer.ninja.lib.document.DocumentInterface;
import de.westwing.printer.ninja.lib.document.ByteDocumentFactory;
import de.westwing.printer.ninja.lib.document.Base64DocumentFactory;

public class JsonMessageParser {
	
	public static final String JSON_PRINTER_NAME = "printerName";
	public static final String JSON_PRINTER_TYPE = "printerType";
	public static final String JSON_PRINTER_FILE_CONTENT = "fileContent";
	public static final String JSON_PRINTER_FILE_PATH = "filePath";
	public static final String JSON_REQUEST_ID = "requestId";
	
	private static JsonMessageParser instance;

	protected ByteDocumentFactory byteDocumentFactory;
	protected Base64DocumentFactory base64DocumentFactory;


	/**
	 * This is meant to be used as Singleton.
	 * @see JsonMessageParser.getInstance()
	 */
	private JsonMessageParser(ByteDocumentFactory byteDocumentFactory, Base64DocumentFactory base64DocumentFactory){
		this.byteDocumentFactory = byteDocumentFactory;
		this.base64DocumentFactory = base64DocumentFactory;
	}

	/**
	 * Disable cloning of the Singleton object.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * @return The singleton instance of the JsonMessageParser.
	 */
	public static JsonMessageParser getInstance(ByteDocumentFactory byteDocumentFactory, Base64DocumentFactory base64DocumentFactory) {
		if (null == instance) {
			instance = new JsonMessageParser(byteDocumentFactory, base64DocumentFactory);
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
	public JsonPrintMessageInterface getPrintMessage(MessageInterface message) throws JSONException, IOException {

		JSONObject json = new JSONObject(message.getBody());
		String printerName = json.getString("printerName");
		String printerType = json.getString("printerType");
		DocumentInterface document = null;


		if (json.has("filePath")) {
			String filePath = json.getString("filePath");
			try {
				document = this.byteDocumentFactory.createByteDocumentFromUrl(filePath, json.getString("cookie"));
			} catch (MalformedURLException $ex) {
				document = this.byteDocumentFactory.createByteDocumentFromFileContent(filePath);
			}
		} else {
			document = this.base64DocumentFactory.createBase64Document(json.getString("fileContent"));
		}
		
		JsonPrintMessage printMessage = new JsonPrintMessage(message);
		printMessage.setPrinterName(printerName);
		printMessage.setPrinterType(printerType);
		printMessage.setDocument(document);
		
		if(json.has("requestId")) {
			printMessage.setRequestId(json.getString("requestId"));
		}

		return printMessage;
	}
	

}
