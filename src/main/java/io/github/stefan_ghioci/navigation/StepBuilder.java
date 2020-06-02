package io.github.stefan_ghioci.navigation;

import io.github.stefan_ghioci.navigation.base.Phase;
import io.github.stefan_ghioci.navigation.base.Step;
import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.navigation.impl.load_image.LoadImageController;
import io.github.stefan_ghioci.navigation.impl.load_image.LoadImageView;

import java.util.ArrayList;
import java.util.List;

public class StepBuilder
{
    List<Step> stepList;

    public StepBuilder()
    {
        stepList = new ArrayList<>();
    }

    private StepBuilder addStep(Step step)
    {
        stepList.add(step);
        return this;
    }

    public StepBuilder addLoadImageStep()
    {
        //TODO: create Ctrl&View
        StepController controller = new StepController(){};
        StepView view = new StepView(controller){};
        controller.setView(view);

        String description = "Load Image";
        Phase phase = Phase.INITIAL;

        return addStep(new Step(description, phase, view));
    }

    public StepBuilder addPreProcessingStep()
    {
        //TODO: create Ctrl&View
        StepController controller = new StepController(){};
        StepView view = new StepView(controller){};
        controller.setView(view);

        String description = "Pre-Processing";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addReconstructionStep()
    {
        //TODO: create Ctrl&View
        StepController controller = new StepController(){};
        StepView view = new StepView(controller){};
        controller.setView(view);

        String description = "Reconstruction";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addCompressionStep()
    {
        //TODO: create Ctrl&View
        StepController controller = new StepController(){};
        StepView view = new StepView(controller){};
        controller.setView(view);

        String description = "Compression";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addFinalResultStep()
    {
        //TODO: create Ctrl&View
        StepController controller = new StepController(){};
        StepView view = new StepView(controller){};
        controller.setView(view);

        String description = "Final Result";
        Phase phase = Phase.FINAL;

        return addStep(new Step(description, phase, view));

    }

    public List<Step> build()
    {
        List<Step> stepList = new ArrayList<>(this.stepList);
        this.stepList.clear();
        return stepList;
    }
}
