package com.skuratov.labma.server;

import com.skuratov.labma.server.handlers.ClientHandler;
import com.skuratov.labma.server.io.FIleThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Server class creates a multi-threaded data transfer between clients.
 */
public class ServerApplication {
    private final static String FILE_NAME = "src/main/resources/drawingCommands.txt";
    private final static ExecutorService executorService = Executors.newFixedThreadPool(1);
    private final static Set<ClientHandler> clientHandlers  = new HashSet<>();
    private final static Logger logger = Logger.getLogger("ServerApplication");
    private final String fileName;

    /**
     * The constructor initializes the main variables.
     * @param fileName - filename for reading drawing data
     */
    public ServerApplication(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Method for creating a connection to the server,
     * and run a thread to check if the file is updated
     */
    public void start() {
        logger.log(Level.INFO,"Server start");
        try (ServerSocket serverSocket = new ServerSocket(29288)) {

            //Test update file by time
            executorService.execute(new FIleThread(fileName));
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                logger.log(Level.INFO,"Client connection");
                clientHandlers.add(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public static void main(String[] args) {
        new ServerApplication(FILE_NAME).start();
    }
}
