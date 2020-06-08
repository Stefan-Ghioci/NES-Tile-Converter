package io.github.stefan_ghioci.processing;

import io.github.stefan_ghioci.ai.ClusteringAlgorithm;
import io.github.stefan_ghioci.ai.impl.TileClusteringAlgorithm;
import io.github.stefan_ghioci.tools.ColorTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static io.github.stefan_ghioci.processing.Constants.SUB_PALETTE_SIZE;
import static io.github.stefan_ghioci.processing.Constants.TILE_SIZE;
import static io.github.stefan_ghioci.tools.Metrics.distanceBetween;

public class Compression
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Compression.class.getSimpleName());
    private static int currentTileCount;
    private static List<Tile> lastResult;

    public static void compress(Color[][] colorMatrix,
                                List<List<Color>> subPaletteList,
                                int desiredTileCount,
                                Type type,
                                Callable<Void> update)
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;
        int tileCount = width * height / (TILE_SIZE * TILE_SIZE);
        LOGGER.info("Initializing compression algorithm on {}-tile image into {} tiles...",
                    tileCount,
                    desiredTileCount);


        ClusteringAlgorithm<Tile> clusteringAlgorithm = new TileClusteringAlgorithm()
        {
            @Override
            protected void merge(List<List<Tile>> clusters,
                                 List<Tile> minLinkageCluster1,
                                 List<Tile> minLinkageCluster2)
            {
                super.merge(clusters, minLinkageCluster1, minLinkageCluster2);
                Compression.LOGGER.info("Clusters merged, calling update function...");
                currentTileCount = clusters.size();
                try
                {
                    update.call();
                }
                catch (Exception e)
                {
                    Compression.LOGGER.info("Could not call update function. Cause: {}", e.getMessage());
                }
            }
        };
        List<Tile> tiles = ColorTools.colorMatrixToTiles(colorMatrix, subPaletteList);
        List<List<Tile>> clusteredTiles = clusteringAlgorithm.run(tiles, desiredTileCount);

        lastResult = compressTiles(colorMatrix, clusteredTiles, type);
    }

    private static List<Tile> compressTiles(Color[][] colorMatrix, List<List<Tile>> clusteredTiles, Type type)
    {
        List<Tile> compressedTiles = new ArrayList<>();

        for (List<Tile> cluster : clusteredTiles)
        {
            int[][] bestFitMapping;

            switch (type)
            {
                case Replace:
                    bestFitMapping = getBestFitMapping(colorMatrix, cluster);
                    break;
                case Mode:
                    bestFitMapping = computeModeMapping(cluster);
                    break;
                default:
                    throw new IllegalStateException("Unexpected compression type: " + type);
            }

            for (Tile tile : cluster)
            {
                List<Color> subPalette = tile.getSubPalette();
                int row = tile.getRow();
                int column = tile.getColumn();

                compressedTiles.add(new Tile(row, column, bestFitMapping, subPalette));
            }
        }

        return compressedTiles;
    }

    private static int[][] getBestFitMapping(Color[][] colorMatrix, List<Tile> cluster)
    {
        int[][] bestFitMapping = cluster.get(0).getColorMapping();
        double minDistanceSum = -1;

        for (Tile tile1 : cluster)
        {
            double distanceSum = 0;

            for (Tile tile2 : cluster)
            {
                int[][] mapping = tile1.getColorMapping();
                int row = tile2.getRow();
                int column = tile2.getColumn();
                List<Color> subPalette = tile2.getSubPalette();

                int y = row * TILE_SIZE;
                int x = column * TILE_SIZE;

                for (int i = 0; i < TILE_SIZE; i++)
                    for (int j = 0; j < TILE_SIZE; j++)
                        distanceSum += distanceBetween(colorMatrix[x + i][y + j], subPalette.get(mapping[i][j]));
            }

            if (minDistanceSum == -1 || minDistanceSum > distanceSum)
            {
                minDistanceSum = distanceSum;
                bestFitMapping = tile1.getColorMapping();
            }
        }
        return bestFitMapping;
    }

    private static int[][] computeModeMapping(List<Tile> tiles)
    {
        int[][] modeMapping = new int[TILE_SIZE][TILE_SIZE];

        for (int y = 0; y < TILE_SIZE; y++)
            for (int x = 0; x < TILE_SIZE; x++)
            {
                int[] frequency = new int[SUB_PALETTE_SIZE];

                for (Tile tile : tiles)
                    frequency[tile.getColorMapping()[x][y]]++;

                int mostFrequent = 0;
                int maxFrequency = 0;

                for (int i = 0; i < SUB_PALETTE_SIZE; i++)
                    if (maxFrequency < frequency[i])
                    {
                        mostFrequent = i;
                        maxFrequency = frequency[i];
                    }

                modeMapping[x][y] = mostFrequent;
            }

        return modeMapping;
    }

    public static int getCurrentTileCount()
    {
        return currentTileCount;
    }

    public static List<Tile> getLastResult()
    {
        return lastResult;
    }

    public static void resetLastResult()
    {
        lastResult = null;
    }

    public enum Type
    {
        Replace,
        Mode
    }
}
