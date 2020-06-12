package io.github.stefan_ghioci.navigation.impl.preprocessing;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.processing.Color;
import io.github.stefan_ghioci.processing.Constants;
import io.github.stefan_ghioci.processing.PreProcessing;
import io.github.stefan_ghioci.tools.ColorTools;
import io.github.stefan_ghioci.tools.FXTools;
import io.github.stefan_ghioci.tools.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PreProcessingController extends StepController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProcessingController.class.getSimpleName());

    private PreProcessingView view;

    @Override
    public void setView(StepView view)
    {
        super.setView(view);
        this.view = (PreProcessingView) this.stepView;
    }

    public void handleAddPickedColor()
    {
        javafx.scene.paint.Color pickedColor = view.colorPicker.getValue();
        LOGGER.info("Adding color {} to current palette...", FXTools.getColorCode(pickedColor));
        view.palette.add(0, pickedColor);
    }

    public void handleDeleteSelectedColor()
    {
        javafx.scene.paint.Color selectedColor = view.paletteListView.getSelectionModel().getSelectedItem();
        LOGGER.info("Removing color {} from current palette...", FXTools.getColorCode(selectedColor));
        view.palette.remove(selectedColor);
    }

    public void handleAddButtonStatus()
    {
        view.addButton.setDisable(view.palette.contains(view.colorPicker.getValue()));
    }

    public void handleColorSelected()
    {
        view.deleteButton.setDisable(view.paletteListView.getSelectionModel().getSelectedItem() == null);
    }


    public void handlePaletteChanged()
    {
        int paletteSize = view.palette.size();

        view.paletteSizeLabel.setText("Palette size: " + paletteSize);
        view.quantizeButton.setDisable(paletteSize <= 1);

        handleAddButtonStatus();
    }

    public void handleLoadNESPalette()
    {
        view.palette.setAll(FXTools.colorListToFXColorList(FileTools.loadNESPalette()));
    }

    public void handleLoadBestPalette()
    {
        Color[][] colorMatrix = FXTools.imageToColorMatrix(view.getInitialImage());
        List<Color> bestPalette = ColorTools.computeBestPalette(colorMatrix, false, Constants.MAX_PALETTE_SIZE);

        view.palette.setAll(FXTools.colorListToFXColorList(bestPalette));
    }

    public void handleLoadNESGrayscalePalette()
    {
        view.palette.setAll(FXTools.colorListToFXColorList(ColorTools.computeNESGrayscalePalette()));
    }

    public void handleQuantization()
    {
        Color[][] colorMatrix = FXTools.imageToColorMatrix(view.getInitialImage());
        List<Color> palette = FXTools.fxColorListToColorList(view.palette);
        PreProcessing.Dithering dithering = PreProcessing.Dithering.valueOf(view.ditheringChoiceBox.getSelectionModel()
                                                                                                   .getSelectedItem());

        view.setImage(FXTools.colorMatrixToImage(PreProcessing.quantize(colorMatrix, palette, dithering)));
    }
}
