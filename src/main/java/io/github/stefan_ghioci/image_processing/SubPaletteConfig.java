package io.github.stefan_ghioci.image_processing;

import io.github.stefan_ghioci.ai.Individual;
import io.github.stefan_ghioci.tools.ColorTools;
import io.github.stefan_ghioci.tools.Metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.stefan_ghioci.image_processing.Constants.TILE_GROUP_SIZE;
import static io.github.stefan_ghioci.tools.Miscellaneous.getRandomElement;

public class SubPaletteConfig implements Individual
{
    private final List<List<Color>> subPaletteList;
    private final Color[][] colorMatrix;
    private double totalDistance;

    public SubPaletteConfig(List<List<Color>> subPaletteList, Color[][] colorMatrix)
    {
        this.subPaletteList = subPaletteList;
        this.colorMatrix = colorMatrix;
    }

    @Override
    public double getFitness()
    {
        return totalDistance;
    }

    @Override
    public void evaluate()
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        totalDistance = 0;

        for (int y = 0; y < height; y += TILE_GROUP_SIZE)
            for (int x = 0; x < width; x += TILE_GROUP_SIZE)
            {
                double bestTileGroupDistance = -1;
                for (List<Color> subPalette : subPaletteList)
                {
                    double tileGroupDistance = 0;
                    for (int j = 0; j < TILE_GROUP_SIZE; j++)
                        for (int i = 0; i < TILE_GROUP_SIZE; i++)
                        {
                            Color color1 = colorMatrix[x + i][y + j];
                            Color color2 = ColorTools.bestMatch(color1, subPalette);

                            tileGroupDistance += Metrics.distanceBetween(color1, color2);
                        }
                    if (bestTileGroupDistance == -1 || tileGroupDistance < bestTileGroupDistance)
                        bestTileGroupDistance = tileGroupDistance;
                }
                totalDistance += bestTileGroupDistance;
            }
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

    @Override
    public Object getSolution()
    {
        return subPaletteList;
    }

    public List<List<Color>> getSubPaletteList()
    {
        return subPaletteList;
    }
}
