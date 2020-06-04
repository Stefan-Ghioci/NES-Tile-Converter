package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.image_processing.Color;

public class Metrics
{

    public static double distanceBetween(Color color1, Color color2)
    {
        double r1 = color1.getRed();
        double g1 = color1.getGreen();
        double b1 = color1.getBlue();

        double r2 = color2.getRed();
        double g2 = color2.getGreen();
        double b2 = color2.getBlue();

        return (((r1 - r2) * 3) * ((r1 - r2) * 3) +
                ((g1 - g2) * 4) * ((g1 - g2) * 4) +
                ((b1 - b2) * 2) * ((b1 - b2) * 2)) /
                (3 * 3 + 4 * 4 + 2 * 2);
    }

    public static int getLuminance(Color color)
    {
        return (int) (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue());
    }
}
