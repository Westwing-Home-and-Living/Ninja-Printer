package de.westwing.printer.ninja.lib.document;

/**
 * 
 * @author Omar Tchokhani <omar.tchokhani@westwing.de>
 *
 */
public interface DocumentInterface {

	public String toRawString();

	public byte[] toBytes();

	public java.nio.ByteBuffer toByteBuffer();
}
