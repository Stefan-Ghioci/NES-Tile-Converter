package io.github.stefan_ghioci.processing;


import java.util.Objects;

public class Color
{
    public static final Color BLACK = new Color(0, 0, 0);
    private final int red;
    private final int green;
    private final int blue;

    public Color(int red, int green, int blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int rgb)
    {

        this.red = (rgb & 0xff0000) >> 16;
        this.green = (rgb & 0xff00) >> 8;
        this.blue = rgb & 0xff;
    }

    public static Color difference(Color oldColor, Color newColor)
    {
        return new Color(oldColor.red - newColor.red,
                         oldColor.green - newColor.green,
                         oldColor.blue - newColor.blue);
    }


    public static Color grayOf(int value)
    {
        return new Color(value, value, value);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color that = (Color) o;
        return red == that.red &&
                blue == that.blue &&
                green == that.green;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(red, blue, green);
    }

    public int getRed()
    {
        return red;
    }

    public int getBlue()
    {
        return blue;
    }

    public int getGreen()
    {
        return green;
    }


}
