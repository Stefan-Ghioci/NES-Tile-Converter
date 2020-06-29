package io.github.stefan_ghioci.processing;

import io.github.stefan_ghioci.ai.EvolutionaryAlgorithm;
import io.github.stefan_ghioci.ai.Individual;
import io.github.stefan_ghioci.ai.impl.SubPaletteConfig;
import io.github.stefan_ghioci.ai.impl.SubPaletteConfigAlgorithm;
import io.github.stefan_ghioci.tools.ColorTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

import static io.github.stefan_ghioci.processing.Constants.*;


public class Reconstruction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Reconstruction.class.getSimpleName());
    private static List<List<Color>> lastResult = null;

    public static List<List<Color>> getLastResult()
    {
        return lastResult;
    }

    public static void reconstruct(Color[][] colorMatrix,
                                   Palette type,
                                   boolean forcedBlack,
                                   Speed speed,
                                   Callable<Void> update)
    {
        LOGGER.info("Initializing reconstruction algorithm, using {} palette (forcedBlack={}), {} speed setting",
                    type,
                    forcedBlack,
                    speed);

        List<Color> palette;
        int populationSize;
        int stagnationFactor;
        double mutationChance;

        switch (speed)
        {
            //TODO: break down run configuration for each setting
            case Slow:
                populationSize = 50;
                stagnationFactor = 2500;
                mutationChance = 0.1;
                break;
            case Standard:
                populationSize = 50;
                stagnationFactor = 500;
                mutationChance = 0.25;
                break;
            case Fast:
                populationSize = 100;
                stagnationFactor = 500;
                mutationChance = 0.5;
                break;
            default:
                throw new IllegalStateException("Unexpected speed setting: " + speed);
        }

        switch (type)
        {
            case Best:
                palette = ColorTools.computeBestPalette(colorMatrix, forcedBlack, MAX_PALETTE_SIZE);
                break;
            case Uniform:
                palette = ColorTools.computeBestPalette(colorMatrix, forcedBlack, SUB_PALETTE_SIZE);
                break;
            case Grayscale:
                palette = ColorTools.computeNESGrayscalePalette();
                break;
            default:
                throw new IllegalStateException("Unexpected palette type: " + type);
        }


        EvolutionaryAlgorithm subPaletteAlgorithm = new SubPaletteConfigAlgorithm(colorMatrix, forcedBlack, palette)
        {
            @Override
            public void setLastBest(Individual lastBest)
            {
                super.setLastBest(lastBest);
                Reconstruction.LOGGER.info("New best sub palette config. Calling update function...");
                lastResult = ((SubPaletteConfig) lastBest).getSubPaletteList();
                try
                {
                    update.call();
                }
                catch (Exception e)
                {
                    Reconstruction.LOGGER.info("Could not call update function. Cause: {}", e.getMessage());
                }
            }
        };
        subPaletteAlgorithm.run(populationSize, stagnationFactor, mutationChance);

    }

    public static Color[][] redrawColorMatrix(Color[][] colorMatrix, List<List<Color>> subPaletteList)
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        Color[][] redrawnMatrix = new Color[width][height];

        List<Color>[][] mapping = ColorTools.computeBestSubPaletteMapping(colorMatrix, subPaletteList);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
            {
                List<Color> bestSubPalette = mapping[x / TILE_GROUP_SIZE][y / TILE_GROUP_SIZE];
                redrawnMatrix[x][y] = ColorTools.bestMatch(colorMatrix[x][y], bestSubPalette);
            }
        return redrawnMatrix;
    }

    public static void resetLastResult()
    {
        LOGGER.info("Resetting last reconstruction result...");
        lastResult = null;
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
