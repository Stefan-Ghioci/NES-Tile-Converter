package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.model.Pixel;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FXTools
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FXTools.class.getSimpleName());

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

    public static ListCell<Color> createColorListCell()
    {
        return new ListCell<>()
        {
            @Override
            protected void updateItem(Color color, boolean empty)
            {
                super.updateItem(color, empty);
                if (!empty)
                {
                    Rectangle rectangle = new Rectangle(100, 20);
                    rectangle.setFill(color);

                    setGraphic(rectangle);
                    setText(getColorCode(color));
                    setFont(Font.font("Courier New"));
                }
                else
                {
                    setText(null);
                    setGraphic(null);
                }
            }
        };
    }

    private static String getColorCode(Color color)
    {
        return "#" + color.toString().substring(3, 9);
    }

    public static List<Color> getNESPalette()
    {
        return FileTools.loadNESPalette()
                        .stream()
                        .map(FXTools::pixelToColor)
                        .collect(Collectors.toList());
    }

    private static Color pixelToColor(Pixel pixel)
    {
        double red = pixel.getRed() / 255.0F;
        double green = pixel.getGreen() / 255.0F;
        double blue = pixel.getBlue() / 255.0F;

        return new Color(red, green, blue, 1.0F);
    }
}
