package com.skuratov.labma.server.db;

import com.skuratov.labma.server.ServerApplication;


import java.util.List;

/**
 * The class saves actual data from reading the file, allows you to reduce the number of file accessesю
 */
public class StoryLines {
    private static StoryLines story;
    private List<String> linesRead;

    public static StoryLines getInstance() {
        if (story == null) {
            story = new StoryLines();
        }
        return story;
    }


    /**
     * Method for updating data on the server in case of a file change.
     * @param listFileLine - the new list read after file changes.
     */
    public void updateStory(List<String> listFileLine) {
        this.linesRead = listFileLine;
        ServerApplication.getClientHandlers().forEach(client -> client.send(listFileLine));
    }

    /**
     * Get the complete list of read lines.
     * @return list String.
     */
    public List<String> getLinesRead() {
        return linesRead;
    }
}
