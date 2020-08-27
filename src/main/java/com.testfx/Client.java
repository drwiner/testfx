package com.testfx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends Application{

    private Stage stage;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private Parent createRoot() {
        Pane root = new Pane();
        GraphPane gridRoot = new GraphPane(WIDTH, HEIGHT);
        gridRoot.addSizeListeners(root);
        root.getChildren().add(gridRoot);

        DragableBox dragableBox = new DragableBox(100, 100, 50, 50);
        root.getChildren().add(dragableBox);
        gridRoot.registerBoxToSizeListener(this.stage, dragableBox);

        DragableBox dragableBox1 = new DragableBox(400, 100, 50, 50);
        dragableBox1.setFill(Color.AZURE);
        dragableBox1.setStrokeWidth(2);
        dragableBox1.setStroke(Color.BLACK);
        root.getChildren().add(dragableBox1);
        gridRoot.registerBoxToSizeListener(this.stage, dragableBox1);

        return root;
    }


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        Scene scene = new Scene(createRoot());

        this.stage.setScene(scene);
        this.stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
