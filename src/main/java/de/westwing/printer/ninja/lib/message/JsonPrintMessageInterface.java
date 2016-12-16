package de.westwing.printer.ninja.lib.message;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;
import de.westwing.printer.ninja.lib.document.DocumentInterface;

public interface JsonPrintMessageInterface {
	
	public String getPrinterType();
	
	public String getPrinterName();
	
	public String getRequestId();
	
	public void setRequestId(String id);
	
	public void setPrinterName(String printerName);
	
    public void setPrinterType(String printerType);
	
	public DocumentInterface getDocument();
	
	public void setDocument(DocumentInterface document);
	
	public void setMessage(MessageInterface message);
	
	public MessageInterface getMessage();
}
