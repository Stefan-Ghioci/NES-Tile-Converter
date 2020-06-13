package io.github.stefan_ghioci.navigation.impl.conversion;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.processing.*;
import io.github.stefan_ghioci.tools.FXTools;
import io.github.stefan_ghioci.tools.FileTools;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

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


    public void handleSaveFullImage()
    {
        saveImage(view.getInitialImage());
    }

    public void saveImage(Image image)
    {
        LOGGER.info("Opening file chooser...");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP", "*.bmp"));
        File file = fileChooser.showSaveDialog(view.getRoot().getScene().getWindow());

        if (file != null)
            FileTools.saveImageToFile(FXTools.imageToBufferedImage(image), file);
    }

    public void handleSaveTileSet()
    {
        List<List<Color>> subPaletteList = Reconstruction.getLastResult();

        if (subPaletteList == null)
        {
            FXTools.showAlert("Conversion Error", FileTools.loadText("skipped_reconstruction"), Alert.AlertType.ERROR);
            return;
        }

        List<Tile> tiles = Compression.getLastResult();

        if (tiles == null)
        {
            FXTools.showAlert("Conversion Error", FileTools.loadText("skipped_compression"), Alert.AlertType.ERROR);
            return;
        }

        Color[][] tileSet = Conversion.createTileSet(tiles, subPaletteList);

        saveImage(FXTools.colorMatrixToImage(tileSet));
    }

    public void handleSavePalette()
    {
        List<List<Color>> subPaletteList = Reconstruction.getLastResult();

        if (subPaletteList == null)
        {
            FXTools.showAlert("Conversion Error", FileTools.loadText("skipped_reconstruction"), Alert.AlertType.ERROR);
            return;
        }

        Color[][] palette = Conversion.createPalette(subPaletteList);
        saveImage(FXTools.colorMatrixToImage(palette));
    }
}
