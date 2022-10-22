package com.skuratov.labma.client.io;

import com.skuratov.labma.client.draw.ColorParser;
import com.skuratov.labma.client.draw.model.CurveLine;
import com.skuratov.labma.client.draw.model.OperationType;
import com.skuratov.labma.client.draw.model.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The ReaderSocket class is designed to read information coming from the server.
 */
public class Reader {

    private final BufferedReader in;

    public Reader(BufferedReader in) {
        this.in = in;
    }

    /**
     * The method reads the data coming from the server.
     * @return array formed curve line
     * @throws IOException if an error occurs while reading the data.
     */
    public List<CurveLine> readCurvedLines() throws IOException {
        List<CurveLine> curves = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        while (in.ready()) {
            Point point = tryParsePointByString(in.readLine());
            if (point != null) {
                if (point.getOperation().equals(OperationType.START) && points.size() != 0) {
                    curves.add(new CurveLine(points));
                    points = new ArrayList<>();
                }
                points.add(point);
            }
        }
        curves.add(new CurveLine(points));
        return curves;
    }

    /**
     * The method translates the string data into a new Point object.
     * @param pointStr string received from the server
     * @return new object Point.
     */
    private static Point tryParsePointByString(String pointStr) {
        String[] splitStr = pointStr.split(";");
        try {
            return new Point(
                    OperationType.valueOf(splitStr[1].toUpperCase(Locale.ROOT)),
                    Float.parseFloat(splitStr[2]),
                    Float.parseFloat(splitStr[3]),
                    ColorParser.getColorByDigit((splitStr[4]))
            );
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
