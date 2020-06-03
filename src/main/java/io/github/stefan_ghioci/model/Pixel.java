package io.github.stefan_ghioci.model;


import java.util.Objects;

public class Pixel
{
    private final int red;

    private final int blue;
    private final int green;

    public Pixel(int red, int green, int blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Pixel(int rgb)
    {

        this.red = (rgb & 0xff0000) >> 16;
        this.green = (rgb & 0xff00) >> 8;
        this.blue = rgb & 0xff;
    }

    public static Pixel black()
    {
        return new Pixel(0, 0, 0);
    }

    public static Pixel difference(Pixel oldPixel, Pixel newPixel)
    {
        return new Pixel(oldPixel.red - newPixel.red,
                              oldPixel.green - newPixel.green,
                              oldPixel.blue - newPixel.blue);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel that = (Pixel) o;
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

    public int getLuminance()
    {
        return (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
    }
}
