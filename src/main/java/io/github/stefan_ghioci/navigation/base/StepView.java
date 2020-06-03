package io.github.stefan_ghioci.navigation.base;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public abstract class StepView
{

    protected final StepController stepController;
    protected BorderPane root;
    private ImageView imageView;
    private Image initialImage;

    public StepView(StepController stepController)
    {
        this.stepController = stepController;
    }

    public void initialize(String progressStatus, Phase phase, Image image)
    {
        initialImage = image;

        root = new BorderPane();
        root.setTop(initializeTopPane(progressStatus));
        root.setCenter(initializeCenterPane(image));
        root.setBottom(initializeBottomPane(phase));
        root.setLeft(initializeLeftPane());
        root.setRight(initializeRightPane());

    }

    protected abstract Pane initializeLeftPane();

    protected abstract Pane initializeRightPane();

    private Pane initializeCenterPane(Image image)
    {
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setSmooth(false);

        return new StackPane(imageView);
    }

    private Pane initializeTopPane(String progressStatus)
    {
        Text progressStatusText = new Text(progressStatus);
        return new TextFlow(progressStatusText);
    }

    private Pane initializeBottomPane(Phase type)
    {
        HBox buttonBox = new HBox();

        switch (type)
        {
            case INITIAL:
                buttonBox.getChildren().add(createNextButton());
                break;
            case STANDARD:
                buttonBox.getChildren().add(createBackButton());
                buttonBox.getChildren().add(createNextButton());
                buttonBox.getChildren().add(createResetButton());
                break;
            case FINAL:
                buttonBox.getChildren().add(createBackButton());
                buttonBox.getChildren().add(createResetButton());
                break;
        }

        return buttonBox;
    }


    private Button createBackButton()
    {
        Button button = new Button();

        button.setOnAction(event -> stepController.handleNavigation(ButtonType.BACK));
        button.setText("Back");

        return button;
    }

    private Button createResetButton()
    {
        Button button = new Button();

        button.setOnAction(event -> stepController.handleNavigation(ButtonType.RESET));
        button.setText("Reset");

        return button;
    }

    private Button createNextButton()
    {
        Button button = new Button();

        button.setOnAction(event -> stepController.handleNavigation(ButtonType.NEXT));
        button.setText("Next");

        return button;
    }


    public Parent getRoot()
    {
        return root;
    }

    public Image getImage()
    {
        return imageView.getImage();
    }

    public void setImage(Image image)
    {
        imageView.setImage(image);
    }

    public Image getInitialImage()
    {
        return initialImage;
    }
}
