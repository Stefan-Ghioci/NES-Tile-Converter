package io.github.stefan_ghioci.navigation.base;

import io.github.stefan_ghioci.tools.Styling;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StepView
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StepView.class.getSimpleName());

    protected final StepController stepController;
    protected BorderPane root;
    HBox navigationButtonBox;
    private ImageView imageView;
    private Image initialImage;

    public StepView(StepController stepController)
    {
        this.stepController = stepController;
    }

    public void initialize(String progressStatus, Phase phase, Image image)
    {
        LOGGER.info("Initializing view...");
        initialImage = image;

        root = new BorderPane();
        root.setTop(initializeTopPane(progressStatus));
        root.setCenter(initializeCenterPane(image));
        root.setBottom(initializeBottomPane(phase));
        root.setLeft(initializeLeftPane());
        root.setRight(initializeRightPane());

        Styling.setBorderPaneSize(root);
    }

    protected abstract Pane initializeLeftPane();

    protected abstract Pane initializeRightPane();

    private Pane initializeCenterPane(Image image)
    {
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setSmooth(false);

        return Styling.createImagePane(imageView);
    }

    private Pane initializeTopPane(String progressStatus)
    {
        return Styling.createProgressLabel(progressStatus);
    }

    private Pane initializeBottomPane(Phase type)
    {
        navigationButtonBox = Styling.createNavigationButtonBox();

        switch (type)
        {
            case INITIAL:
                navigationButtonBox.getChildren().add(createNextButton());
                break;
            case STANDARD:
                navigationButtonBox.getChildren().add(createBackButton());
                navigationButtonBox.getChildren().add(createNextButton());
                navigationButtonBox.getChildren().add(createResetButton());
                break;
            case FINAL:
                navigationButtonBox.getChildren().add(createBackButton());
                break;
        }

        return navigationButtonBox;
    }


    private Button createBackButton()
    {
        Button button = Styling.createDefaultButton();

        button.setOnAction(event -> stepController.handleNavigation(ButtonType.BACK));
        button.setText("Back");

        return button;
    }

    private Button createResetButton()
    {
        Button button = Styling.createWarningButton();

        button.setOnAction(event -> stepController.handleResetChanges());
        button.setText("Reset");

        return button;
    }

    private Button createNextButton()
    {
        Button button = Styling.createPrimaryButton();

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
