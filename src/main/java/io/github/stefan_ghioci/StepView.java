package io.github.stefan_ghioci;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class StepView
{

    private final StepController controller;
    private BorderPane root;
    private ImageView imageView;

    public StepView(StepController controller)
    {
        this.controller = controller;
    }

    public void initialize(String progressStatus, StepType type, Image image)
    {
        root = new BorderPane();

        initializeTop(progressStatus);
        initializeCenter(image);
        initializeBottom(type);

    }

    private void initializeCenter(Image image)
    {
        imageView = new ImageView();

        imageView.setImage(image);
        imageView.setSmooth(false);

        root.setCenter(imageView);
    }

    private void initializeTop(String progressStatus)
    {
        Text progressStatusText = new Text(progressStatus);

        progressStatusText.setTextAlignment(TextAlignment.LEFT);

        root.setTop(progressStatusText);
    }

    private void initializeBottom(StepType type)
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

        buttonBox.setAlignment(Pos.CENTER);

        root.setBottom(buttonBox);
    }


    private Button createBackButton()
    {
        Button button = new Button();

        button.setOnAction(event -> controller.handleNavigation(Navigation.BACK));
        button.setText("Back");

        return button;
    }

    private Button createResetButton()
    {
        Button button = new Button();

        button.setOnAction(event -> controller.handleNavigation(Navigation.RESET));
        button.setText("Reset");

        return button;
    }

    private Button createNextButton()
    {
        Button button = new Button();

        button.setOnAction(event -> controller.handleNavigation(Navigation.NEXT));
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
}
