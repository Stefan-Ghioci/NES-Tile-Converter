package io.github.stefan_ghioci.navigation.impl.compression;

import io.github.stefan_ghioci.navigation.base.StepController;
import io.github.stefan_ghioci.navigation.base.StepView;
import io.github.stefan_ghioci.processing.Color;
import io.github.stefan_ghioci.processing.Compression;
import io.github.stefan_ghioci.processing.Reconstruction;
import io.github.stefan_ghioci.tools.ColorTools;
import io.github.stefan_ghioci.tools.FXTools;
import io.github.stefan_ghioci.tools.FileTools;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompressionController extends StepController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CompressionController.class.getSimpleName());
    private CompressionView view;

    @Override
    public void setView(StepView view)
    {
        super.setView(view);
        this.view = (CompressionView) this.stepView;
    }

    public void handleCompression()
    {
        int desiredTileCount = (int) view.tileSlider.getValue();
        Compression.Type type = (Compression.Type) view.compressionTypeToggleGroup.getSelectedToggle()
                                                                                  .getUserData();

        Color[][] colorMatrix = FXTools.imageToColorMatrix(view.getInitialImage());

        List<List<Color>> subPaletteList = Reconstruction.getLastResult();

        if (subPaletteList == null)
        {
            FXTools.showAlert("Compression Error", FileTools.loadText("skipped_reconstruction"), Alert.AlertType.ERROR);
            return;
        }

        setButtonBehaviour(true);

        Thread thread = new Thread(() ->
                                   {
                                       LOGGER.info("Starting compression thread...");
                                       Compression.compress(colorMatrix,
                                                            subPaletteList,
                                                            desiredTileCount,
                                                            type,
                                                            () ->
                                                            {
                                                                Platform.runLater(() -> update(desiredTileCount));
                                                                return null;
                                                            });
                                       LOGGER.info("Compression finished");
                                       setCompressedImage();
                                       setButtonBehaviour(false);
                                   });

        view.interruptCompressionButton.setOnAction(event ->
                                                    {
                                                        LOGGER.info("Interrupting compression thread...");
                                                        thread.stop();
                                                        setButtonBehaviour(false);
                                                        resetProgress();
                                                    });
        thread.start();
    }

    private void resetProgress()
    {
        view.compressionProgressBar.setProgress(0);
        view.compressionCompletionLabel.setText("Completion: 0%");
    }

    private void setCompressedImage()
    {
        view.setImage(FXTools.colorMatrixToImage(ColorTools.tilesToColorMatrix(Compression.getLastResult(),
                                                                               (int) view.getImage().getWidth(),
                                                                               (int) view.getImage().getHeight())));
    }

    private void update(int desiredTileCount)
    {
        int width = (int) view.getImage().getWidth();
        int height = (int) view.getImage().getHeight();

        int initialTileCount = ColorTools.getTileCount(width, height);
        int currentTileCount = Compression.getCurrentTileCount();

        double progress = 1.0 * (initialTileCount - currentTileCount) / (initialTileCount - desiredTileCount);
        view.compressionProgressBar.setProgress(progress);
        view.compressionCompletionLabel.setText("Completion: " + (int) (progress * 100) + "%");
    }

    private void setButtonBehaviour(boolean working)
    {
        view.compressionProgressBar.setDisable(!working);

        view.compressButton.setDisable(working);

        view.interruptCompressionButton.setDisable(!working);
        setNavigationBarDisabled(working);
    }

    @Override
    public void handleResetChanges()
    {
        super.handleResetChanges();
        Compression.resetLastResult();
    }

    public void handleTileCountChanged()
    {
        view.tileSliderLabel.setText("Number of tiles: " + (int) view.tileSlider.getValue());
    }
}
