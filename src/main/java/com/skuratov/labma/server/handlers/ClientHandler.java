package com.skuratov.labma.server.handlers;

import com.skuratov.labma.server.db.StoryLines;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Class for sending messages to the client side
 */
public class ClientHandler {
    private final PrintWriter out;

    /**
     * Method sends actual drawing data {@link #send(List)} and initializes variables.
     * @param clientSocket - init socket
     */
    public ClientHandler(Socket clientSocket) throws IOException {
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
        //display the current version of the lines
        List<String> linesStory = StoryLines.getInstance().getLinesRead();
        send(linesStory);
    }

    public void send(List<String> linesStory) {
        if (linesStory.size() > 0) {
            linesStory.stream().map(line -> line + "\n").forEach(out::write);
            out.flush();
        }
    }

}
