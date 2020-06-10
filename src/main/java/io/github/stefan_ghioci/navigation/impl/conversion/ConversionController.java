package io.github.stefan_ghioci.navigation.impl.conversion;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.tools.FileTools;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ConversionController extends StepController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionController.class.getSimpleName());
    private ConversionView view;


    @Override
    public void setView(StepView stepView)
    {
        super.setView(stepView);
        this.view = (ConversionView) stepView;
    }


    public void handleSaveImage()
    {
        LOGGER.info("Opening file chooser...");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP", "*.bmp"));
        File file = fileChooser.showSaveDialog(view.getRoot().getScene().getWindow());

        if (file != null)
        {
            FileTools.saveImageToFile(view.getInitialImage(), file);
        }
    }
}
