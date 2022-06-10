package com.skuratov.labma.server.io;

import com.skuratov.labma.server.db.StoryLines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * The class is needed to wait for the file with drawing data to change, and read it.
 */
public class FIleThread extends Thread {
    private final static Logger logger = Logger.getLogger("FIleThread");
    private final File file;
    private long lastTimeFile;


    public FIleThread(String fileName) {
        file = new File(fileName);
        lastTimeFile = 0;
    }

    /**
     * The method is endlessly called to check the last update of the file.
     */
    @Override
    public synchronized void run() {
        logger.info("File watching...");
        if (file.lastModified() > lastTimeFile) {
            logger.info("file update! time : " + file.lastModified());
            lastTimeFile = file.lastModified();
            StoryLines.getInstance().updateStory(this.readFile(file));
        }
    }

    /**
     * Method for reading file data
     *
     * @param file - drawing data file
     * @return list of lines read
     */
    private List<String> readFile(File file) {
        List<String> validLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (validateLine(line)) {
                    validLines.add(line + "\n");
                }
            }
            return validLines;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Method for validating string data.
     *
     * @param line - line read from file.
     */
    private boolean validateLine(String line) {
        String[] splitLine = line.split(";");
        try {
            return splitLine.length == 5
                    && Pattern.matches("^([0-9A-Fa-f]{2}[\\\\.:-]){5}([0-9A-Fa-f]{2})$", splitLine[0])
                    && ((splitLine[1].equals("start") || splitLine[1].equals("move")
                    && Double.parseDouble(splitLine[2]) > 0 && Double.parseDouble(splitLine[3]) > 0));
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
