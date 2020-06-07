package io.github.stefan_ghioci.image_processing;

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
                    break;
                    case Random:
                    {
                        double scale = Math.random() * Math.random();
                        double randomWeight = (1 - Math.random() * 2) * scale;

                        Color oldColor = colorMatrix[x][y];
                        Color newColor = ColorTools.bestMatch(addWeightedError(oldColor, oldColor, randomWeight),
                                                              palette);

                        colorMatrix[x][y] = newColor;
                    }
                    break;
                }

        return colorMatrix;
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
