package io.github.stefan_ghioci.navigation.impl.load_image;

import io.github.stefan_ghioci.navigation.base.StepController;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;

public class ImageLoadController extends StepController
{

    public void handleImageLoad()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP", "*.bmp"));

        File file = fileChooser.showOpenDialog(view.getRoot().getScene().getWindow());
        if (file != null)
        {
            Image image = new Image(file.toURI().toString());
            view.setImage(image);
        }
    }
}
