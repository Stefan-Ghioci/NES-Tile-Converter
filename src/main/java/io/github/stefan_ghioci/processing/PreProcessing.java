package io.github.stefan_ghioci.processing;

import io.github.stefan_ghioci.tools.ColorTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.github.stefan_ghioci.processing.Constants.TILE_SIZE;

public class PreProcessing
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProcessing.class.getSimpleName());
    private static double[][] thresholdMap =
            {
                    { 36, 55, 2, 21, 32, 51, 6, 17 },
                    { 56, 11, 30, 41, 60, 15, 26, 45 },
                    { 20, 39, 50, 5, 16, 35, 54, 1 },
                    { 40, 59, 14, 25, 44, 63, 10, 29 },
                    { 4, 23, 34, 53, 0, 19, 38, 49 },
                    { 24, 43, 62, 9, 28, 47, 58, 13 },
                    { 52, 7, 18, 37, 48, 3, 22, 33 },
                    { 8, 27, 46, 57, 12, 31, 42, 61 }
            };

    public static Color[][] quantize(Color[][] colorMatrix, List<Color> palette, Dithering dithering)
    {
        LOGGER.info("Quantizing image given {}-color palette, using dithering method: {}",
                    palette.size(),
                    dithering.name());

        int width = colorMatrix.length;
        int height = colorMatrix[0].length;


        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                switch (dithering)
                {
                    case None:
                        colorMatrix[x][y] = ColorTools.bestMatch(colorMatrix[x][y], palette);
                        break;
                    case FloydSteinberg:
                        floydSteinbergDither(colorMatrix, palette, x, y);
                        break;
                    case Random:
                        randomDither(colorMatrix, palette, x, y);
                        break;
                    case Ordered:
                        int size = thresholdMap.length;
                        double factor = thresholdMap[x % size][y % size] / (size * size) - 0.5;
                        Color threshold = Color.grayOf(TILE_SIZE * TILE_SIZE);
                        Color attempt = addWeightedError(colorMatrix[x][y], threshold, factor);
                        colorMatrix[x][y] = ColorTools.bestMatch(attempt, palette);
                        break;
                }

        return colorMatrix;
    }

    private static void randomDither(Color[][] colorMatrix, List<Color> palette, int x, int y)
    {
        double scale = Math.pow(Math.random(), 3);
        double randomWeight = (1 - Math.random() * 2) * scale;

        Color oldColor = colorMatrix[x][y];
        Color newColor = ColorTools.bestMatch(addWeightedError(oldColor, oldColor, randomWeight), palette);

        colorMatrix[x][y] = newColor;
    }

    private static void floydSteinbergDither(Color[][] colorMatrix, List<Color> palette, int x, int y)
    {
        Color oldColor = colorMatrix[x][y];
        Color newColor = ColorTools.bestMatch(oldColor, palette);
        Color error = Color.difference(oldColor, newColor);

        colorMatrix[x][y] = newColor;

        try
        {
            colorMatrix[x + 1][y] = addWeightedError(colorMatrix[x + 1][y], error, 7 / 16.0);
            colorMatrix[x - 1][y + 1] = addWeightedError(colorMatrix[x - 1][y + 1], error, 3 / 16.0);
            colorMatrix[x][y + 1] = addWeightedError(colorMatrix[x][y + 1], error, 4 / 16.0);
            colorMatrix[x + 1][y + 1] = addWeightedError(colorMatrix[x + 1][y + 1], error, 1 / 16.0);
        }
        catch (ArrayIndexOutOfBoundsException ignored)
        {
            // out of image boundary -> continue
        }
    }

    private static Color addWeightedError(Color color, Color error, double weight)
    {
        return new Color(color.getRed() + (int) Math.round(error.getRed() * weight),
                         color.getGreen() + (int) Math.round(error.getGreen() * weight),
                         color.getBlue() + (int) Math.round(error.getBlue() * weight));
    }

    public enum Dithering
    {
        None,
        FloydSteinberg,
        Random,
        Ordered
    }
}
