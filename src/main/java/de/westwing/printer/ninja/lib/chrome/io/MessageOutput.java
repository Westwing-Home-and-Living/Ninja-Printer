package de.westwing.printer.ninja.lib.chrome.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;

public class MessageOutput implements MessageOutputInterface {

	protected OutputStream out;
	
	public MessageOutput(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public int write(MessageInterface message) throws IOException {
		
		this.out.write(getBytes(message.getLength()));
		this.out.write(message.getBody().getBytes(Charset.forName("UTF-8")));
		
		return message.getLength();
	}
	
	protected static byte[] getBytes(int length) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (length & 0xFF);
		bytes[1] = (byte) ((length >> 8) & 0xFF);
		bytes[2] = (byte) ((length >> 16) & 0xFF);
		bytes[3] = (byte) ((length >> 24) & 0xFF);
		return bytes;
	}
}
