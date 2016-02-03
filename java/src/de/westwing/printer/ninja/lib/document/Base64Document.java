package de.westwing.printer.ninja.lib.document;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import de.westwing.printer.ninja.lib.Utilities;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 *
 */
public class Base64Document implements DocumentInterface {

	protected String base64encodedContent;

	protected byte[] decodedContent;

	/**
	 * @param base64Encoded
	 */
	public Base64Document(String base64Encoded) {
		this.base64encodedContent = base64Encoded;
	}

	@Override
	public String toRawString() {
		try {
			return new String(this.toBytes(), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			return new String(this.toBytes());
		}

	}

	@Override
	public byte[] toBytes() {
		if (null == this.decodedContent) {
			this.decodedContent = Utilities.base64decode(this.base64encodedContent);
		}

		return this.decodedContent;
	}

	@Override
	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(this.toBytes());
	}
}
