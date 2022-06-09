package com.skuratov.labma.client.io;

import com.skuratov.labma.client.draw.model.CurveLine;
import com.skuratov.labma.client.draw.model.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The ReaderSocket class is designed to read information coming from the server.
 */
public class ReaderSocket {
    private final BufferedReader in;

    public ReaderSocket(BufferedReader in) {
        this.in = in;
    }

    /**
     *The method reads the data coming from the server.
     * @return array formed curve line
     * @throws IOException if an error occurs while reading the data.
     */
    public ArrayList<CurveLine> readAllCurveLine() throws IOException {
        ArrayList<CurveLine> curveLineList = new ArrayList<>();
        ArrayList<Point> pointList = new ArrayList<>();
        while (in.ready()) {
            Point point = parsePointByString(in.readLine());

            if (point != null) {
                if (point.getOperation().equals("start") && pointList.size() != 0) {
                    curveLineList.add(new CurveLine((ArrayList<Point>) pointList.clone()));
                    pointList.clear();
                }
                pointList.add(point);
            }
        }
        curveLineList.add(new CurveLine(pointList));
        return curveLineList;
    }

    /**
     * The method translates the string data into a new Point object.
     * @param pointStr string received from the server
     * @return new object Point.
     */
    private Point parsePointByString(String pointStr) {
        String[] splitStr = pointStr.split(";");
        try {
            return new Point(
                    splitStr[1],
                    Float.parseFloat(splitStr[2]),
                    Float.parseFloat(splitStr[3]),
                    Integer.parseInt(splitStr[4])
            );
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
