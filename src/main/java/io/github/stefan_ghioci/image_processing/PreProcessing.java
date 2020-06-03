package io.github.stefan_ghioci.image_processing;

import io.github.stefan_ghioci.model.Color;
import io.github.stefan_ghioci.navigation.impl.preprocessing.PreProcessingController;
import io.github.stefan_ghioci.tools.FileTools;
import io.github.stefan_ghioci.tools.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class PreProcessing
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProcessingController.class.getSimpleName());

    public static List<Color> computeBestPalette(Color[][] colorMatrix)
    {
        List<Color> palette = FileTools.loadNESPalette();

        Map<Color, Integer> histogram = palette.stream()
                                               .collect(Collectors.toMap(color -> color, color -> 0, (a, b) -> b));

        for (Color[] colors : colorMatrix)
            for (Color color : colors)
            {
                Color bestMatch = Metrics.bestMatch(color, palette);
                histogram.put(bestMatch, histogram.get(bestMatch) + 1);
            }

        return histogram.entrySet()
                        .stream()
                        .sorted(Comparator.comparingInt((ToIntFunction<Map.Entry<Color, Integer>>) Map.Entry::getValue)
                                          .reversed())
                        .limit(13)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
    }

    public static Color[][] quantize(Color[][] colorMatrix, List<Color> palette, Dithering.Method ditheringMethod)
    {
        LOGGER.info("Quantizing image given {}-color palette, using dithering method: {}",
                    palette.size(),
                    ditheringMethod.name());

        int width = colorMatrix.length;
        int height = colorMatrix[0].length;


        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
            {
                Color oldColor = colorMatrix[x][y];
                Color newColor = Metrics.bestMatch(oldColor, palette);

                colorMatrix[x][y] = newColor;

                Dithering.dither(colorMatrix, x, y, oldColor, newColor, ditheringMethod);
            }

        return colorMatrix;
    }
}
