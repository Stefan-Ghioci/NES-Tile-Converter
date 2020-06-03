package io.github.stefan_ghioci.navigation.impl.preprocessing;

import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.tools.FXTools;
import io.github.stefan_ghioci.tools.FileTools;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class PreProcessingView extends StepView
{
    private final PreProcessingController controller;

    ObservableList<Color> palette;
    ListView<Color> paletteListView;
    ColorPicker colorPicker;
    Button deleteButton;
    Button addButton;
    Text paletteSizeText;
    Button loadNESPaletteButton;
    Button loadBestPaletteButton;
    ChoiceBox<String> ditheringChoiceBox;
    Button quantizeButton;

    public PreProcessingView(PreProcessingController controller)
    {
        super(controller);
        this.controller = (PreProcessingController) this.stepController;
    }

    @Override
    protected Pane initializeLeftPane()
    {
        loadNESPaletteButton = new Button();
        loadNESPaletteButton.setOnAction(event -> controller.handleLoadNESPalette());
        loadNESPaletteButton.setText("Load NES Palette");

        loadBestPaletteButton = new Button();
        loadBestPaletteButton.setOnAction(event -> controller.handleLoadBestPalette());
        loadBestPaletteButton.setText("Load Best Palette");

        ditheringChoiceBox = new ChoiceBox<>();
        ditheringChoiceBox.getItems().setAll(controller.getDitheringMethods());
        ditheringChoiceBox.getSelectionModel().select(0);

        quantizeButton = new Button();
        quantizeButton.setOnAction(event -> controller.handleQuantization());
        quantizeButton.setText("Quantize Image");
        quantizeButton.setDisable(true);

        return new VBox(loadNESPaletteButton, loadBestPaletteButton, ditheringChoiceBox, quantizeButton);
    }

    @Override
    protected Pane initializeRightPane()
    {
        paletteSizeText = new Text("Palette size: 0");

        palette = FXCollections.observableArrayList();
        palette.addListener((ListChangeListener<? super Color>) event -> controller.handlePaletteChanged());

        paletteListView = new ListView<>();
        paletteListView.setItems(palette);
        paletteListView.setEditable(false);
        paletteListView.setFocusTraversable(false);
        paletteListView.setCellFactory(callback -> FXTools.createColorListCell());
        paletteListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        paletteListView.getSelectionModel()
                       .selectedItemProperty()
                       .addListener(listener -> controller.handleColorSelected());

        colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> controller.handleAddButtonStatus());
        colorPicker.getCustomColors().addAll(FXTools.colorListToFXColorList(FileTools.loadNESPalette()));

        addButton = new Button("Add");
        addButton.setOnAction(event -> controller.handleAddPickedColor());

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> controller.handleDeleteSelectedColor());
        deleteButton.setDisable(true);

        HBox topBox = new HBox(colorPicker, addButton, deleteButton);

        return new VBox(topBox, paletteListView, paletteSizeText);
    }
}
