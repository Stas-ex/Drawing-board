package com.skuratov.labma.server.io;

import com.skuratov.labma.server.db.StoryLines;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * The constructor reads all the data from the file and passes it to the  data {@link StoryLines}.
     * @param fileName file name to read
     */
    public FIleThread(String fileName) {
        file = new File(fileName);
        lastTimeFile = file.lastModified();
        List<String> linesRead = readAllFile();
        StoryLines.getInstance().updateStory(linesRead);
    }

    /**
     * The method is endlessly called to check the last update of the file.
     */
    @Override
    public synchronized void run() {
        while (!this.isInterrupted()) {
            if (file.lastModified() > lastTimeFile) {
                List<String> readLines = this.readEndLine();
                if (!readLines.isEmpty()) {
                    logger.info(String.format("file update! time : %d, lines: %d", file.lastModified(), readLines.size()));
                    StoryLines.getInstance().updateStory(readLines);
                }
                lastTimeFile = file.lastModified();
            }
        }
    }

    /**
     * The method reads and checks for correctness all lines from the file.
     * @return all lines from the file
     */
    private List<String> readAllFile() {
        logger.info("Read all file.");
        List<String> validLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (validateLine(line)) {
                    validLines.add(line);
                }
            }
            return validLines;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * The method reads the last lines from the file after it has been updated.
     * @return read lines from file
     */
    private List<String> readEndLine() {
        logger.info("Read end file.");
        List<String> updateLines = new ArrayList<>();
        List<String> storyLines = StoryLines.getInstance().getLinesRead();

        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8)) {
            String lastLine = reader.readLine();
            while (lastLine != null && !storyLines.contains(lastLine)) {
                if (validateLine(lastLine)) {
                    updateLines.add(lastLine);
                }
                lastLine = reader.readLine();
            }
            Collections.reverse(updateLines);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return updateLines;
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
