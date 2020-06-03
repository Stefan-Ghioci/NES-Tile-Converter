package io.github.stefan_ghioci.state;

import io.github.stefan_ghioci.navigation.base.Step;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.StringJoiner;

public class StepSceneManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(StepSceneManager.class.getSimpleName());
    private static Stage stage;

    static List<Step> stepList;
    static Step currentStep;

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

        Image image = ImageStateManager.loadDefault();
        currentStep = stepList.get(0);

        LOGGER.info("Progress status: {}", getProgressStatusFormattedString());



        currentStep.initializeView(getProgressStatusFormattedString(), image);

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

        Image image = currentStep.getImage();
        ImageStateManager.save(currentStep, image);

        currentStep = stepList.get(stepList.indexOf(currentStep) + 1);

        LOGGER.info("Progress status: {}", getProgressStatusFormattedString());


        currentStep.initializeView(getProgressStatusFormattedString(), image);

        Parent root = currentStep.getViewRoot();
        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    public static void goToPreviousStep()
    {
        LOGGER.info("Loading previous step...");
        currentStep = stepList.get(stepList.indexOf(currentStep) - 1);


        LOGGER.info("Progress status: {}", getProgressStatusFormattedString());


        Image image = ImageStateManager.restore(currentStep);
        currentStep.initializeView(getProgressStatusFormattedString(), image);

        Parent root = currentStep.getViewRoot();
        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.show();
    }


    public static void reset()
    {
        LOGGER.info("Resetting all steps...");
        ImageStateManager.wipe();
        start();
    }
}
