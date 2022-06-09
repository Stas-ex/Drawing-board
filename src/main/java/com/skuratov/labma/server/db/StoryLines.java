package com.skuratov.labma.server.db;

import com.skuratov.labma.server.Server;

import java.util.ArrayList;

/**
 * The class saves actual data from reading the file, allows you to reduce the number of file accessesю
 */
public class StoryLines {
    private volatile static ArrayList<String> listFileLine;

    /**
     * Method for updating data on the server in case of a file change.
     * @param listFileLine - the new list read after file changes.
     */
    public void updateStory(ArrayList<String> listFileLine) {
        StoryLines.listFileLine = listFileLine;
        Server.listClient.forEach(client -> client.send(listFileLine));
    }

    /**
     * Get the complete list of read lines.
     * @return list String.
     */
    public ArrayList<String> getStory() {
        return listFileLine;
    }
}
