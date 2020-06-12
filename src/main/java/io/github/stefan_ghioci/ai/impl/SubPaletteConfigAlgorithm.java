package io.github.stefan_ghioci.ai.impl;

import io.github.stefan_ghioci.ai.EvolutionaryAlgorithm;
import io.github.stefan_ghioci.ai.Individual;
import io.github.stefan_ghioci.processing.Color;
import io.github.stefan_ghioci.processing.Constants;
import io.github.stefan_ghioci.tools.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static io.github.stefan_ghioci.tools.Miscellaneous.getRandomElement;

public class SubPaletteConfigAlgorithm extends EvolutionaryAlgorithm
{
    private final List<Color> palette;
    private final Color[][] colorMatrix;
    private final Color backgroundColor;

    public SubPaletteConfigAlgorithm(Color[][] colorMatrix, boolean forcedBlack, List<Color> palette)
    {
        this.colorMatrix = colorMatrix;
        this.palette = palette;
        this.backgroundColor = forcedBlack ? Color.BLACK : palette.stream()
                                                                  .min(Comparator.comparingDouble(Metrics::getLuminance))
                                                                  .get();
    }

    @Override
    protected Individual generateIndividual()
    {
        List<List<Color>> subPaletteList = new ArrayList<>();


        for (int i = 0; i < Constants.SUB_PALETTE_COUNT; i++)
        {
            List<Color> subPalette = new ArrayList<>();

            subPalette.add(0, backgroundColor);
            while (subPalette.size() < Constants.SUB_PALETTE_SIZE)
            {
                Color color = getRandomElement(palette);
                if (!subPalette.contains(color))
                    subPalette.add(color);
            }
            subPalette.sort(Comparator.comparingDouble(Metrics::getLuminance));

            subPaletteList.add(subPalette);
        }

        return new SubPaletteConfig(subPaletteList, colorMatrix);
    }

    @Override
    protected Individual crossover(Individual mother, Individual father)
    {
        List<List<Color>> subPaletteList = new ArrayList<>();
        List<List<List<Color>>> parentsSubPaletteLists = Arrays.asList(((SubPaletteConfig) mother).getSubPaletteList(),
                                                                       ((SubPaletteConfig) father).getSubPaletteList());

        for (int i = 0; i < Constants.SUB_PALETTE_COUNT; i++)
            subPaletteList.add(getRandomElement(parentsSubPaletteLists).get(0));

        return new SubPaletteConfig(subPaletteList, colorMatrix);
    }

}
