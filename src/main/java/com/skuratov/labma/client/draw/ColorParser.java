package com.skuratov.labma.client.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for converting quantitative color values to a Color object.
 */
public class ColorParser {
    private static final Map<String, Color> colorMap = new HashMap<>();

    static {
        colorMap.put("-16777216", Color.black);
    }

    public static Color getColorByDigit(String numberStr) {
        return colorMap.getOrDefault(numberStr, Color.BLACK);
    }
}
