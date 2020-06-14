package io.github.stefan_ghioci.tools;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Styling
{
    public static void initializeStage(Stage stage)
    {
        Image icon = new Image(Styling.class.getResourceAsStream("icon_placeholder.png"));
        String title = "NES Tile Converter";

        stage.getIcons().add(icon);
        stage.setTitle(title);
        stage.setResizable(false);
    }

    public static Scene createScene(Parent root)
    {
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        scene.getStylesheets().add("io/github/stefan_ghioci/tools/opacityondisable.css");
        return scene;
    }

    public static Button createPrimaryButton()
    {
        return createButton("btn-primary");
    }

    public static Button createDefaultButton()
    {
        return createButton("btn-default");
    }

    public static Button createWarningButton()
    {
        return createButton("btn-warning");
    }

    public static Button createSuccessButton()
    {
        return createButton("btn-success");
    }

    public static Button createErrorButton()
    {
        return createButton("btn-danger");
    }

    public static Button createButton(String buttonClass)
    {
        Button button = new Button();
        button.getStyleClass().setAll("btn", "btn-sm", buttonClass);
        return button;
    }

    public static ProgressBar createProgressBar()
    {
        ProgressBar progressBar = new ProgressBar();
        progressBar.getStyleClass().setAll("progress-bar", "progress-bar-info", "panel-info");
        return progressBar;
    }

    public static HBox createNavigationButtonBox()
    {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }


    public static VBox createProgressLabel(String status)
    {
        Label statusLabel = new Label();
        statusLabel.setText(status);
        statusLabel.getStyleClass().setAll("lbl", "lbl-success", "h4");

        VBox vBox = new VBox(statusLabel);
        vBox.setPadding(new Insets(20));

        return vBox;
    }


    public static void setBorderPaneSize(BorderPane borderPane)
    {
        Pane left = ((Pane) borderPane.getLeft());
        Pane right = ((Pane) borderPane.getRight());
        Pane center = ((Pane) borderPane.getCenter());
        Pane top = ((Pane) borderPane.getTop());
        Pane bottom = ((Pane) borderPane.getBottom());

        setSize(left, 250, 400);
        setSize(right, 250, 400);
        setSize(center, 300, 400);

        setSize(top, 600, 50);
        setSize(bottom, 600, 100);
    }

    private static void setSize(Region region, int width, int height)
    {
        region.setPrefSize(width, height);
        region.setMinSize(width, height);
//        region.setMaxSize(width, height);
    }

    public static Pane createImagePane(ImageView imageView)
    {
        ScrollPane scrollPane = new ScrollPane();
        StackPane imageHolder = new StackPane(imageView);
        scrollPane.setContent(imageHolder);
        scrollPane.setPrefSize(300, 300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().setAll("panel", "panel-info");

        imageHolder.minWidthProperty()
                   .bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getWidth() - 1,
                                                      scrollPane.viewportBoundsProperty()));
        imageHolder.minHeightProperty()
                   .bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getHeight() - 1,
                                                      scrollPane.viewportBoundsProperty()));


        VBox vBox = new VBox(new StackPane(scrollPane));
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public static Alert createAlert(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().getScene().getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        ((Stage) alert.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UNDECORATED);
        return alert;
    }

    public static VBox createLeftControlsVBox()
    {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(20));
        return vBox;
    }

    public static VBox createRightControlsVBox()
    {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.TOP_RIGHT);
        vBox.setPadding(new Insets(20));
        return vBox;
    }


    public static ChoiceBox<String> createChoiceBox()
    {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getStyleClass().setAll("btn", "btn-sm", "btn-default");
        return choiceBox;
    }

    public static Label createLabel()
    {
        Label label = new Label();
        label.getStyleClass().setAll("lbl", "lbl-info");
        return label;
    }

    public static ColorPicker createColorPicker()
    {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.getStyleClass().setAll("btn", "btn-sm", "btn-default");
        return colorPicker;
    }

    public static ListView<Color> createColorListView()
    {
        ListView<Color> listView = new ListView<>();
        listView.getStyleClass().setAll("panel", "panel-info");
        listView.setCellFactory(callback -> new ListCell<>()
        {
            @Override
            protected void updateItem(javafx.scene.paint.Color color, boolean empty)
            {
                super.updateItem(color, empty);
                if (!empty)
                {
                    Rectangle rectangle = new Rectangle(60, 20);
                    rectangle.setFill(color);

                    setGraphic(rectangle);
                    setAlignment(Pos.CENTER);
                }
                else
                {
                    setGraphic(null);
                }
                setText(null);
            }
        });
        setSize(listView, 100, 150);
        listView.setMaxWidth(100);
        return listView;
    }

    public static VBox createLeftButtonGroupVBox()
    {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        return vBox;
    }

    public static RadioButton createRadioButton()
    {
        RadioButton radioButton = new RadioButton();
        radioButton.getStyleClass().setAll("btn", "btn-xs", "btn-default");
        return radioButton;
    }


    public static HBox createColorHBox()
    {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        return hBox;
    }

    public static Rectangle createColorRectangle()
    {
        Rectangle rectangle = new Rectangle(40, 25);
        rectangle.setFill(Color.BLACK);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStroke(Color.GRAY);
        rectangle.setStrokeWidth(1);
        return rectangle;
    }

    public static Slider createValueSlider()
    {
        Slider slider = new Slider();
        slider.setMaxWidth(150);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        return slider;
    }

}
