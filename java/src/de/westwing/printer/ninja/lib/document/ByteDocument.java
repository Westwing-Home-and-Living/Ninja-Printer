package de.westwing.printer.ninja.lib.document;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 */
public class ByteDocument implements DocumentInterface {

	protected byte[] content;

	/**
	 * @param documentData
	 */
	public ByteDocument(byte[] documentData) {
		this.content = documentData;
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
		return this.content;
	}


	@Override
	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(this.toBytes());
	}
}
