package io.github.stefan_ghioci.navigation.impl.load_image;

import io.github.stefan_ghioci.navigation.base.StepView;
import javafx.scene.control.Button;

public class ImageLoadView extends StepView
{
    private final ImageLoadController controller;

    public ImageLoadView(ImageLoadController controller)
    {
        super(controller);
        this.controller = ((ImageLoadController) stepController);
    }

    @Override
    protected void initializeLeft()
    {
        Button loadImageButton = new Button();

        loadImageButton.setText("Load Image...");
        loadImageButton.setOnAction(event -> controller.handleImageLoad());

        root.setLeft(loadImageButton);
    }

    @Override
    protected void initializeRight()
    {

    }
}
