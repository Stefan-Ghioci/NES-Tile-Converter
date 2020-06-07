package io.github.stefan_ghioci.navigation.impl.reconstruction;

import io.github.stefan_ghioci.image_processing.Color;
import io.github.stefan_ghioci.image_processing.Constants;
import io.github.stefan_ghioci.image_processing.Reconstruction;
import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.tools.FXTools;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class ReconstructionController extends StepController
{
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

        view.reconstructButton.setDisable(true);

        Reconstruction.SubPaletteConfig solution = Reconstruction.reconstruct(colorMatrix, type, forcedBlack, speed);
        List<List<Color>> palette = solution.getSubPaletteList();
        view.setImage(FXTools.colorMatrixToImage(Reconstruction.redrawColorMatrix(colorMatrix,
                                                                                  palette)));

        setViewPalette(palette);
        view.reconstructButton.setDisable(false);
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
    }
}
