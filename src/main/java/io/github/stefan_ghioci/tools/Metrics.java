package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.model.Color;

import java.util.List;

public class Metrics
{
    public static int getLuminance(Color color)
    {
        return (int) (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue());
    }

    public static Color bestMatch(Color color, List<Color> palette)
    {
        Color minDistanceColor = palette.get(0);
        double minDistance = distanceBetween(color, palette.get(0));

        for (Color paletteColor : palette)
        {
            double distance = distanceBetween(color, paletteColor);
            if (distance < minDistance)
            {
                minDistance = distance;
                minDistanceColor = paletteColor;
            }
        }
        return minDistanceColor;
    }

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
}
