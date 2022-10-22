package com.skuratov.labma.server.db;

import com.skuratov.labma.server.ServerApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * The class saves actual data from reading the file, allows you to reduce the number of file accesses
 */
public class StoryLines {

    private static StoryLines story;

    private final static List<String> linesRead = new ArrayList<>();

    public static StoryLines getInstance() {
        if (story == null) {
            story = new StoryLines();
        }
        return story;
    }

    /**
     * The method gets the last read lines and adds them to the history.
     * @param line - new read lines from the file
     */
    public void updateStory(String line) {
        if (line != null && !line.isEmpty()) {
            ServerApplication.getClientHandlers().forEach(client -> client.send(line));
            linesRead.add(line);
        }
    }

    public List<String> getLinesRead() {
        return linesRead;
    }
}
