package io.github.stefan_ghioci.processing;

import javafx.scene.image.Image;

public class ConstraintValidator
{
    public static boolean validateImageSize(Image image)
    {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        return width % 16 == 0 && height % 16 == 0;
    }
}
