package io.github.stefan_ghioci.image_processing;

import io.github.stefan_ghioci.ai.Individual;
import io.github.stefan_ghioci.tools.ColorTools;
import io.github.stefan_ghioci.tools.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

import static io.github.stefan_ghioci.image_processing.Constants.TILE_GROUP_SIZE;

public class Reconstruction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Reconstruction.class.getSimpleName());
    private static SubPaletteConfig lastBestResult = null;

    public static SubPaletteConfig getLastBestResult()
    {
        return lastBestResult;
    }

    public static SubPaletteConfig reconstruct(Color[][] colorMatrix, Palette type, boolean forcedBlack, Speed speed, Callable<Void> update)
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;
        int tileGroupCount = (width * height) / (TILE_GROUP_SIZE * TILE_GROUP_SIZE);

        LOGGER.info(
                "Initializing reconstruction algorithm on {}-tile {}x{} image, using {} palette (forcedBlack={}), {} speed setting",
                tileGroupCount,
                width,
                height,
                type,
                forcedBlack,
                speed);

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


        switch (speed)
        {
            case Slow:
                populationSize = tileGroupCount / 3;
                stagnationFactor = tileGroupCount * 10;
                mutationChance = 0.5;
                break;
            case Standard:
                populationSize = tileGroupCount / 6;
                stagnationFactor = tileGroupCount * 4;
                mutationChance = 0.25;
                break;
            case Fast:
                populationSize = tileGroupCount / 10;
                stagnationFactor = tileGroupCount / 2;
                mutationChance = 0.1;
                break;
            default:
                throw new IllegalStateException("Unexpected speed setting: " + speed);
        }

        SubPaletteConfigAlgorithm subPaletteAlgorithm = new SubPaletteConfigAlgorithm(colorMatrix,
                                                                                      forcedBlack,
                                                                                      palette)
        {
            @Override
            public void setLastBest(Individual lastBest)
            {
                super.setLastBest(lastBest);
                LOGGER.info("New best sub palette config. Calling update function...");
                lastBestResult = (SubPaletteConfig) lastBest;
                try
                {
                    update.call();
                }
                catch (Exception e)
                {
                    LOGGER.info("Could not call update function. Cause: {}", e.getMessage());
                }
            }
        };
        subPaletteAlgorithm.run(populationSize, stagnationFactor, mutationChance);

        return lastBestResult;
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
                    {
                        bestTileGroupDistance = tileGroupDistance;
                        bestSubPalette = subPalette;
                    }
                }

                for (int j = 0; j < TILE_GROUP_SIZE; j++)
                    for (int i = 0; i < TILE_GROUP_SIZE; i++)
                        redrawnMatrix[x + i][y + j] = ColorTools.bestMatch(colorMatrix[x + i][y + j], bestSubPalette);
            }
        return redrawnMatrix;
    }

    public static void resetLastBestConfig()
    {
        lastBestResult = null;
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

}
