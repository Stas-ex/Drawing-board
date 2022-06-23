package com.skuratov.labma.client.draw;

import java.awt.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Class for converting quantitative color values to a Color object.
 */
public class ColorParser {
    private static final Map<String, Color> colorMap = new HashMap<>();

    static {colorMap.put("-16777216", Color.black);}

    /**
     * The method translates the color number into the {@link Color}
     * @param numberStr unique number for conversion to colo
     * @return point color
     */
    public static Color getColorByDigit(String numberStr) {
        return colorMap.getOrDefault(numberStr, Color.BLACK);
    }
}
