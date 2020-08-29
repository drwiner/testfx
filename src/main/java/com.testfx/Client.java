package com.testfx;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Client extends Application{

    private Stage stage;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int LEFT_SIDE_WIDTH = 200;
    private static final int RIGHT_SIDE_WIDTH = 200;
    private static final int TOP_SIDE_HEIGHT = 100;
    private static final int BOTTOM_SIDE_HEIGHT = 100;

    private Parent createRoot() {
        BorderPane root = new BorderPane();

        VBox vBoxLeft = new VBox();
        vBoxLeft.setPrefWidth(LEFT_SIDE_WIDTH);
        Rectangle rectangle = new Rectangle(0, 0, LEFT_SIDE_WIDTH, HEIGHT  );
        rectangle.setFill(Color.GREY);
        vBoxLeft.getChildren().add(rectangle);
        VBox vBoxRight = new VBox();
        vBoxRight.setPrefWidth(RIGHT_SIDE_WIDTH);

        HBox.setHgrow(vBoxRight, Priority.NEVER);
//        v

        Rectangle rectangle1 = new Rectangle(0, 0, LEFT_SIDE_WIDTH, HEIGHT  );
        vBoxRight.getChildren().add(rectangle1);
        rectangle1.setFill(Color.GREY);
        vBoxRight.setMaxWidth(RIGHT_SIDE_WIDTH);

        Rectangle topRect = new Rectangle(0, 0, LEFT_SIDE_WIDTH + WIDTH + RIGHT_SIDE_WIDTH, TOP_SIDE_HEIGHT);
        Rectangle botRect = new Rectangle(0, 0, LEFT_SIDE_WIDTH + WIDTH + RIGHT_SIDE_WIDTH, TOP_SIDE_HEIGHT);
        topRect.setFill(Color.DARKRED);
        botRect.setFill(Color.DARKRED);

        HBox hBoxTop = new HBox(topRect);
        hBoxTop.setPrefHeight(TOP_SIDE_HEIGHT);
        HBox hBoxBottom = new HBox(botRect);
        hBoxBottom.setPrefHeight(BOTTOM_SIDE_HEIGHT);

        root.setLeft(vBoxLeft);
        root.setRight(vBoxRight);
        root.setTop(hBoxTop);
        root.setBottom(hBoxBottom);
//        vBoxLeft.toFront();

        ReadOnlyDoubleProperty [] spatialContext = new ReadOnlyDoubleProperty [4];
        spatialContext[0] = vBoxLeft.widthProperty();
        spatialContext[1] = hBoxTop.heightProperty();
        spatialContext[2] = vBoxRight.widthProperty();
        spatialContext[3] = hBoxBottom.heightProperty();

        GraphSpace graphSpace = new GraphSpace(spatialContext, WIDTH, HEIGHT);
        graphSpace.getHost().hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            System.out.println("Host: " + t1);
        });
        graphSpace.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            System.out.println("GraphSpace: " + t1);
        });
        root.setCenter(graphSpace.getHost());
        HBox.setHgrow(graphSpace.getHost(), Priority.NEVER);
//        graphSpace.getHost().set



        DragableBox dragableBox = new DragableBox(graphSpace.getHost(), 100, 100, 50, 50);
        graphSpace.addBox(dragableBox);
//        graphSpace.registerBoxToSizeListener(this.stage, dragableBox);

        DragableBox dragableBox1 = new DragableBox(graphSpace.getHost(), 400, 100, 50, 50);
        dragableBox1.setFill(Color.AZURE);
        dragableBox1.setStrokeWidth(2);
        dragableBox1.setStroke(Color.BLACK);
        graphSpace.addBox(dragableBox1);
//        graphSpace.registerBoxToSizeListener(this.stage, dragableBox1);

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
