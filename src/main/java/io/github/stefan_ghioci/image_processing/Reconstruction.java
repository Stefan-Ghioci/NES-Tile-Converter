package io.github.stefan_ghioci.image_processing;

import io.github.stefan_ghioci.ai.EvolutionaryAlgorithm;
import io.github.stefan_ghioci.ai.Individual;
import io.github.stefan_ghioci.tools.ColorTools;
import io.github.stefan_ghioci.tools.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.stefan_ghioci.image_processing.Constants.TILE_GROUP_SIZE;
import static io.github.stefan_ghioci.tools.Miscellaneous.getRandomElement;

public class Reconstruction
{
    private static SubPaletteConfig lastBestConfig = null;

    public static SubPaletteConfig reconstruct(Color[][] colorMatrix, Palette type, boolean forcedBlack, Speed speed)
    {
        List<Color> palette;
        int populationSize;
        int stagnationFactor;
        double mutationChance;

        switch (type)
        {
            case Best:
                palette = ColorTools.computeBestPalette(colorMatrix, forcedBlack, Constants.MAX_PALETTE_SIZE);
                break;
            case Uniform:
                palette = ColorTools.computeBestPalette(colorMatrix, forcedBlack, Constants.SUB_PALETTE_SIZE);
                break;
            case Grayscale:
                palette = ColorTools.computeNESGrayscalePalette();
                break;
            default:
                throw new IllegalStateException("Unexpected palette type: " + type);
        }

        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        int tileGroupCount = (width * height) / (TILE_GROUP_SIZE * TILE_GROUP_SIZE);

        switch (speed)
        {
            case Slow:
                populationSize = tileGroupCount;
                stagnationFactor = tileGroupCount * 6;
                mutationChance = 0.5;
                break;
            case Standard:
                populationSize = tileGroupCount / 3;
                stagnationFactor = tileGroupCount * 3;
                mutationChance = 0.25;
                break;
            case Fast:
                populationSize = tileGroupCount / 6;
                stagnationFactor = tileGroupCount;
                mutationChance = 0.1;
                break;
            default:
                throw new IllegalStateException("Unexpected speed setting: " + speed);
        }

        SubPaletteConfigAlgorithm subPaletteAlgorithm = new SubPaletteConfigAlgorithm(colorMatrix,
                                                                                      forcedBlack,
                                                                                      palette);
        subPaletteAlgorithm.run(populationSize, stagnationFactor, mutationChance);

        return subPaletteAlgorithm.getLastBestSubPaletteConfig();
    }

    public static Color[][] redrawColorMatrix(Color[][] colorMatrix, List<List<Color>> subPaletteList)
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        Color[][] redrawnMatrix = new Color[width][height];

        for (int y = 0; y < height; y += TILE_GROUP_SIZE)
            for (int x = 0; x < width; x += TILE_GROUP_SIZE)
            {
                List<Color> bestSubPalette = null;
                double bestTotalDistance = -1;

                for (List<Color> subPalette : subPaletteList)
                {
                    double totalDistance = 0;
                    for (int j = 0; j < TILE_GROUP_SIZE; j++)
                        for (int i = 0; i < TILE_GROUP_SIZE; i++)
                        {
                            Color color1 = colorMatrix[x + i][y + j];
                            Color color2 = ColorTools.bestMatch(color1, subPalette);

                            totalDistance += Metrics.distanceBetween(color1, color2);
                        }
                    if (bestTotalDistance == -1 || totalDistance < bestTotalDistance)
                    {
                        bestTotalDistance = totalDistance;
                        bestSubPalette = subPalette;
                    }
                }

                for (int j = 0; j < TILE_GROUP_SIZE; j++)
                    for (int i = 0; i < TILE_GROUP_SIZE; i++)
                        redrawnMatrix[x + i][y + j] = ColorTools.bestMatch(colorMatrix[x + i][y + j], bestSubPalette);
            }
        return redrawnMatrix;
    }

    public enum Speed
    {
        Slow,
        Standard,
        Fast
    }

    public enum Palette
    {
        Best,
        Uniform,
        Grayscale
    }

    private static class SubPaletteConfigAlgorithm extends EvolutionaryAlgorithm
    {
        private final List<Color> palette;
        private final Color[][] colorMatrix;
        private final boolean forcedBlack;

        private SubPaletteConfig lastBestSubPaletteConfig;

        public SubPaletteConfigAlgorithm(Color[][] colorMatrix, boolean forcedBlack, List<Color> palette)
        {
            this.colorMatrix = colorMatrix;
            this.forcedBlack = forcedBlack;
            this.palette = palette;
        }

        @Override
        protected Individual generateIndividual()
        {
            List<List<Color>> subPaletteList = new ArrayList<>();

            Color backgroundColor = forcedBlack ? Color.black() : getRandomElement(palette);

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
                subPaletteList.add(subPalette);
            }

            return new SubPaletteConfig(subPaletteList, colorMatrix);
        }

        @Override
        protected Individual select(List<Individual> population)
        {
            int populationThreshold = (int) (population.size() * 0.75);
            return getRandomElement(population.stream()
                                              .sorted(Comparator.comparingDouble(Individual::getFitness).reversed())
                                              .limit(populationThreshold)
                                              .collect(Collectors.toList()));
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

        @Override
        public void setLastBest(Individual lastBest)
        {
            super.setLastBest(lastBest);
            this.lastBestSubPaletteConfig = (SubPaletteConfig) lastBest;
        }

        public SubPaletteConfig getLastBestSubPaletteConfig()
        {
            return lastBestSubPaletteConfig;
        }
    }

    public static class SubPaletteConfig implements Individual
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
            Color[][] redrawnMatrix = redrawColorMatrix(colorMatrix, subPaletteList);
            totalDistance = ColorTools.totalDistanceBetween(redrawnMatrix, colorMatrix);
        }

        @Override
        public void mutate()
        {
            List<Color> mixedSubPalette = new ArrayList<>();

            for (int i = 0; i < Constants.SUB_PALETTE_SIZE; i++)
                mixedSubPalette.add(getRandomElement(subPaletteList).get(i));

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
}
