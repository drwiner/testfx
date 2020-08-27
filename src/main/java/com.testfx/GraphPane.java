package com.testfx;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GraphPane extends Pane {

    private double currentHeight;
    private double currentWidth;


    public GraphPane(double width, double height) {
        setPrefWidth(width);
        setPrefHeight(height);

        drawLines( 0, 0, width, height);

        currentHeight = height;
        currentWidth = width;

    }

    public void addSizeListeners(Pane parent){
        parent.widthProperty().addListener(((observableValue, number, t1) -> {
            drawLines( 0, 0, t1.doubleValue(), currentHeight);
            currentWidth = t1.doubleValue();
        }));

        parent.heightProperty().addListener(((observableValue, number, t1) -> {
            drawLines( 0, 0, currentWidth, t1.doubleValue());
            currentHeight = t1.doubleValue();
        }));

    }

    public void registerBoxToSizeListener(Stage parent, DragableBox box){

        parent.widthProperty().lessThan(box.translateXProperty().add(box.getX() + box.getWidth()))
                .addListener((observableValue, aBoolean, t1) -> {
                if (t1 && box.isDragging())
                    parent.setWidth(box.translateXProperty().get() + box.getWidth() + box.getX());
        });

        parent.heightProperty().lessThan(box.translateYProperty().add(box.getHeight() + box.getY()))
                .addListener((observableValue, aBoolean, t1) -> {
                if (t1 && box.isDragging())
                    parent.setHeight(box.translateYProperty().get() + box.getHeight() + box.getY());
        });


//        box.translateXProperty().lessThan(0)
//                .addListener(((observableValue, aBoolean, t1) -> {
//                if (t1 && box.isDragging())
//                    parent.set
//        }));
    }


    private static final int GRID_SPACING = 10;

    private void drawLines(int startX, int startY, double width, double height){
        getChildren().clear();
        for (int i=startX; i<width; i+=GRID_SPACING) {
            // Vertical Lines
            Line vertLine = new Line(i, startY, i, height);
            vertLine.setStroke(Color.LIGHTGRAY);
            getChildren().add(vertLine);
        }

        for (int i=startY; i<height; i+=GRID_SPACING){
            Line horiLine = new Line(startX, i, width, i);
            horiLine.setStroke(Color.LIGHTGRAY);
            getChildren().add(horiLine);
        }
    }


}
