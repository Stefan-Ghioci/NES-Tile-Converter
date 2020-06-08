package io.github.stefan_ghioci.ai.impl;

import io.github.stefan_ghioci.ai.ClusteringAlgorithm;
import io.github.stefan_ghioci.processing.Constants;
import io.github.stefan_ghioci.processing.Tile;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;

public class TileClusteringAlgorithm extends ClusteringAlgorithm<Tile>
{
    private final Map<Tile, int[]> frequencies = new HashMap<>();

    @Override
    protected double distance(Tile tile1, Tile tile2)
    {
        int[] frequency1 = frequencies.computeIfAbsent(tile1, this::computeMappingFrequency);
        int[] frequency2 = frequencies.computeIfAbsent(tile2, this::computeMappingFrequency);

        return sqrt(IntStream.range(0, 4)
                             .mapToDouble(i -> (frequency1[i] - frequency2[i]) *
                                     (frequency1[i] - frequency2[i]))
                             .sum());
    }

    private int[] computeMappingFrequency(Tile tile)
    {
        int[] frequency = new int[4];

        int[][] mapping = tile.getColorMapping();

        for (int x = 0; x < Constants.TILE_SIZE; x++)
            for (int y = 0; y < Constants.TILE_SIZE; y++)
                frequency[mapping[x][y]]++;

        return frequency;
    }
}
