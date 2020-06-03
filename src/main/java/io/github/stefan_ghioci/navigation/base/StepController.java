package io.github.stefan_ghioci.navigation.base;

import io.github.stefan_ghioci.state.StepSceneManager;

public abstract class StepController
{

    protected StepView stepView;

    public StepController()
    {
    }

    public void setView(StepView view)
    {
        this.stepView = view;
    }

    public void handleNavigation(ButtonType option)
    {
        switch (option)
        {
            case BACK:
                StepSceneManager.goToPreviousStep();
                break;
            case NEXT:
                StepSceneManager.goToNextStep();
                break;
            case RESET:
                StepSceneManager.reset();
                break;
        }
    }
}
