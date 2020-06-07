package io.github.stefan_ghioci.navigation.impl.reconstruction;

import io.github.stefan_ghioci.image_processing.Color;
import io.github.stefan_ghioci.image_processing.Constants;
import io.github.stefan_ghioci.image_processing.Reconstruction;
import io.github.stefan_ghioci.image_processing.SubPaletteConfig;
import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.tools.FXTools;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReconstructionController extends StepController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReconstructionController.class.getSimpleName());
    private ReconstructionView view;

    @Override
    public void setView(StepView view)
    {
        super.setView(view);
        this.view = (ReconstructionView) this.stepView;
    }

    public void handleReconstruction()
    {
        Reconstruction.Palette type = Reconstruction.Palette.valueOf(view.paletteTypeChoiceBox.getValue());
        Reconstruction.Speed speed = (Reconstruction.Speed) view.speedToggleGroup.getSelectedToggle().getUserData();
        boolean forcedBlack = view.blackBackgroundColorCheckBox.isSelected();
        Color[][] colorMatrix = FXTools.imageToColorMatrix(view.getInitialImage());

        setButtonBehaviour(true);

        Thread thread = new Thread(() ->
                                   {
                                       LOGGER.info("Starting reconstruction thread...");
                                       Reconstruction.reconstruct(colorMatrix, type, forcedBlack, speed, () ->
                                       {
                                           Platform.runLater(() -> update(Reconstruction.getLastBestResult()));
                                           return null;
                                       });
                                       LOGGER.info("Reconstruction finished");
                                       setButtonBehaviour(false);
                                   });

        view.stopReconstructionButton.setOnAction(event ->
                                                  {
                                                      LOGGER.info("Interrupting reconstruction thread...");
                                                      thread.stop();
                                                      setButtonBehaviour(false);
                                                  });

        thread.start();
    }

    private void setButtonBehaviour(boolean working)
    {
        view.stopReconstructionButton.setDisable(!working);
        view.reconstructButton.setDisable(working);
        setNavigationBarDisabled(working);
    }

    private void setViewPalette(List<List<Color>> palette)
    {
        ObservableList<Node> hBoxChildren = view.paletteVBox.getChildren()
                                                            .filtered(node -> node.getClass().equals(HBox.class));

        for (int i = 0; i < Constants.SUB_PALETTE_COUNT; i++)
        {
            HBox subPaletteHBox = (HBox) hBoxChildren.get(i);
            for (int j = 0; j < Constants.SUB_PALETTE_SIZE; j++)
            {
                Rectangle rectangle = (Rectangle) subPaletteHBox.getChildren().get(j);
                rectangle.setFill(FXTools.colorToFXColor(palette.get(i).get(j)));
            }
        }
    }

    @Override
    public void handleResetChanges()
    {
        super.handleResetChanges();

        ObservableList<Node> hBoxChildren = view.paletteVBox.getChildren()
                                                            .filtered(node -> node.getClass().equals(HBox.class));

        for (int i = 0; i < Constants.SUB_PALETTE_COUNT; i++)
        {
            HBox subPaletteHBox = (HBox) hBoxChildren.get(i);
            for (int j = 0; j < Constants.SUB_PALETTE_SIZE; j++)
            {
                Rectangle rectangle = (Rectangle) subPaletteHBox.getChildren().get(j);
                rectangle.setFill(javafx.scene.paint.Color.BLACK);
            }
        }

        Reconstruction.resetLastBestConfig();
    }

    public void update(SubPaletteConfig subPaletteConfig)
    {
        LOGGER.info("Redrawing image with new palette...");
        Color[][] colorMatrix = FXTools.imageToColorMatrix(view.getInitialImage());
        List<List<Color>> palette = subPaletteConfig.getSubPaletteList();
        view.setImage(FXTools.colorMatrixToImage(Reconstruction.redrawColorMatrix(colorMatrix, palette)));
        setViewPalette(palette);
    }
}
