package io.github.stefan_ghioci;

import javafx.scene.Parent;
import javafx.scene.image.Image;

public class Step
{
    private final String description;
    private final StepType type;
    private StepView view;

    public String getDescription()
    {
        return description;
    }

    public Step(String description, StepType type, StepView view)
    {
        this.description = description;
        this.view = view;
        this.type = type;
    }

    public Parent getViewRoot()
    {
        return view.getRoot();
    }

    public void initializeView(String progressStatus, Image image)
    {

        view.initialize(progressStatus, type, image);
    }

    public Image getImage()
    {
        return view.getImage();
    }
}
