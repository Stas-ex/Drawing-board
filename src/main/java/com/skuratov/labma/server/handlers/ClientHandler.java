package com.skuratov.labma.server.handlers;

import com.skuratov.labma.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Class for sending messages to the client side
 */
public class ClientHandler {
    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket clientSocket;

    /**
     * Method sends actual drawing data {@link #send(ArrayList)} and initializes variables.
     * @param clientSocket - init socket
     * @throws IOException if errors occurred while creating write and read streams
     */
    public ClientHandler(Socket clientSocket) throws IOException {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            this.clientSocket = clientSocket;

            //display the current version of the lines
            this.send(Server.story.getStory());
    }

    /**
     * Method sends strings to the connected client
     * @param listFileLine - list of lines read
     */
    public void send(ArrayList<String> listFileLine) {
        if (listFileLine.size() > 0) {
            listFileLine.forEach(out::write);
            out.flush();
        }
    }

    /**
     * Closes the client connection.
     */
    public void closeClient() {
        try {
            Server.listClient.remove(this);
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
    }
}
