package io.github.stefan_ghioci.navigation;

import io.github.stefan_ghioci.navigation.base.Phase;
import io.github.stefan_ghioci.navigation.base.Step;
import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.navigation.impl.compression.CompressionController;
import io.github.stefan_ghioci.navigation.impl.compression.CompressionView;
import io.github.stefan_ghioci.navigation.impl.conversion.ConversionController;
import io.github.stefan_ghioci.navigation.impl.conversion.ConversionView;
import io.github.stefan_ghioci.navigation.impl.image_load.ImageLoadController;
import io.github.stefan_ghioci.navigation.impl.image_load.ImageLoadView;
import io.github.stefan_ghioci.navigation.impl.preprocessing.PreProcessingController;
import io.github.stefan_ghioci.navigation.impl.preprocessing.PreProcessingView;
import io.github.stefan_ghioci.navigation.impl.reconstruction.ReconstructionController;
import io.github.stefan_ghioci.navigation.impl.reconstruction.ReconstructionView;

import java.util.ArrayList;
import java.util.List;

public class StepBuilder
{
    final List<Step> stepList;

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
        StepController controller = new ImageLoadController();
        StepView view = new ImageLoadView(controller);
        controller.setView(view);

        String description = "Image Load";
        Phase phase = Phase.INITIAL;

        return addStep(new Step(description, phase, view));
    }

    public StepBuilder addPreProcessingStep()
    {
        StepController controller = new PreProcessingController();
        StepView view = new PreProcessingView(controller);
        controller.setView(view);

        String description = "Pre-Processing";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addReconstructionStep()
    {
        StepController controller = new ReconstructionController();
        StepView view = new ReconstructionView(controller);
        controller.setView(view);

        String description = "Reconstruction";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addCompressionStep()
    {
        StepController controller = new CompressionController();
        StepView view = new CompressionView(controller);
        controller.setView(view);

        String description = "Compression";
        Phase phase = Phase.STANDARD;

        return addStep(new Step(description, phase, view));

    }

    public StepBuilder addConversionStep()
    {
        StepController controller = new ConversionController();
        StepView view = new ConversionView(controller);
        controller.setView(view);

        String description = "Conversion";
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
