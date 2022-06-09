package com.skuratov.labma.client.model;

import com.skuratov.labma.client.draw.BezierGraphic;
import com.skuratov.labma.client.draw.MainWindow;
import com.skuratov.labma.client.draw.model.CurveLine;
import com.skuratov.labma.client.io.ReaderSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The client class interacts with the server and with a graphical window for a single thread.
 */
public class Client implements Runnable {
    private final String host;
    private final int port;
    private final MainWindow window;

    private BufferedReader in;
    private Socket socket;
    private ReaderSocket readerSocket;

    /**
     *
     * @param host - variable to connect to the server
     * @param port - variable to connect to the server
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        System.out.println("Client open window.");
        window = new MainWindow(700, 600);
    }


    @Override
    public void run() {
        //If unable to connect to socket, wait for connection
        if(socket == null)
            waitSocketConnection();

        try {
            //Redraw the image if new data has arrived from the server
            while (!Thread.currentThread().isInterrupted()) {
                if (in.ready()) {
                    System.out.println("Repaint..");
                    ArrayList<CurveLine> listReadLine = readerSocket.readAllCurveLine();
                    ArrayList<CurveLine> listBezierLine = BezierGraphic.getBezierCurves(listReadLine);
                    window.setCurveLineList(listBezierLine);
                    window.repaint();
                    window.revalidate();
                    Thread.sleep(1000);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            this.close();
        }
    }

    /**
     * The method works until a connection is made to the client.
     */
    public void waitSocketConnection() {
        while (true) {
            try {
                this.socket = new Socket(host, port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                readerSocket = new ReaderSocket(in);
                System.out.println("Socket connection");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closing method for all stream elements.
     */
    private void close() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
