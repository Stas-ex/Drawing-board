package com.skuratov.labma.client.model;

import com.skuratov.labma.client.draw.BezierGraphic;
import com.skuratov.labma.client.draw.MainWindow;
import com.skuratov.labma.client.draw.model.CurveLine;
import com.skuratov.labma.client.draw.model.OperationType;
import com.skuratov.labma.client.draw.model.Point;
import com.skuratov.labma.client.io.Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

/**
 * The client class interacts with the server and with a graphical window for a single thread.
 */
public class ClientThread implements Runnable {

    private final static Logger logger = Logger.getLogger("ServerApplication");

    private final String host;
    private final int port;
    private final MainWindow window;

    private Reader reader;

    /**
     * @param host variable to connect to the server
     * @param port variable to connect to the server
     */
    public ClientThread(String host, int port) {
        logger.info("Client open window.");
        this.host = host;
        this.port = port;
        window = new MainWindow(700, 600);
    }

    /**
     * The main method for interacting with the window and the server
     */
    @Override
    public void run() {
        //If unable to connect to socket, wait for connection
        while (reader == null) {
            try (Socket socket = new Socket(host, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                reader = new Reader(in);
                while (!socket.isClosed()) {
                    repaint(in);
                }
            } catch (IOException e) {
                logger.warning(String.format("Client %s can't connect", Thread.currentThread().getName()));

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * Method for reading updated data from the server and redrawing the window
     * @param in stream for reading data from the server
     * @throws IOException if data read error
     */
    public void repaint(BufferedReader in) throws IOException {
        if (in.ready()) {
            logger.info("Repaint..");
            List<CurveLine> curvesRead = reader.readCurvedLines();

            //Connecting old and new points
            if (!window.getCurves().isEmpty()) {
                Point point = curvesRead.get(0).getPoints().get(0);
                if (point.getOperation().equals(OperationType.MOVE)) {
                    List<Point> lastPointsWindow = window.getCurves().get(window.getCurves().size() - 1).getPoints();
                    curvesRead.get(0).addBeginningPoint(lastPointsWindow.get(lastPointsWindow.size() - 1));
                }
            }
            List<CurveLine> curvesBezier = BezierGraphic.getBezierCurves(curvesRead);
            if (!curvesBezier.isEmpty()) {
                window.addCurves(curvesBezier);
                window.revalidate();
                window.repaint();
            }
        }
    }
}
