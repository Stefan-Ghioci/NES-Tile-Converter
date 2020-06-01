package io.github.stefan_ghioci.window;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class View
{
    private BorderPane root;

    public View(Controller controller)
    {
        initializeRoot();
    }

    private void initializeRoot()
    {
        this.root = new BorderPane();

    }

    public Parent getRoot()
    {
        return this.root;
    }
}
