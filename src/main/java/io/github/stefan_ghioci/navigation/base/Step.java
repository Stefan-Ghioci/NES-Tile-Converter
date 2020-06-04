package io.github.stefan_ghioci.navigation.base;

import javafx.scene.image.Image;

public class Step
{
    private final String description;
    private final Phase phase;
    private StepView view;

    public String getDescription()
    {
        return description;
    }

    public Step(String description, Phase phase, StepView view)
    {
        this.description = description;
        this.view = view;
        this.phase = phase;
    }

    public StepView getView()
    {
        return view;
    }

    public void initializeView(String progressStatus, Image image)
    {

        view.initialize(progressStatus, phase, image);
    }

}
