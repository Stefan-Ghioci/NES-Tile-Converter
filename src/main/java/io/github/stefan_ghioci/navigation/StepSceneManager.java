package io.github.stefan_ghioci.navigation;

import io.github.stefan_ghioci.navigation.base.Step;
import io.github.stefan_ghioci.tools.FileTools;
import io.github.stefan_ghioci.tools.Styling;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class StepSceneManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(StepSceneManager.class.getSimpleName());
    private static final Map<Parent, Scene> scenes = new HashMap<>();

    private static List<Step> stepList;
    private static Step currentStep;
    private static Stage stage;

    private StepSceneManager()
    {
        throw new UnsupportedOperationException("Static class should not be instantiated.");
    }

    public static void setStage(Stage stage)
    {
        StepSceneManager.stage = stage;
    }

    public static void setSteps(List<Step> steps)
    {
        stepList = steps;
    }

    public static void start()
    {
        LOGGER.info("Starting first step");

        Image image = FileTools.loadDefaultImage();
        currentStep = stepList.get(0);
        currentStep.initializeView(getProgressStatusFormattedString(), image);

        loadStage();
    }


    private static String getProgressStatusFormattedString()
    {
        StringJoiner joiner = new StringJoiner(" → ");
        int inclusiveBound = stepList.indexOf(currentStep);
        for (int i = 0; i <= inclusiveBound; i++)
        {
            String description = stepList.get(i).getDescription();
            joiner.add(description);
        }
        return joiner.toString();
    }

    public static void goToNextStep()
    {
        LOGGER.info("Loading next step...");

        Image image = currentStep.getView().getImage();

        currentStep = stepList.get(stepList.indexOf(currentStep) + 1);
        currentStep.initializeView(getProgressStatusFormattedString(), image);

        loadStage();
    }

    public static void goToPreviousStep()
    {
        LOGGER.info("Loading previous step...");

        currentStep = stepList.get(stepList.indexOf(currentStep) - 1);

        loadStage();
    }

    private static void loadStage()
    {
        LOGGER.info("Progress status: {}", getProgressStatusFormattedString());

        Parent root = currentStep.getView().getRoot();
        Scene scene = getScene(root);

        stage.setScene(scene);
        stage.show();
    }

    private static Scene getScene(Parent root)
    {
        if (scenes.get(root) == null)
            scenes.put(root, Styling.createScene(root));
        return scenes.get(root);
    }

}
