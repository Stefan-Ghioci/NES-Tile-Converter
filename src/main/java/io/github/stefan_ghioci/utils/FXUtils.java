package io.github.stefan_ghioci.utils;

import io.github.stefan_ghioci.model.Pixel;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Region;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FXUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FXUtils.class.getSimpleName());

    public static Pixel[][] getPixelMatrix(Image image)
    {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        Pixel[][] pixelMatrix = new Pixel[width][height];

        PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
            {
                pixelMatrix[x][y] = new Pixel(pixelReader.getArgb(x, y));
            }

        return pixelMatrix;
    }

    public static void showAlert(String title, String message, Alert.AlertType type)
    {
        Alert alert = new Alert(type);

        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait();
    }
}
