package de.westwing.printer.ninja.lib.chrome.io;

import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;

import de.westwing.printer.ninja.NinjaPrinter;
import de.westwing.printer.ninja.lib.chrome.message.Message;
import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;

public class MessageReader implements MessageReaderInterface {

	protected InputStream ins;

	public MessageReader(InputStream ins) {
		this.ins = ins;
	}

	@Override
	public MessageInterface read() throws IOException, JSONException {
		// Read message length
		byte[] bytes = new byte[4];
		this.ins.read(bytes, 0, 4);
		int messageLength = getInt(bytes);
		byte[] fullMessage = new byte[messageLength];
		
		NinjaPrinter.debug("Expected length:" + messageLength + ", message:" + fullMessage);

		int offset = 0;
		int readBytes = 0;
		
		try {
			do {
				readBytes = this.ins.read(fullMessage, offset, messageLength - offset);
				
				NinjaPrinter.debug("offest:" + offset + ", read bytes:" + readBytes + ", message:" + fullMessage.toString());
	
				offset += readBytes;
				
			} while(offset < messageLength);
		} catch (Exception e) {
			NinjaPrinter.debug("Someone killed me:" + e.getMessage());
		}
		
		NinjaPrinter.debug("Arrived length:" + fullMessage.toString().length());
		
		return new Message(new String(fullMessage, "UTF-8"));
	
	}
	
	@Override
	public int available() throws IOException
	{
		return this.ins.available();
	}
	
	public void close() throws IOException {
		if (null != this.ins) {
			this.ins.close();
		}
	}

	/**
	 * Read the message size from Chrome.
	 * 
	 * @param bytes
	 * @return
	 */
	protected static int getInt(byte[] bytes) {
		
		return  (bytes[3] << 24) & 0xff000000 | 
				(bytes[2] << 16) & 0x00ff0000 | 
				(bytes[1] << 8) & 0x0000ff00 |
				(bytes[0] << 0) & 0x000000ff;
	}
}
