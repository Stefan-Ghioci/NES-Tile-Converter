package io.github.stefan_ghioci;

import javafx.scene.Parent;

public class Step
{
    private final String description;
    private final StepType type;
    private StepView view;
    private StepController controller;

    public String getDescription()
    {
        return description;
    }

    public Step(String description, StepType type, StepView view, StepController controller)
    {
        this.description = description;
        this.view = view;
        this.type = type;
        this.controller = controller;
    }

    public Parent getViewRoot()
    {
        return view.getRoot();
    }

    public void initializeView(String progressStatus)
    {
        view.initialize(progressStatus, type);
    }
}
