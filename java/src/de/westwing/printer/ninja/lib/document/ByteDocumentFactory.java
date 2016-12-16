package de.westwing.printer.ninja.lib.document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by felixwoell on 2016-12-16.
 */
public class ByteDocumentFactory {

    /**
     * @param filePath
     * @return A concrete DocumentInterface object.
     * @throws IOException
     */
    public DocumentInterface createByteDocumentFromFileContent(String filePath) throws IOException
    {
        DataInputStream dis = null;
        File file = null;

        byte[] fileContent = null;

        try {
            file = new File(filePath);
            fileContent = new byte[(int) file.length()];
            dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileContent);

            return new ByteDocument(fileContent);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != dis) {
                dis.close();
            }
        }
    }

    /**
     * @param filePath
     * @param cookie
     * @return A concrete DocumentInterface object.
     * @throws IOException
     */
    public DocumentInterface createByteDocumentFromUrl(String filePath, String cookie) throws IOException
    {
        URLConnection urlConnection = null;
        BufferedInputStream ins = null;
        ByteArrayOutputStream out = null;

        try {
            URL url = new URL(filePath);
            urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Cookie", cookie);
            urlConnection.connect();
            ins = new BufferedInputStream(urlConnection.getInputStream());
            out = new ByteArrayOutputStream();

            byte[] byteChunk = new byte[1024];
            int n;

            while ( (n = ins.read(byteChunk)) > 0 ) {
                out.write(byteChunk, 0, n);
            }

            return new ByteDocument(out.toByteArray());

        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != ins) ins.close();
            if (null != out) out.close();
        }
    }
}
