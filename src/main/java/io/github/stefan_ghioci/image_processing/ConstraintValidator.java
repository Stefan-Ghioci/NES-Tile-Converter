package io.github.stefan_ghioci.image_processing;

import javafx.scene.image.Image;

public class ConstraintValidator
{

    public static boolean validateImageSize(Image image)
    {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        return width % Constraints.TILE_GROUP_SIZE == 0 && height % Constraints.TILE_GROUP_SIZE == 0;
    }
}
