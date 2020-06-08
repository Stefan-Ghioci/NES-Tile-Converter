package io.github.stefan_ghioci.processing;

import io.github.stefan_ghioci.tools.ColorTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PreProcessing
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProcessing.class.getSimpleName());

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
                        floydSteinbergDither(colorMatrix, palette, y, x);
                        break;
                    case Random:
                        randomDither(colorMatrix[x], palette, y);
                        break;
                }

        return colorMatrix;
    }

    private static void randomDither(Color[] colorMatrix, List<Color> palette, int y)
    {
        double scale = Math.random() / 4;
        double randomWeight = (1 - Math.random() * 2) * scale;

        Color oldColor = colorMatrix[y];
        Color newColor = ColorTools.bestMatch(addWeightedError(oldColor, oldColor, randomWeight),
                                              palette);

        colorMatrix[y] = newColor;
    }

    private static void floydSteinbergDither(Color[][] colorMatrix, List<Color> palette, int y, int x)
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
        Random
    }
}
