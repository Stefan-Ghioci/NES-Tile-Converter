package io.github.stefan_ghioci.navigation.impl.preprocessing;

import io.github.stefan_ghioci.image_processing.PreProcessing;
import io.github.stefan_ghioci.model.Color;
import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.tools.FXTools;
import io.github.stefan_ghioci.tools.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        view.palette.add(0, view.colorPicker.getValue());
    }

    public void handleDeleteSelectedColor()
    {
        view.palette.remove(view.paletteListView.getSelectionModel().getSelectedItem());
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

        view.paletteSizeText.setText("Palette size: " + paletteSize);
        view.quantizeButton.setDisable(paletteSize <= 1);

        handleAddButtonStatus();
    }

    public void handleLoadNESPalette()
    {
        view.palette.setAll(FXTools.colorListToFXColorList(FileTools.loadNESPalette()));
    }

    public void handleLoadBestPalette()
    {
        view.palette.setAll(FXTools.colorListToFXColorList(PreProcessing.computeBestPalette(FXTools.imageToColorMatrix(
                view.getInitialImage()))));
    }

    public List<String> getDitheringMethods()
    {
        return Stream.of(PreProcessing.DitheringMethod.values())
                     .map(PreProcessing.DitheringMethod::name)
                     .collect(Collectors.toList());
    }

    public void handleQuantization()
    {
        Color[][] colorMatrix = FXTools.imageToColorMatrix(view.getInitialImage());
        List<Color> palette = FXTools.fxColorListToColorList(view.palette);
        PreProcessing.DitheringMethod ditheringMethod = PreProcessing.DitheringMethod.valueOf(view.ditheringChoiceBox.getSelectionModel()
                                                                                                                     .getSelectedItem());

        view.setImage(FXTools.colorMatrixToImage(PreProcessing.quantize(colorMatrix, palette, ditheringMethod)));
    }
}
