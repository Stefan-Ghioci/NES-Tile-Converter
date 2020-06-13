package io.github.stefan_ghioci.navigation.impl.preprocessing;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.processing.PreProcessing;
import io.github.stefan_ghioci.tools.FXTools;
import io.github.stefan_ghioci.tools.FileTools;
import io.github.stefan_ghioci.tools.Styling;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PreProcessingView extends StepView
{
    private final PreProcessingController controller;

    ObservableList<Color> palette;
    ListView<Color> paletteListView;
    ColorPicker colorPicker;
    Button deleteButton;
    Button addButton;
    Label paletteSizeLabel;
    Button loadNESPaletteButton;
    Button loadBestPaletteButton;
    Button loadNESGrayscalePaletteButton;
    ChoiceBox<String> ditheringChoiceBox;
    Button quantizeButton;

    public PreProcessingView(StepController controller)
    {
        super(controller);
        this.controller = (PreProcessingController) this.stepController;
    }

    @Override
    protected Pane initializeLeftPane()
    {

        Label standardPaletteLabel = Styling.createLabel();
        standardPaletteLabel.setText("Standard palettes");

        loadNESPaletteButton = Styling.createDefaultButton();
        loadNESPaletteButton.setOnAction(event -> controller.handleLoadNESPalette());
        loadNESPaletteButton.setText("Load NES Palette");

        loadBestPaletteButton = Styling.createDefaultButton();
        loadBestPaletteButton.setOnAction(event -> controller.handleLoadBestPalette());
        loadBestPaletteButton.setText("Load Best Palette");


        loadNESGrayscalePaletteButton = Styling.createDefaultButton();
        loadNESGrayscalePaletteButton.setOnAction(event -> controller.handleLoadNESGrayscalePalette());
        loadNESGrayscalePaletteButton.setText("Load Grayscale Palette");

        ditheringChoiceBox = Styling.createChoiceBox();
        ditheringChoiceBox.getItems().setAll(Stream.of(PreProcessing.Dithering.values())
                                                   .map(PreProcessing.Dithering::name)
                                                   .collect(Collectors.toList()));
        ditheringChoiceBox.getSelectionModel().select(0);

        Label ditheringLabel = Styling.createLabel();
        ditheringLabel.setText("Dithering");

        quantizeButton = Styling.createPrimaryButton();
        quantizeButton.setOnAction(event -> controller.handleQuantization());
        quantizeButton.setText("Quantize Image");
        quantizeButton.setDisable(true);

        VBox vBox = Styling.createLeftControlsVBox();

        Label runLabel = Styling.createLabel();
        runLabel.setText("Run");

        vBox.getChildren().addAll(standardPaletteLabel,
                                  loadNESPaletteButton,
                                  loadBestPaletteButton,
                                  loadNESGrayscalePaletteButton,
                                  ditheringLabel,
                                  ditheringChoiceBox,
                                  runLabel,
                                  quantizeButton);
        return vBox;
    }

    @Override
    protected Pane initializeRightPane()
    {
        Label colorControlsLabel = Styling.createLabel();
        colorControlsLabel.setText("Color selection");

        paletteSizeLabel = Styling.createLabel();
        paletteSizeLabel.setText("Palette size: 0");

        palette = FXCollections.observableArrayList();
        palette.addListener((ListChangeListener<? super Color>) event -> controller.handlePaletteChanged());

        paletteListView = Styling.createColorListView();
        paletteListView.setItems(palette);
        paletteListView.setEditable(false);
        paletteListView.setFocusTraversable(false);
        paletteListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        paletteListView.getSelectionModel()
                       .selectedItemProperty()
                       .addListener(listener -> controller.handleColorSelected());

        colorPicker = Styling.createColorPicker();
        colorPicker.setOnAction(event -> controller.handleAddButtonStatus());
        colorPicker.getCustomColors().addAll(FXTools.colorListToFXColorList(FileTools.loadNESPalette()));

        addButton = Styling.createPrimaryButton();
        addButton.setText("Add");
        addButton.setOnAction(event -> controller.handleAddPickedColor());

        deleteButton = Styling.createDefaultButton();
        deleteButton.setText("Delete");
        deleteButton.setOnAction(event -> controller.handleDeleteSelectedColor());
        deleteButton.setDisable(true);


        VBox vBox = Styling.createRightControlsVBox();
        vBox.getChildren().setAll(colorControlsLabel,
                                  colorPicker,
                                  addButton,
                                  deleteButton,
                                  paletteSizeLabel,
                                  paletteListView);
        return vBox;
    }

}
