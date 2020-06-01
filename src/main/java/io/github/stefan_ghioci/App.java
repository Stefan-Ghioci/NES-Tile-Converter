package io.github.stefan_ghioci;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class App extends Application
{

    @Override
    public void start(Stage stage)
    {
        initializeStage(stage);

        SceneManager.setStage(stage);
        SceneManager.setSteps(initializeSteps());
        SceneManager.start();
    }

    private List<Step> initializeSteps()
    {

        StepController controller1 = new StepController();
        StepView view1 = new StepView(controller1);
        controller1.setView(view1);
        
        StepController controller2 = new StepController();
        StepView view2 = new StepView(controller1);
        controller1.setView(view2);

        StepController controller3 = new StepController();
        StepView view3 = new StepView(controller3);
        controller3.setView(view3);
        
        StepController controller4 = new StepController();
        StepView view4 = new StepView(controller4);
        controller4.setView(view4);

        StepController controller5 = new StepController();
        StepView view5 = new StepView(controller5);
        controller5.setView(view5);
        
        Step step1 = new Step("Load Image", StepType.INITIAL, view1, controller1);
        Step step2 = new Step("Pre-Processing", StepType.STANDARD, view2, controller2);
        Step step3 = new Step("Reconstruct", StepType.STANDARD, view3, controller3);
        Step step4 = new Step("Compress", StepType.STANDARD, view4, controller4);
        Step step5 = new Step("Final Result", StepType.FINAL, view5, controller5);

        return new ArrayList<>(Arrays.asList(step1, step2, step3, step4, step5));
    }

    private void initializeStage(Stage stage)
    {
        Image icon = new Image(App.class.getResourceAsStream("assets/placeholder.png"));
        String title = "NES Tile Converter";

        stage.getIcons().add(icon);
        stage.setTitle(title);
    }

    public static void main(String[] args)
    {
        launch();
    }

}