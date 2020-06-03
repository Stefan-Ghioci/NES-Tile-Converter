package io.github.stefan_ghioci.navigation.impl.preprocessing;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.processing.PreProcessing;
import io.github.stefan_ghioci.tools.FXTools;
import io.github.stefan_ghioci.tools.FileTools;

public class PreProcessingController extends StepController
{
    private PreProcessingView view;

    @Override
    public void setView(StepView view)
    {
        super.setView(view);
        this.view = (PreProcessingView) this.stepView;
    }

    public void handleAddPickedColor()
    {
        view.palette.add(view.colorPicker.getValue());
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
        view.paletteSizeText.setText("Palette size: " + view.palette.size());
        handleAddButtonStatus();
    }

    public void handleLoadNESPalette()
    {
        view.palette.setAll(FXTools.colorListToFXColorList(FileTools.loadNESPalette()));
    }

    public void handleLoadBestPalette()
    {
        view.palette.setAll(FXTools.colorListToFXColorList(PreProcessing.computeBestPalette(FXTools.getColorMatrix(view.getImage()))));
    }
}
