package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.model.Color;

import java.util.List;

public class ColorTools
{

    public static Color bestMatch(Color color, List<Color> palette)
    {
        Color minDistanceColor = palette.get(0);
        double minDistance = Metrics.distanceBetween(color, palette.get(0));

        for (Color paletteColor : palette)
        {
            double distance = Metrics.distanceBetween(color, paletteColor);
            if (distance < minDistance)
            {
                minDistance = distance;
                minDistanceColor = paletteColor;
            }
        }
        return minDistanceColor;
    }

    public static Color getAverage(List<Color> palette)
    {
        int red = palette.stream().mapToInt(Color::getRed).sum() / palette.size();
        int green = palette.stream().mapToInt(Color::getGreen).sum() / palette.size();
        int blue = palette.stream().mapToInt(Color::getBlue).sum() / palette.size();

        return new Color(red, green, blue);
    }
}
