package de.westwing.printer.ninja.lib.chrome.message;

import de.westwing.printer.ninja.lib.document.DocumentInterface;

public class Message implements MessageInterface {
	
	protected String body;
	
	protected DocumentInterface document;
	
	protected int id;
	
	public Message(String body)
	{
		this.setBody(body);
	}
	
	@Override public void setBody(String body) {
		this.body = body;
	}
	
	@Override public String getBody() {
		return this.body;
	}
	
	@Override public void setId(int id) {
		this.id = id;
	}
	
	@Override public int getId() {
		return this.id;
	}
	
	@Override public int getLength() {
		return this.body.length();
	}
	
	@Override public String toString() {
		return this.body;
	}
}
