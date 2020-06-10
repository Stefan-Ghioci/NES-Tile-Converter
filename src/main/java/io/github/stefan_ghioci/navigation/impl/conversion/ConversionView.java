package io.github.stefan_ghioci.navigation.impl.conversion;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import javafx.scene.control.Button;
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

        Button saveImageButton = new Button();
        saveImageButton.setText("Save Image");
        saveImageButton.setOnAction(event -> controller.handleSaveImage());

        return new VBox(saveImageButton);
    }
}
