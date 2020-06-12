package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.processing.Color;
import io.github.stefan_ghioci.processing.Tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static io.github.stefan_ghioci.processing.Constants.TILE_GROUP_SIZE;
import static io.github.stefan_ghioci.processing.Constants.TILE_SIZE;

public class ColorTools
{

    private static List<Color> nesGrayscalePalette;

    public static Color bestMatch(Color color, List<Color> palette)
    {
        Color minDistanceColor = palette.get(0);
        double minDistance = Metrics.distanceBetween(color, palette.get(0));

        for (Color paletteColor : palette)
        {
            double distance = Metrics.distanceBetween(color, paletteColor);
            if (distance < minDistance)
            {
                minDistance = distance;
                minDistanceColor = paletteColor;
            }
        }
        return minDistanceColor;
    }

    public static List<Color> computeBestPalette(Color[][] colorMatrix, boolean forcedBlack, int size)
    {
        List<Color> palette = FileTools.loadNESPalette();

        Map<Color, Integer> histogram = palette.stream()
                                               .collect(Collectors.toMap(color -> color, color -> 0, (a, b) -> b));

        for (Color[] colors : colorMatrix)
            for (Color color : colors)
            {
                Color bestMatch = bestMatch(color, palette);
                histogram.put(bestMatch, histogram.get(bestMatch) + 1);
            }

        List<Color> bestPalette = histogram.entrySet()
                                           .stream()
                                           .sorted(Comparator.comparingInt((ToIntFunction<Map.Entry<Color, Integer>>) Map.Entry::getValue)
                                                             .reversed())
                                           .limit(size)
                                           .map(Map.Entry::getKey)
                                           .collect(Collectors.toList());

        if (forcedBlack && !bestPalette.contains(Color.BLACK))
        {
            bestPalette.remove(bestPalette.size() - 1);
            bestPalette.add(Color.BLACK);
        }

        return bestPalette;
    }

    public static List<Color> computeNESGrayscalePalette()
    {
        if (nesGrayscalePalette != null)
            return nesGrayscalePalette;

        List<Color> palette = FileTools.loadNESPalette()
                                       .stream()
                                       .filter(color -> color.getRed() == color.getGreen() && color.getGreen() == color
                                               .getBlue())
                                       .collect(Collectors.toList());

        nesGrayscalePalette = palette;
        return palette;
    }

    public static int getTileGroupCount(int width, int height)
    {
        return (width * height) / (TILE_GROUP_SIZE * TILE_GROUP_SIZE);
    }

    public static int getTileCount(int width, int height)
    {
        return (width * height) / (TILE_SIZE * TILE_SIZE);
    }

    public static List<Tile> colorMatrixToTiles(Color[][] colorMatrix, List<List<Color>> subPaletteConfig)
    {
        List<Tile> tiles = new ArrayList<>();

        List<Color>[][] subPaletteMapping = computeBestSubPaletteMapping(colorMatrix, subPaletteConfig);

        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        for (int y = 0; y < height; y += TILE_SIZE)
            for (int x = 0; x < width; x += TILE_SIZE)
            {
                int row = y / TILE_SIZE;
                int column = x / TILE_SIZE;

                List<Color> subPalette = subPaletteMapping[x / TILE_GROUP_SIZE][y / TILE_GROUP_SIZE];

                int[][] colorMapping = new int[TILE_SIZE][TILE_SIZE];
                for (int j = 0; j < TILE_SIZE; j++)
                    for (int i = 0; i < TILE_SIZE; i++)
                    {
                        colorMapping[i][j] = subPalette.indexOf(colorMatrix[x + i][y + j]);
                    }

                tiles.add(new Tile(row, column, colorMapping, subPalette));
            }
        return tiles;
    }

    public static List<Color>[][] computeBestSubPaletteMapping(Color[][] colorMatrix, List<List<Color>> subPaletteList)
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        int columns = width / TILE_GROUP_SIZE;
        int rows = height / TILE_GROUP_SIZE;

        List<Color>[][] subPaletteMapping = new List[columns][rows];

        for (int y = 0; y < height; y += TILE_GROUP_SIZE)
            for (int x = 0; x < width; x += TILE_GROUP_SIZE)
            {
                double bestTileGroupDistance = -1;
                List<Color> bestSubPalette = subPaletteList.get(0);

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

                subPaletteMapping[x / TILE_GROUP_SIZE][y / TILE_GROUP_SIZE] = bestSubPalette;
            }
        return subPaletteMapping;
    }

    public static Color[][] tilesToColorMatrix(List<Tile> tiles, int width, int height)
    {
        Color[][] colorMatrix = new Color[width][height];

        for (Tile tile : tiles)
        {
            int y = tile.getRow() * TILE_SIZE;
            int x = tile.getColumn() * TILE_SIZE;
            int[][] mapping = tile.getColorMapping();
            List<Color> subPalette = tile.getSubPalette();

            for (int j = 0; j < TILE_SIZE; j++)
                for (int i = 0; i < TILE_SIZE; i++)
                    colorMatrix[x + i][y + j] = subPalette.get(mapping[i][j]);
        }

        return colorMatrix;
    }
}
