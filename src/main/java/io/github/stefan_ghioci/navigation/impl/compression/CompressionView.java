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

import static io.github.stefan_ghioci.tools.Miscellaneous.gcd;

public class CompressionView extends StepView
{
    private final CompressionController controller;

    Slider tileSlider;
    Button compressButton;
    Button interruptCompressionButton;
    ProgressBar compressionProgressBar;
    ToggleGroup compressionTypeToggleGroup;

    public CompressionView(StepController controller)
    {
        super(controller);
        this.controller = (CompressionController) this.stepController;
    }

    @Override
    protected Pane initializeLeftPane()
    {
        //todo change label type
        Label tileSliderLabel = new Label();
        tileSliderLabel.setText("Desired number of tiles");

        int width = (int) getImage().getWidth();
        int height = (int) getImage().getHeight();

        int tileCount = ColorTools.getTileCount(width, height);
        int tileSetSize = Constants.MAX_TILE_COUNT;
        int unitSize = gcd(tileCount, tileSetSize);

        tileSlider = new Slider();
        tileSlider.setShowTickLabels(true);
        tileSlider.setShowTickMarks(true);
        tileSlider.setBlockIncrement(unitSize);
        tileSlider.setMax(tileCount);
        tileSlider.setMin(0);
        tileSlider.setSnapToTicks(true);
        tileSlider.setMajorTickUnit(Math.min(tileCount, tileSetSize));
        tileSlider.setMinorTickCount(unitSize);
        tileSlider.setValue(tileSetSize);

        VBox vBox = new VBox();
        vBox.getChildren().setAll(tileSliderLabel, tileSlider);

        return vBox;
    }

    @Override
    protected Pane initializeRightPane()
    {

        Label compressionTypeLabel = new Label();
        compressionTypeLabel.setText("Compression Type");

        compressionTypeToggleGroup = new ToggleGroup();

        RadioButton modeCompressionButton = new RadioButton();
        modeCompressionButton.setUserData(Compression.Type.Mode);
        modeCompressionButton.setText("Blend (Mode)");
        modeCompressionButton.setToggleGroup(compressionTypeToggleGroup);

        RadioButton replaceCompressionButton = new RadioButton();
        replaceCompressionButton.setUserData(Compression.Type.Replace);
        replaceCompressionButton.setText("Replace");
        replaceCompressionButton.setToggleGroup(compressionTypeToggleGroup);


        compressionTypeToggleGroup.selectToggle(modeCompressionButton);

        VBox compressionTypeVBox = new VBox(modeCompressionButton, replaceCompressionButton);

        compressButton = new Button();
        compressButton.setText("Compress");
        compressButton.setOnAction(event -> controller.handleCompression());

        compressionProgressBar = Styling.createProgressBar();
        compressionProgressBar.setProgress(0);
        compressionProgressBar.setDisable(true);

        interruptCompressionButton = Styling.createErrorButton();
        interruptCompressionButton.setText("Interrupt");
        interruptCompressionButton.setDisable(true);

        VBox vBox = Styling.createLeftControlsVBox();
        vBox.getChildren().setAll(compressionTypeLabel,
                                  compressionTypeVBox,
                                  compressButton,
                                  compressionProgressBar,
                                  interruptCompressionButton);
        return vBox;
    }
}
