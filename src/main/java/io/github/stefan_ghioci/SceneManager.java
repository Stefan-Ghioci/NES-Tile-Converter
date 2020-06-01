package io.github.stefan_ghioci;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class SceneManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(SceneManager.class.getSimpleName());
    private static Stage stage;

    static List<Step> stepList;
    static Step currentStep;

    private SceneManager()
    {
    }

    public static void setStage(Stage stage)
    {
        SceneManager.stage = stage;
    }

    public static void setSteps(List<Step> steps)
    {
        stepList = steps;
    }

    public static void start()
    {
        LOGGER.info("Starting first step");
        currentStep = stepList.get(0);
        loadCurrentStep();
    }

    private static void loadCurrentStep()
    {
        LOGGER.info("Progress status: {}", getProgressStatusFormattedString());

        currentStep.initializeView(getProgressStatusFormattedString());

        Parent root = currentStep.getViewRoot();
        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    private static String getProgressStatusFormattedString()
    {
        StringJoiner joiner = new StringJoiner(" -> ");
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
        currentStep = stepList.get(stepList.indexOf(currentStep) + 1);

        loadCurrentStep();
    }

    public static void goToPreviousStep()
    {
        LOGGER.info("Loading previous step...");
        currentStep = stepList.get(stepList.indexOf(currentStep) - 1);

        loadCurrentStep();
    }


    public static void reset()
    {
        LOGGER.info("Resetting all steps...");
        start();
    }
}
