package io.github.stefan_ghioci.processing;

import java.util.List;

public class Tile
{
    private final int row;
    private final int column;
    private final int[][] colorMapping;
    private final List<Color> subPalette;

    public Tile(int row, int column, int[][] colorMapping, List<Color> subPalette)
    {
        this.row = row;
        this.column = column;
        this.colorMapping = colorMapping;
        this.subPalette = subPalette;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public int[][] getColorMapping()
    {
        return colorMapping;
    }

    public List<Color> getSubPalette()
    {
        return subPalette;
    }
}
