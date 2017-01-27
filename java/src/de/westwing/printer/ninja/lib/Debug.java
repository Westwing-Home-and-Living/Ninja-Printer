package de.westwing.printer.ninja.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;

public class Debug {
    protected static final String DEBUG_FILE_NAME = "print-debug.log";
    protected static final int MAX_DEBUG_FILE_SIZE = 5000000;

    /**
     * @param message
     */
    public void print(String message)
    {
        try {
            Files.write(getDebugFilePath(), (Calendar.getInstance().getTime() + " " + message + '\r' +'\n').getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Can not write to file:" + e.getMessage());
            System.err.println(Calendar.getInstance().getTime() + " " + message);
        }
    }

    /**
     * Retrieves the file path of the debug log file
     * - creates the file if not exists or file already exceeded the file size limit
     * - opens the file for appending otherwise
     *
     * @return
     */
    protected Path getDebugFilePath()
    {
        Path debugFilePath = Paths.get(DEBUG_FILE_NAME);

        try {
            if (Files.exists(debugFilePath) && Files.size(debugFilePath) < MAX_DEBUG_FILE_SIZE) {
                return debugFilePath;
            }

            if (Files.exists(debugFilePath)) {
                Files.delete(debugFilePath);
            }
            Files.createFile(debugFilePath);
        } catch (IOException e) {
        }

        return debugFilePath;
    }
}
