package io.github.stefan_ghioci;

import io.github.stefan_ghioci.navigation.StepBuilder;
import io.github.stefan_ghioci.navigation.StepSceneManager;
import io.github.stefan_ghioci.navigation.base.Step;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;


public class App extends Application
{

    @Override
    public void start(Stage stage)
    {
        initializeStage(stage);

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


    private void initializeStage(Stage stage)
    {
        Image icon = new Image(App.class.getResourceAsStream("icon_placeholder.png"));
        String title = "NES Tile Converter";

        stage.getIcons().add(icon);
        stage.setTitle(title);
    }

    public static void main(String[] args)
    {
        launch();
    }

}