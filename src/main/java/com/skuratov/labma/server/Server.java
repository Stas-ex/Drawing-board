package com.skuratov.labma.server;

import com.skuratov.labma.server.db.StoryLines;
import com.skuratov.labma.server.handlers.ClientHandler;
import com.skuratov.labma.server.io.FIleThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The Server class creates a multi-threaded data transfer between clients.
 */
public class Server {
    public static StoryLines story;
    private static ScheduledExecutorService scExecutorService;
    private final String fileName;
    public static Set<ClientHandler> listClient;

    /**
     * The constructor initializes the main variables.
     * @param fileName - filename for reading drawing data
     */
    public Server(String fileName) {
        scExecutorService = Executors.newSingleThreadScheduledExecutor();
        listClient = new HashSet<>();
        story = new StoryLines();
        this.fileName = fileName;
    }

    /**
     * Method for creating a connection to the server,
     * and run a thread to check if the file is updated
     */
    public void start() {
        System.out.println("Server start");
        try (ServerSocket serverSocket = new ServerSocket(29288)) {

            //Test update file by time
            scExecutorService.scheduleWithFixedDelay(
                    new FIleThread(fileName), 0, 2, TimeUnit.SECONDS);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connection");
                listClient.add(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Server("src/main/resources/drawingCommands.txt").start();
    }
}
