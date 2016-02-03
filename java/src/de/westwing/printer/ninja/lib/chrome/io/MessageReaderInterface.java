package de.westwing.printer.ninja.lib.chrome.io;

import java.io.IOException;

import org.json.JSONException;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;

interface MessageReaderInterface {

	public MessageInterface read() throws IOException, JSONException;
	
	public int available() throws IOException;
}
