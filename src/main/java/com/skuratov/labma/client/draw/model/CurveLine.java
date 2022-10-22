package com.skuratov.labma.client.draw.model;

import lombok.Data;

import java.util.List;

@Data
public class CurveLine {

    private List<Point> points;

    public CurveLine(List<Point> listPoints) {
        this.points = listPoints;
    }

    public void addBeginningPoint(Point point) {
        points.add(0, point);
    }

}
