package io.github.stefan_ghioci.navigation.impl.load_image;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.processing.ConstraintValidator;
import io.github.stefan_ghioci.utils.FXUtils;
import io.github.stefan_ghioci.utils.StringUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ImageLoadController extends StepController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageLoadController.class.getSimpleName());

    public void handleImageLoad()
    {
        LOGGER.info("Opening file chooser...");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP", "*.bmp"));
        File file = fileChooser.showOpenDialog(view.getRoot().getScene().getWindow());

        LOGGER.info("Validating chosen file...");

        Image image = new Image(file.toURI().toString());
        boolean isValid = ConstraintValidator.validateImageSize(image);

        if (isValid)
        {
            LOGGER.info("Image is valid, replacing previous image...");
            view.setImage(image);
        }
        else
        {
            LOGGER.info("Invalid image, showing error message...");
            FXUtils.showAlert("Validation Error",
                              StringUtils.loadText("invalid_size"),
                              Alert.AlertType.ERROR);
        }
    }
}
