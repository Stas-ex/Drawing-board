package com.skuratov.labma.server.io;

import com.skuratov.labma.server.db.StoryLines;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * The class is needed to wait for the file with drawing data to change, and read it.
 */
public class FIleThread extends Thread {

    private final static Logger logger = Logger.getLogger("FIleThread");

    private final File file;

    private volatile long lastTimeFile;

    /**
     * The constructor reads all the data from the file and passes it to the  data {@link StoryLines}.
     * @param fileName file name to read
     */
    public FIleThread(String fileName) {
        file = new File(fileName);
    }

    /**
     * The method is endlessly called to check the last update of the file.
     */
    @Override
    public synchronized void run() {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            while (!this.isInterrupted()) {
                if (file.lastModified() > lastTimeFile) {
                    lastTimeFile = file.lastModified();
                    readChangesFile(reader);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * The method reads and writes new lines from the file.
     * @param reader - reads lines from a file
     */
    private void readChangesFile(BufferedReader reader) throws IOException {
        logger.info("Read update lines... time: " + lastTimeFile);
        while (reader.ready()) {
            String line = reader.readLine();
            if (validateLine(line)) {
                StoryLines.getInstance().updateStory(line);
            }
        }
    }

    /**
     * Method for validating string data.
     * @param line line read from file.
     */
    private boolean validateLine(String line) {
        try {
            String[] splitLine = line.split(";");
            return splitLine.length == 5
                    && Pattern.matches("^([0-9A-Fa-f]{2}[\\\\.:-]){5}([0-9A-Fa-f]{2})$", splitLine[0])
                    && ((splitLine[1].equals("start") || splitLine[1].equals("move")
                    && Double.parseDouble(splitLine[2]) > 0 && Double.parseDouble(splitLine[3]) > 0));
        } catch (NumberFormatException | NullPointerException ex) {
            return false;
        }
    }

}
