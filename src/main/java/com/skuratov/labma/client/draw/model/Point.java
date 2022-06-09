package com.skuratov.labma.client.draw.model;

import com.skuratov.labma.client.draw.ColorParser;
import lombok.Data;

import java.awt.*;

@Data
public class Point {
    private String operation;
    private float x;
    private float y;
    private Color color;

    public Point(String operation, float x, float y, int colorNum) {
        this.operation = operation;
        this.x = x;
        this.y = y;
        this.color = ColorParser.getColorByDigit(colorNum);
    }

    public Point(float x, float y) {
        this("move",x,y,14445);
    }
}
