package io.github.stefan_ghioci.navigation.impl.preprocessing;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import javafx.scene.paint.Color;

import java.util.Comparator;

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
        view.palette.sort(Comparator.comparingDouble(Color::getHue));
    }

    public void handleDeleteSelectedColor()
    {
        view.palette.remove(view.paletteListView.getSelectionModel().getSelectedItem());
        view.palette.sort(Comparator.comparing(Color::toString));
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
}
