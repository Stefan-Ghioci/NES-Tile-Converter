package io.github.stefan_ghioci.navigation.base;

import io.github.stefan_ghioci.state.SceneManager;

public abstract class StepController
{

    protected StepView view;

    public StepController()
    {
    }

    public void setView(StepView view)
    {
        this.view = view;
    }

    public void handleNavigation(ButtonType option)
    {
        switch (option)
        {
            case BACK:
                SceneManager.goToPreviousStep();
                break;
            case NEXT:
                SceneManager.goToNextStep();
                break;
            case RESET:
                SceneManager.reset();
                break;
        }
    }
}
