package com.skuratov.labma.client.draw.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CurveLine {
    private ArrayList<Point> listPoints;

    public CurveLine(ArrayList<Point> listPoints) {
        this.listPoints = listPoints;
    }
}
