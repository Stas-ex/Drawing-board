package com.skuratov.labma.client.draw;

import com.skuratov.labma.client.draw.model.CurveLine;
import com.skuratov.labma.client.draw.model.Point;

import java.util.ArrayList;
import java.util.List;

public class BezierGraphic {

    /**
     * Method that generates a list of curved lines based on lines received from the server.
     *
     * @param readCurves - list of points read from the server
     * @return List formed from Bezier curves
     */
    public static List<CurveLine> getBezierCurves(List<CurveLine> readCurves) {
        List<CurveLine> curvesBezier = new ArrayList<>();
        for (CurveLine line : readCurves) {
            curvesBezier.add(getBezierCurve(line));
        }
        return curvesBezier;
    }

    /**
     * @param line - curved line resulting from reading a file
     * @return - curved line obtained by the Bezier method
     */
    private static CurveLine getBezierCurve(CurveLine line) {
        List<Point> curvesOld = line.getPoints();
        List<Point> curvesBezier = new ArrayList<>();
        for (float i = 0.001f; i < 1; i += 0.001f) {
            float x = 0, y = 0;
            for (int j = 0; j < curvesOld.size(); j++) {
                float b = getBezierBasis(j, curvesOld.size() - 1, i);
                x += curvesOld.get(j).getX() * b;
                y += curvesOld.get(j).getY() * b;
            }
            curvesBezier.add(new Point(x, y));
        }
        return new CurveLine(curvesBezier);
    }

    /**
     * The method generates a constant for creating points Bezier.
     *
     * @param i - vertex number
     * @param n - number of vertices
     * @param t - curve position (from 0 to 1)
     * @return basis bezier
     */
    private static float getBezierBasis(int i, int n, float t) {
        return (float) ((fact(n) / (fact(i) * fact(n - i))) * Math.pow(t, i) * Math.pow(1 - t, n - i));
    }

    /**
     * Method for Factorial Calculation.
     *
     * @return factorial by number
     */
    private static float fact(int number) {
        return (number <= 1) ? 1 : number * fact(number - 1);
    }
}
