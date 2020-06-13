package io.github.stefan_ghioci.navigation.impl.conversion;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.tools.Styling;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class ConversionView extends StepView
{
    private ConversionController controller;

    public ConversionView(StepController controller)
    {
        super(controller);
        this.controller = (ConversionController) this.stepController;
    }

    @Override
    protected Pane initializeLeftPane()
    {

        return new Pane();
    }

    @Override
    protected Pane initializeRightPane()
    {
        Label exportLabel = Styling.createLabel();
        exportLabel.setText("Export");

        Button saveFullImageButton = Styling.createPrimaryButton();
        saveFullImageButton.setText("Save full image");
        saveFullImageButton.setOnAction(event -> controller.handleSaveFullImage());

        Button saveTileSetButton = Styling.createPrimaryButton();
        saveTileSetButton.setText("Save tile set");
        saveTileSetButton.setOnAction(event -> controller.handleSaveTileSet());

        Button savePaletteButton = Styling.createPrimaryButton();
        savePaletteButton.setText("Save palette");
        savePaletteButton.setOnAction(event -> controller.handleSavePalette());


        VBox vBox = Styling.createRightControlsVBox();
        vBox.getChildren().addAll(exportLabel, saveFullImageButton, saveTileSetButton, savePaletteButton);
        return vBox;
    }
}
