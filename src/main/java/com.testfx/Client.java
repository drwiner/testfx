package com.testfx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends Application{

    private Stage stage;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int LEFT_SIDE_WIDTH = 200;
    private static final int RIGHT_SIDE_WIDTH = 200;

    private Parent createRoot() {
        BorderPane root = new BorderPane();
        GraphPane gridRoot = new GraphPane(RIGHT_SIDE_WIDTH + RIGHT_SIDE_WIDTH, WIDTH, HEIGHT);

        VBox vBoxLeft = new VBox();
        vBoxLeft.setPrefWidth(LEFT_SIDE_WIDTH);
        VBox vBoxRight = new VBox();
        vBoxRight.setPrefWidth(RIGHT_SIDE_WIDTH);
        root.setLeft(vBoxLeft);
        root.setRight(vBoxRight);


        Pane pane = new Pane();
        pane.getChildren().add(gridRoot);
        root.setCenter(pane);

        gridRoot.addSizeListeners(pane);

        DragableBox dragableBox = new DragableBox(pane, 100, 100, 50, 50);
        pane.getChildren().add(dragableBox);
        gridRoot.registerBoxToSizeListener(this.stage, dragableBox);

        DragableBox dragableBox1 = new DragableBox(pane, 400, 100, 50, 50);
        dragableBox1.setFill(Color.AZURE);
        dragableBox1.setStrokeWidth(2);
        dragableBox1.setStroke(Color.BLACK);
        pane.getChildren().add(dragableBox1);
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
