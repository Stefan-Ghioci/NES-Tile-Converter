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
        Label speedSettingLabel = Styling.createLabel();
        speedSettingLabel.setText("Speed setting");

        speedToggleGroup = new ToggleGroup();

        RadioButton slowButton = Styling.createRadioButton();
        slowButton.setUserData(Reconstruction.Speed.Slow);
        slowButton.setText("Slow (precise)");
        slowButton.setToggleGroup(speedToggleGroup);

        RadioButton standardButton = Styling.createRadioButton();
        standardButton.setUserData(Reconstruction.Speed.Standard);
        standardButton.setText("Standard");
        standardButton.setToggleGroup(speedToggleGroup);

        RadioButton fastButton = Styling.createRadioButton();
        fastButton.setUserData(Reconstruction.Speed.Fast);
        fastButton.setText("Fast (causes artifacts)");
        fastButton.setToggleGroup(speedToggleGroup);

        speedToggleGroup.selectToggle(standardButton);

        VBox speedButtonVBox = Styling.createLeftButtonGroupVBox();
        speedButtonVBox.getChildren().setAll(slowButton, standardButton, fastButton);

        Label paletteTypeLabel = Styling.createLabel();
        paletteTypeLabel.setText("Palette choice");

        paletteTypeChoiceBox = Styling.createChoiceBox();
        paletteTypeChoiceBox.getItems().setAll(Stream.of(Reconstruction.Palette.values())
                                                     .map(Reconstruction.Palette::name)
                                                     .collect(Collectors.toList()));
        paletteTypeChoiceBox.getSelectionModel().select(0);

        blackBackgroundColorCheckBox = new CheckBox();
        blackBackgroundColorCheckBox.setText("Force black as background color");

        Label runLabel = Styling.createLabel();
        runLabel.setText("Run");

        reconstructButton = Styling.createPrimaryButton();
        reconstructButton.setText("Reconstruct");
        reconstructButton.setOnAction(event -> controller.handleReconstruction());

        stopReconstructionButton = Styling.createSuccessButton();
        stopReconstructionButton.setText("Stop");
        stopReconstructionButton.setDisable(true);

        VBox vBox = Styling.createLeftControlsVBox();
        vBox.getChildren().setAll(paletteTypeLabel,
                                  paletteTypeChoiceBox,
                                  blackBackgroundColorCheckBox,
                                  speedSettingLabel,
                                  speedButtonVBox,
                                  runLabel,
                                  reconstructButton,
                                  stopReconstructionButton);
        return vBox;
    }

    @Override
    protected Pane initializeRightPane()
    {
        paletteVBox = Styling.createRightControlsVBox();

        for (int i = 0; i < Constants.SUB_PALETTE_COUNT; i++)
        {
            HBox hBox = Styling.createColorHBox();
            for (int j = 0; j < Constants.SUB_PALETTE_SIZE; j++)
                hBox.getChildren().add(Styling.createColorRectangle());

            Label subPaletteLabel = Styling.createLabel();
            subPaletteLabel.setText("Sub Palette " + i);

            paletteVBox.getChildren().addAll(subPaletteLabel, hBox);
        }

        return paletteVBox;
    }

}
