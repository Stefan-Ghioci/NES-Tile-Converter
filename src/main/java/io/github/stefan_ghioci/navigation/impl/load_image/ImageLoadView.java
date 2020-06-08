package io.github.stefan_ghioci.navigation.impl.load_image;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
        loadImageButton = new Button();
        loadImageButton.setText("Load Image...");
        loadImageButton.setOnAction(event -> controller.handleImageLoad());

        return new StackPane(loadImageButton);
    }

    @Override
    protected Pane initializeRightPane()
    {
        return null;
    }
}
