package io.github.stefan_ghioci.navigation.impl.image_load;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.tools.Styling;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ImageLoadView extends StepView
{
    private final ImageLoadController controller;
    Button loadImageButton;

    public ImageLoadView(StepController controller)
    {
        super(controller);
        this.controller = ((ImageLoadController) stepController);
    }

    @Override
    protected Pane initializeLeftPane()
    {
        Label loadImageLabel = Styling.createLabel();
        loadImageLabel.setText("Import");

        loadImageButton = Styling.createPrimaryButton();
        loadImageButton.setText("Load from file...");
        loadImageButton.setOnAction(event -> controller.handleImageLoad());

        VBox vBox = Styling.createLeftControlsVBox();
        vBox.getChildren().addAll(loadImageLabel, loadImageButton);
        return vBox;
    }

    @Override
    protected Pane initializeRightPane()
    {
        return new Pane();
    }
}
