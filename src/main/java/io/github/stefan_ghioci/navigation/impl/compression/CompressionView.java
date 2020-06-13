package io.github.stefan_ghioci.navigation.impl.compression;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.processing.Compression;
import io.github.stefan_ghioci.processing.Constants;
import io.github.stefan_ghioci.tools.ColorTools;
import io.github.stefan_ghioci.tools.Styling;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CompressionView extends StepView
{
    private final CompressionController controller;

    Slider tileSlider;
    Button compressButton;
    Button interruptCompressionButton;
    ProgressBar compressionProgressBar;
    ToggleGroup compressionTypeToggleGroup;
    Label tileSliderLabel;
    Label compressionCompletionLabel;

    public CompressionView(StepController controller)
    {
        super(controller);
        this.controller = (CompressionController) this.stepController;
    }

    @Override
    protected Pane initializeLeftPane()
    {
        int width = (int) getImage().getWidth();
        int height = (int) getImage().getHeight();

        int tileCount = ColorTools.getTileCount(width, height);
        int maxTileCount = Constants.MAX_TILE_COUNT;

        tileSliderLabel = Styling.createLabel();
        tileSliderLabel.setText("Number of tiles: " + maxTileCount);

        tileSlider = Styling.createValueSlider();
        tileSlider.setMax(Math.min(tileCount, maxTileCount));
        tileSlider.setMin(8);
        tileSlider.setValue(maxTileCount);
        tileSlider.setMajorTickUnit((maxTileCount - 8) / 4);
        tileSlider.valueProperty()
                  .addListener(listener -> controller.handleTileCountChanged());
        tileSlider.valueProperty()
                  .addListener((observableValue, oldValue, newValue) -> tileSlider.setValue(Math.round(newValue.doubleValue())));

        Label compressionTypeLabel = Styling.createLabel();
        compressionTypeLabel.setText("Compression Type");

        compressionTypeToggleGroup = new ToggleGroup();

        RadioButton modeCompressionButton = Styling.createRadioButton();
        modeCompressionButton.setUserData(Compression.Type.Mode);
        modeCompressionButton.setText("Blend (Mode)");
        modeCompressionButton.setToggleGroup(compressionTypeToggleGroup);

        RadioButton replaceCompressionButton = Styling.createRadioButton();
        replaceCompressionButton.setUserData(Compression.Type.Replace);
        replaceCompressionButton.setText("Replace");
        replaceCompressionButton.setToggleGroup(compressionTypeToggleGroup);


        compressionTypeToggleGroup.selectToggle(modeCompressionButton);

        VBox compressionTypeVBox = Styling.createLeftButtonGroupVBox();
        compressionTypeVBox.getChildren().setAll(modeCompressionButton, replaceCompressionButton);

        VBox vBox = Styling.createLeftControlsVBox();
        vBox.getChildren().setAll(compressionTypeLabel,
                                  compressionTypeVBox,
                                  tileSliderLabel,
                                  tileSlider);

        return vBox;
    }

    @Override
    protected Pane initializeRightPane()
    {
        Label runLabel = Styling.createLabel();
        runLabel.setText("Run");

        compressButton = Styling.createPrimaryButton();
        compressButton.setText("Compress");
        compressButton.setOnAction(event -> controller.handleCompression());

        interruptCompressionButton = Styling.createErrorButton();
        interruptCompressionButton.setText("Interrupt");
        interruptCompressionButton.setDisable(true);

        compressionCompletionLabel = Styling.createLabel();
        compressionCompletionLabel.setText("Completion: 0%");

        compressionProgressBar = Styling.createProgressBar();
        compressionProgressBar.setProgress(0);
        compressionProgressBar.setDisable(true);


        VBox vBox = Styling.createRightControlsVBox();
        vBox.getChildren().setAll(
                runLabel,
                compressButton,
                interruptCompressionButton,
                compressionCompletionLabel,
                compressionProgressBar);
        return vBox;
    }
}
