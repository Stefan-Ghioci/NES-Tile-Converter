package io.github.stefan_ghioci.tools;

import io.github.stefan_ghioci.processing.Color;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileTools
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileTools.class.getSimpleName());

    private static List<Color> nesPalette;

    public static String loadText(String code)
    {

        URL resource = FileTools.class.getResource("text/" + code + ".txt");

        try (Scanner scanner = new Scanner(resource.openStream(),
                                           StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
        catch (IOException e)
        {
            LOGGER.info("Could not load text with code \"{}\". Cause: {}", code, e.getMessage());
            return "Error loading text.";
        }
    }

    public static List<Color> loadNESPalette()
    {
        if (nesPalette != null)
            return nesPalette;

        LOGGER.info("Loading NES Palette...");

        InputStream resourceAsStream = FileTools.class.getResourceAsStream("nes_palette.csv");
        List<Color> palette = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream)))
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] rgb = line.split(",");

                int red = Integer.parseInt(rgb[0]);
                int green = Integer.parseInt(rgb[1]);
                int blue = Integer.parseInt(rgb[2]);

                palette.add(new Color(red, green, blue));
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to load NES palette file. Cause: {}", e.getMessage());
        }

        nesPalette = palette;
        LOGGER.info("Loaded {}-color palette", palette.size());

        return palette;
    }

    public static Image loadDefaultImage()
    {
        LOGGER.info("Loading default...");
        return new Image(FileTools.class.getResourceAsStream("image_placeholder.bmp"));
    }
}
