package com.testfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends Application{

    private Stage stage;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int LEFT_SIDE_WIDTH = 200;
    private static final int RIGHT_SIDE_WIDTH = 200;

    private ScrollPane scrollPane;

    private Parent createRoot() {
        BorderPane root = new BorderPane();
        GraphPane gridRoot = new GraphPane(WIDTH, HEIGHT);

        VBox vBoxLeft = new VBox();
        vBoxLeft.setPrefWidth(LEFT_SIDE_WIDTH);
        VBox vBoxRight = new VBox();
        vBoxRight.setPrefWidth(RIGHT_SIDE_WIDTH);
        root.setLeft(vBoxLeft);
        root.setRight(vBoxRight);

        scrollPane = new ScrollPane(gridRoot);
        scrollPane.setPannable(false);

        root.setCenter(scrollPane);

        DragableBox dragableBox = new DragableBox(scrollPane, gridRoot, 100, 100, 50, 50);
        gridRoot.addBox(dragableBox);

        DragableBox dragableBox1 = new DragableBox(scrollPane, gridRoot, 400, 100, 50, 50);
        dragableBox1.setFill(Color.AZURE);
        dragableBox1.setStrokeWidth(2);
        dragableBox1.setStroke(Color.BLACK);
        gridRoot.addBox(dragableBox1);


        StackPane stackPane = new StackPane();
        gridRoot.getChildren().add(stackPane);
        stackPane.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        stackPane.setTranslateX(100);
        stackPane.setTranslateY(300);

        LateXCanvas lateXCanvas = new LateXCanvas("\\langle x, y \\rangle ", 20);
        stackPane.getChildren().add(lateXCanvas);
        stackPane.setPrefWidth(200);
        stackPane.setPrefHeight(200);
        lateXCanvas.widthProperty().bind( stackPane.widthProperty());
        lateXCanvas.heightProperty().bind( stackPane.heightProperty());

        return root;
    }


    @Override
    public void start(Stage stage) {
        javafx.scene.text.Font.loadFont(Client.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/base/jlm_cmmi10.ttf"), 1);
        javafx.scene.text.Font.loadFont(Client.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/maths/jlm_cmsy10.ttf"), 1);
        javafx.scene.text.Font.loadFont(Client.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/latin/jlm_cmr10.ttf"), 1);
        this.stage = stage;
        Scene scene = new Scene(createRoot());

        this.stage.setScene(scene);
        this.stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
