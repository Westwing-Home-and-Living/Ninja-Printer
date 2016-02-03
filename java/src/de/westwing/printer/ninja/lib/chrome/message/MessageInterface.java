package de.westwing.printer.ninja.lib.chrome.message;

public interface MessageInterface {
	
	public int getLength();
	
	public String getBody();
	
	public void setBody(String body);
	
	public void setId(int id);
	
	public int getId();
}
