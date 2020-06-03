package io.github.stefan_ghioci.processing;

import io.github.stefan_ghioci.model.Color;
import io.github.stefan_ghioci.tools.FileTools;
import io.github.stefan_ghioci.tools.Metrics;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class PreProcessing
{
    public static List<Color> computeBestPalette(Color[][] colorMatrices)
    {
        List<Color> palette = FileTools.loadNESPalette();

        Map<Color, Integer> histogram = palette.stream()
                                               .collect(Collectors.toMap(color -> color, color -> 0, (a, b) -> b));


        for (Color[] colors : colorMatrices)
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

}
