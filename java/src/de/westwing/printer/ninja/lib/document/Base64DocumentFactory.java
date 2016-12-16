package de.westwing.printer.ninja.lib.document;

import java.io.IOException;

/**
 * Created by felixwoell on 2016-12-16.
 */
public class Base64DocumentFactory {
    public DocumentInterface createBase64Document(String fileContent) throws IOException{
        return new Base64Document(fileContent);
    }
}
