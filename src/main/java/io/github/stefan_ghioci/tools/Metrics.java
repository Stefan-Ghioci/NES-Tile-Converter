package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.model.Color;

import java.util.List;

public class Metrics
{
    public static int getColorLuminance(Color color)
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
        return 0;
    }
}
