package io.github.stefan_ghioci.navigation.base;

import io.github.stefan_ghioci.navigation.StepSceneManager;
import javafx.scene.control.Button;

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
        }
    }

    public void handleResetChanges()
    {
        stepView.setImage(stepView.getInitialImage());
    }

    protected void setNavigationBarDisabled(boolean disabled)
    {
        stepView.navigationButtonBox.getChildren()
                                    .filtered(node -> node.getClass().equals(Button.class))
                                    .forEach((button -> button.setDisable(disabled)));
    }
}
