package io.github.stefan_ghioci;

public class StepController
{

    private StepView view;

    public StepController()
    {
    }

    public void setView(StepView view)
    {
        this.view = view;
    }

    public void handleNavigation(Navigation option)
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
