package io.github.stefan_ghioci.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.stefan_ghioci.processing.Constants.*;

public class Conversion
{
    public static Color[][] createTileSet(List<Tile> tiles, List<List<Color>> subPaletteList)
    {
        List<Tile> distinctTiles = getUniqueTiles(tiles);

        int width = TILE_SET_SIZE;
        int height = (int) Math.ceil(1.0 * distinctTiles.size() / (width / TILE_SIZE)) * SUB_PALETTE_COUNT * TILE_SIZE;

        Color neutralColor = subPaletteList.get(0).get(0);
        Color[][] tileSet = initializeColorMatrix(width, height, neutralColor);

        int x = 0;
        int y = 0;
        for (List<Color> subPalette : subPaletteList)
        {
            for (Tile tile : distinctTiles)
            {
                if (x == width)
                {
                    x = 0;
                    y += TILE_SIZE;
                }
                for (int j = 0; j < TILE_SIZE; j++)
                    for (int i = 0; i < TILE_SIZE; i++)
                        tileSet[x + i][y + j] = subPalette.get(tile.getColorMapping()[i][j]);
                x += TILE_SIZE;

            }
            x = 0;
            y += TILE_SIZE;
        }

        return tileSet;
    }

    public static Color[][] initializeColorMatrix(int width, int height, Color color)
    {
        Color[][] colorMatrix = new Color[width][height];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                colorMatrix[x][y] = color;
        return colorMatrix;
    }

    public static List<Tile> getUniqueTiles(List<Tile> tiles)
    {
        List<Tile> distinctTiles = new ArrayList<>();

        List<int[][]> mappings = new ArrayList<>();

        for (Tile tile : tiles)
        {
            boolean found = false;
            for (int[][] mapping : mappings)
                if (Arrays.deepEquals(mapping, tile.getColorMapping()))
                    found = true;
            if (!found)
            {
                mappings.add(tile.getColorMapping());
                distinctTiles.add(tile);
            }
        }
        return distinctTiles;
    }
}
