package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.processing.Color;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class FXTools
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FXTools.class.getSimpleName());

    public static Color[][] imageToColorMatrix(Image image)
    {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        Color[][] colorMatrix = new Color[width][height];

        PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
            {
                colorMatrix[x][y] = new Color(pixelReader.getArgb(x, y));
            }

        return colorMatrix;
    }

    public static void showAlert(String title, String message, Alert.AlertType type)
    {
        Alert alert = Styling.createAlert(type, title, message);
        alert.showAndWait();
    }

    public static String getColorCode(javafx.scene.paint.Color color)
    {
        return "#" + color.toString().substring(2, 8);
    }

    public static List<javafx.scene.paint.Color> colorListToFXColorList(List<Color> colors)
    {
        return colors.stream()
                     .map(FXTools::colorToFXColor)
                     .collect(Collectors.toList());
    }

    public static javafx.scene.paint.Color colorToFXColor(Color color)
    {
        double red = color.getRed() / 255.0F;
        double green = color.getGreen() / 255.0F;
        double blue = color.getBlue() / 255.0F;

        return new javafx.scene.paint.Color(red, green, blue, 1.0F);
    }

    public static List<Color> fxColorListToColorList(List<javafx.scene.paint.Color> fxColors)
    {
        return fxColors.stream()
                       .map(FXTools::fxColorToColor)
                       .collect(Collectors.toList());
    }

    private static Color fxColorToColor(javafx.scene.paint.Color color)
    {
        int red = Math.toIntExact(Math.round(color.getRed() * 255.0F));
        int green = Math.toIntExact(Math.round(color.getGreen() * 255.0F));
        int blue = Math.toIntExact(Math.round(color.getBlue() * 255.0F));

        return new Color(red, green, blue);
    }

    public static Image colorMatrixToImage(Color[][] colorMatrix)
    {
        int width = colorMatrix.length;
        int height = colorMatrix[0].length;

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                pixelWriter.setColor(x, y, colorToFXColor(colorMatrix[x][y]));

        return writableImage;
    }

    public static BufferedImage imageToBufferedImage(Image image)
    {
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, TYPE_INT_RGB);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));

        return bufferedImage;
    }
}
