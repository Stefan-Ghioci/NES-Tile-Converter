package io.github.stefan_ghioci;

import io.github.stefan_ghioci.navigation.StepBuilder;
import io.github.stefan_ghioci.navigation.StepSceneManager;
import io.github.stefan_ghioci.navigation.base.Step;
import io.github.stefan_ghioci.tools.Styling;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;


public class App extends Application
{

    @Override
    public void start(Stage stage)
    {
        Styling.initializeStage(stage);

        List<Step> steps = new StepBuilder()
                .addImageLoadStep()
                .addPreProcessingStep()
                .addReconstructionStep()
                .addCompressionStep()
                .addConversionStep()
                .build();

        StepSceneManager.setStage(stage);
        StepSceneManager.setSteps(steps);
        StepSceneManager.start();
    }


    public static void main(String[] args)
    {
        launch();
    }

}