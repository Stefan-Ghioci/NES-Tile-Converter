package io.github.stefan_ghioci.navigation.impl.reconstruction;

import io.github.stefan_ghioci.navigation.base.Phase;
import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.processing.Constants;
import io.github.stefan_ghioci.processing.Reconstruction;
import io.github.stefan_ghioci.tools.Styling;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReconstructionView extends StepView
{
    private final ReconstructionController controller;

    VBox paletteVBox;
    CheckBox blackBackgroundColorCheckBox;
    Button reconstructButton;
    Button stopReconstructionButton;
    ToggleGroup speedToggleGroup;
    ChoiceBox<String> paletteTypeChoiceBox;

    public ReconstructionView(StepController controller)
    {
        super(controller);
        this.controller = (ReconstructionController) this.stepController;
    }

    @Override
    public void initialize(String progressStatus, Phase phase, Image image)
    {
        super.initialize(progressStatus, phase, image);
        Reconstruction.resetLastResult();
    }

    @Override
    protected Pane initializeLeftPane()
    {
        speedToggleGroup = new ToggleGroup();

        RadioButton slowButton = new RadioButton();
        slowButton.setUserData(Reconstruction.Speed.Slow);
        slowButton.setText("Slow (precise)");
        slowButton.setToggleGroup(speedToggleGroup);

        RadioButton standardButton = new RadioButton();
        standardButton.setUserData(Reconstruction.Speed.Standard);
        standardButton.setText("Standard");
        standardButton.setToggleGroup(speedToggleGroup);

        RadioButton fastButton = new RadioButton();
        fastButton.setUserData(Reconstruction.Speed.Fast);
        fastButton.setText("Fast (causes artifacts)");
        fastButton.setToggleGroup(speedToggleGroup);

        speedToggleGroup.selectToggle(standardButton);

        VBox speedButtonVBox = new VBox(slowButton, standardButton, fastButton);

        paletteTypeChoiceBox = new ChoiceBox<>();
        paletteTypeChoiceBox.getItems().setAll(Stream.of(Reconstruction.Palette.values())
                                                     .map(Reconstruction.Palette::name)
                                                     .collect(Collectors.toList()));
        paletteTypeChoiceBox.getSelectionModel().select(0);

        blackBackgroundColorCheckBox = new CheckBox();
        blackBackgroundColorCheckBox.setText("Force black as background color");

        reconstructButton = Styling.createPrimaryButton();
        reconstructButton.setText("Reconstruct");
        reconstructButton.setOnAction(event -> controller.handleReconstruction());

        stopReconstructionButton = Styling.createSuccessButton();
        stopReconstructionButton.setText("Stop");
        stopReconstructionButton.setDisable(true);

        return new VBox(paletteTypeChoiceBox,
                        speedButtonVBox,
                        blackBackgroundColorCheckBox,
                        reconstructButton,
                        stopReconstructionButton);
    }

    @Override
    protected Pane initializeRightPane()
    {
        paletteVBox = new VBox();

        for (int i = 0; i < Constants.SUB_PALETTE_COUNT; i++)
        {
            HBox subPaletteHBox = new HBox();
            for (int j = 0; j < Constants.SUB_PALETTE_SIZE; j++)
            {
                Rectangle rectangle = new Rectangle(20, 20);

                subPaletteHBox.getChildren().add(rectangle);
            }
            subPaletteHBox.setSpacing(5);

            Label subPaletteLabel = new Label();
            subPaletteLabel.setText("Sub Palette " + i);
            subPaletteLabel.setLabelFor(subPaletteHBox);

            paletteVBox.getChildren().addAll(subPaletteLabel, subPaletteHBox);
        }

        return paletteVBox;
    }
}
