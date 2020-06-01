package io.github.stefan_ghioci;

import io.github.stefan_ghioci.window.Controller;
import io.github.stefan_ghioci.window.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class App extends Application
{

    @Override
    public void start(Stage stage)
    {
        Controller controller = new Controller();
        View view = new View(controller);
        controller.setView(view);

        Image icon = new Image(App.class.getResourceAsStream("assets/placeholder.png"));
        Scene scene = new Scene(view.getRoot());
        String title = "NES Tile Converter";

        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

}