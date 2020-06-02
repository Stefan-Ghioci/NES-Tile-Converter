package io.github.stefan_ghioci.navigation;

import io.github.stefan_ghioci.navigation.base.Phase;
import io.github.stefan_ghioci.navigation.base.Step;
import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.navigation.impl.compression.CompressionController;
import io.github.stefan_ghioci.navigation.impl.compression.CompressionView;
import io.github.stefan_ghioci.navigation.impl.final_result.FinalResultController;
import io.github.stefan_ghioci.navigation.impl.final_result.FinalResultStepView;
import io.github.stefan_ghioci.navigation.impl.load_image.ImageLoadController;
import io.github.stefan_ghioci.navigation.impl.load_image.ImageLoadView;
import io.github.stefan_ghioci.navigation.impl.preprocessing.PreProcessingController;
import io.github.stefan_ghioci.navigation.impl.preprocessing.PreProcessingView;
import io.github.stefan_ghioci.navigation.impl.reconstruction.ReconstructionController;
import io.github.stefan_ghioci.navigation.impl.reconstruction.ReconstructionView;

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

    public StepBuilder addImageLoadStep()
    {
        ImageLoadController controller = new ImageLoadController();
        StepView view = new ImageLoadView(controller);
        controller.setView(view);

        String description = "Image Load";
        Phase phase = Phase.INITIAL;

        return addStep(new Step(description, phase, view));
    }

    public StepBuilder addPreProcessingStep()
    {
        PreProcessingController controller = new PreProcessingController();
        StepView view = new PreProcessingView(controller);
        controller.setView(view);

        String description = "Pre-Processing";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addReconstructionStep()
    {
        ReconstructionController controller = new ReconstructionController();
        StepView view = new ReconstructionView(controller);
        controller.setView(view);

        String description = "Reconstruction";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addCompressionStep()
    {
        //TODO: create Ctrl&View
        CompressionController controller = new CompressionController();
        StepView view = new CompressionView(controller);
        controller.setView(view);

        String description = "Compression";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addFinalResultStep()
    {
        //TODO: create Ctrl&View
        FinalResultController controller = new FinalResultController();
        StepView view = new FinalResultStepView(controller);
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
