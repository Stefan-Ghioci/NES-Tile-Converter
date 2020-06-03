package io.github.stefan_ghioci.image_processing;

import io.github.stefan_ghioci.model.Color;

public class Dithering
{
    public static void dither(Color[][] colorMatrix, int x, int y, Color oldColor, Color newColor, Method ditheringMethod)
    {
        switch (ditheringMethod)
        {
            case None:
                break;
        }
    }

    public enum Method
    {
        None,
        FloydSteinberg,
        Pattern
    }
}
