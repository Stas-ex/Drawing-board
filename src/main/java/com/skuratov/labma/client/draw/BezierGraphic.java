package com.skuratov.labma.client.draw;

import com.skuratov.labma.client.draw.model.CurveLine;
import com.skuratov.labma.client.draw.model.Point;

import java.util.ArrayList;

public class BezierGraphic {

    /**
     * The method generates a constant for creating points Bezier.
     * @param i - vertex number
     * @param n - number of vertices
     * @param t - curve position (from 0 to 1)
     */
    private static float getBezierBasis(int i, int n, float t) {
        return (float) ((fact(n) / (fact(i) * fact(n - i))) * Math.pow(t, i) * Math.pow(1 - t, n - i));
    }

    /**
     * Method for Factorial Calculation.
     * @return factorial by number
     */
    private static float fact(int number) {
        return (number <= 1) ? 1 : number * fact(number - 1);
    }

    // arr - массив опорных точек. Точка - двухэлементный массив, (x = arr[0], y = arr[1])
    // step - шаг при расчете кривой (0 < step < 1), по умолчанию 0.01
    private static ArrayList<Point> getBezierCurve(CurveLine line) {
        ArrayList<Point> listCurve = line.getListPoints();
        ArrayList<Point> newListCurve = new ArrayList<>();
        float x = 0, y = 0, b;
        for (float i = 0.001f; i < 1; i += 0.001f) {
            for (int j = 0; j < listCurve.size(); j++) {
                b = getBezierBasis(j, listCurve.size() - 1, i);
                x += listCurve.get(j).getX() * b;
                y += listCurve.get(j).getY() * b;
            }
            newListCurve.add(new Point(x, y));
            x = 0;
            y = 0;
        }
        return newListCurve;
    }

    /**
     *  Method that generates a list of curved lines based on lines received from the server.
     * @param listCurves - list of points read from the server
     * @return List formed from Bezier curves
     */
    public static ArrayList<CurveLine> getBezierCurves(ArrayList<CurveLine> listCurves){
        ArrayList<CurveLine> listAnswer = new ArrayList<>();
        for (CurveLine line: listCurves) {
            listAnswer.add(new CurveLine(getBezierCurve(line)));
        }
        return listAnswer;
    }

}
