package io.github.stefan_ghioci.state;

import io.github.stefan_ghioci.navigation.base.Step;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ImageManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageManager.class.getSimpleName());

    private static final Map<Step, Image> states = new HashMap<>();

    private ImageManager()
    {
        throw new UnsupportedOperationException("Static class should not be instantiated.");
    }

    public static void save(Step step, Image image)
    {
        LOGGER.info("Saving image state for step {}...", step.getDescription());
        states.put(step, image);
    }

    public static void wipe()
    {
        LOGGER.info("Wiping all saved states...");
        states.clear();
    }

    public static Image restore(Step step)
    {

        if (states.get(step) == null)
        {
            LOGGER.info("No image state found for step {}", step.getDescription());
            return loadDefault();
        }
        LOGGER.info("Restoring image state for step {} ...", step.getDescription());
        return states.get(step);
    }

    public static Image loadDefault()
    {
        LOGGER.info("Loading default...");
        return new Image(ImageManager.class.getResourceAsStream("image_placeholder.bmp"));
    }
}
