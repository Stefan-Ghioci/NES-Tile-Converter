package io.github.stefan_ghioci.processing;


import java.util.Objects;

public class Color
{
    private final int red;

    private final int blue;
    private final int green;

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

    public static Color black()
    {
        return new Color(0, 0, 0);
    }

    public static Color difference(Color oldColor, Color newColor)
    {
        return new Color(oldColor.red - newColor.red,
                         oldColor.green - newColor.green,
                         oldColor.blue - newColor.blue);
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

    public int toInt()
    {
        int rgb;
        rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
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
