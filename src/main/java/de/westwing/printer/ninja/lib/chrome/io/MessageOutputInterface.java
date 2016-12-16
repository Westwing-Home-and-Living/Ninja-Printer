package de.westwing.printer.ninja.lib.chrome.io;

import java.io.IOException;

import de.westwing.printer.ninja.lib.chrome.message.MessageInterface;

interface MessageOutputInterface {

	public int write(MessageInterface message) throws IOException;
}
