package de.westwing.printer.ninja.lib.message;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;
import de.westwing.printer.ninja.lib.document.DocumentInterface;

public class JsonPrintMessage implements JsonPrintMessageInterface {
	
	protected int length;
	
	protected String body;
	
	protected String printerName;
	
	protected String printerType;
	
	protected DocumentInterface document;
	
	protected int requestId;
	
	protected MessageInterface message;
	
	public JsonPrintMessage(MessageInterface message) {
		this.setMessage(message);
	}
	
	@Override public void setMessage(MessageInterface message) {
		this.message = message;
	}
	
	@Override public MessageInterface getMessage() {
		return this.message;
	}
	
	@Override public void setPrinterName(String printerName) {
		this.printerName =  printerName;
	}
	
	@Override public String getPrinterName() {
		return this.printerName;
	}
	
	@Override public String getPrinterType() {
		return this.printerType;
	}
	
	@Override public void setPrinterType(String type) {
		this.printerType = type;
	}

	@Override public DocumentInterface getDocument() {
		
		return this.document;
	}
	
	@Override public void setDocument(DocumentInterface document) {
		this.document = document;
	}

	@Override public int getRequestId() {
			
		return this.requestId;
	}
		
	@Override public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("PrnterType: ");
		sb.append(this.getPrinterType());
		sb.append(", PrinterName: ");
		sb.append(this.getPrinterName());
		sb.append(", RequestId: ");
		sb.append(this.getRequestId());
		sb.append("]");
		
		return sb.toString();
	}
}
