package io.github.stefan_ghioci.navigation.impl.reconstruction;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;

public class ReconstructionController extends StepController
{
    private ReconstructionView view;

    @Override
    public void setView(StepView view)
    {
        super.setView(view);
        this.view = (ReconstructionView) this.stepView;
    }

    public void handleReconstruction()
    {

    }
}
