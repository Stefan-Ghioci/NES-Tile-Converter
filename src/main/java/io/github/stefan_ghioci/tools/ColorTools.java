package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.image_processing.Color;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class ColorTools
{

    private static List<Color> nesGrayscalePalette;

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

    public static List<Color> computeBestPalette(Color[][] colorMatrix, boolean forcedBlack, int size)
    {
        List<Color> palette = FileTools.loadNESPalette();

        Map<Color, Integer> histogram = palette.stream()
                                               .collect(Collectors.toMap(color -> color, color -> 0, (a, b) -> b));

        for (Color[] colors : colorMatrix)
            for (Color color : colors)
            {
                Color bestMatch = bestMatch(color, palette);
                histogram.put(bestMatch, histogram.get(bestMatch) + 1);
            }

        List<Color> bestPalette = histogram.entrySet()
                                           .stream()
                                           .sorted(Comparator.comparingInt((ToIntFunction<Map.Entry<Color, Integer>>) Map.Entry::getValue)
                                                             .reversed())
                                           .limit(size)
                                           .map(Map.Entry::getKey)
                                           .collect(Collectors.toList());

        if (forcedBlack && !bestPalette.contains(Color.black()))
        {
            bestPalette.remove(bestPalette.size() - 1);
            bestPalette.add(Color.black());
        }

        return bestPalette;
    }

    public static List<Color> computeNESGrayscalePalette()
    {
        if (nesGrayscalePalette != null)
            return nesGrayscalePalette;

        List<Color> palette = FileTools.loadNESPalette()
                                       .stream()
                                       .filter(color -> color.getRed() == color.getGreen() && color.getGreen() == color
                                               .getBlue())
                                       .collect(Collectors.toList());

        nesGrayscalePalette = palette;
        return palette;
    }

    public static double totalDistanceBetween(Color[][] colorMatrix1, Color[][] colorMatrix2)
    {
        double totalDistance = 0;

        int width = colorMatrix1.length;
        int height = colorMatrix1[0].length;

        if (width != colorMatrix2.length || height != colorMatrix2[0].length)
            throw new UnsupportedOperationException("Matrices sizes are not the same. ");

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
            {
                Color color1 = colorMatrix1[x][y];
                Color color2 = colorMatrix2[x][y];

                totalDistance += Metrics.distanceBetween(color1, color2);
            }

        return totalDistance;
    }
}
