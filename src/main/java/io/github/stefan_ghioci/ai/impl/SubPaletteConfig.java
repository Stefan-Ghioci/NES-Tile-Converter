package io.github.stefan_ghioci.ai.impl;

import io.github.stefan_ghioci.ai.Individual;
import io.github.stefan_ghioci.processing.Color;
import io.github.stefan_ghioci.processing.Constants;
import io.github.stefan_ghioci.tools.ColorTools;
import io.github.stefan_ghioci.tools.Metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.stefan_ghioci.processing.Constants.TILE_GROUP_SIZE;
import static io.github.stefan_ghioci.tools.ColorTools.bestMatch;
import static io.github.stefan_ghioci.tools.Metrics.distanceBetween;
import static io.github.stefan_ghioci.tools.Miscellaneous.getRandomElement;

public class SubPaletteConfig implements Individual
{
    private final List<List<Color>> subPaletteList;
    private final Color[][] colorMatrix;
    private double averageDistance;

    public SubPaletteConfig(List<List<Color>> subPaletteList, Color[][] colorMatrix)
    {
        this.subPaletteList = subPaletteList;
        this.colorMatrix = colorMatrix;
    }

    @Override
    public double getFitness()
    {
        return averageDistance;
    }

    @Override
    public void evaluate()
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        averageDistance = 0;

        List<Color>[][] mapping = ColorTools.computeBestSubPaletteMapping(colorMatrix, subPaletteList);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
            {
                List<Color> bestSubPalette = mapping[x / TILE_GROUP_SIZE][y / TILE_GROUP_SIZE];

                Color color1 = colorMatrix[x][y];
                Color color2 = bestMatch(color1, bestSubPalette);

                averageDistance += distanceBetween(color1, color2);
            }

        averageDistance /= (width * height);
    }

    @Override
    public void mutate()
    {
        List<Color> mixedSubPalette = new ArrayList<>();

        for (int i = 0; i < Constants.SUB_PALETTE_SIZE; i++)
            mixedSubPalette.add(getRandomElement(subPaletteList).get(i));
        mixedSubPalette.sort(Comparator.comparingDouble(Metrics::getLuminance));

        subPaletteList.remove(getRandomElement(subPaletteList));
        subPaletteList.add(mixedSubPalette);
    }

    public List<List<Color>> getSubPaletteList()
    {
        return subPaletteList;
    }
}
