package com.skuratov.labma.client.draw.model;

import lombok.Data;

import java.awt.*;

@Data
public class Point {

    private float x;
    private float y;
    private Color color;
    private OperationType operation;

    public Point(OperationType operation, float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.operation = operation;
    }

    public Point(float x, float y) {
        this(OperationType.MOVE, x, y, Color.BLACK);
    }
}
