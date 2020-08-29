package com.testfx;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GraphPane extends Pane {

    private double currentHeight;
    private double currentWidth;
    private double stageWidthExtra;

    /*
     * Lol you actually need this or else boolean bindings give up (they are weak listeners)
     */
    private List<BooleanBinding> bindingsForReference = new ArrayList<>();

    Pane host;
    Group gridLines;
    Group boxes;

    private AtomicReference<Group> atomRef;


    public GraphPane(double stageWidthExtra, double width, double height) {
        this.stageWidthExtra = stageWidthExtra;
        setPrefWidth(width);
        setPrefHeight(height);

        gridLines = new Group();
        getChildren().add(gridLines);
//        atomRef = new AtomicReference<>(gridLines);

        drawLines( 0, 0, width, height);

        boxes = new Group();
        getChildren().add(boxes);

        currentHeight = height;
        currentWidth = width;

        boundsInParentProperty().addListener((observableValue, bounds, t1) -> {
            if (t1.getMaxX() > currentWidth && !drawing){
                drawLines(0, 0, t1.getMaxX()-1, currentHeight);
                currentWidth = t1.getMaxX();
            }
            else if (t1.getMaxY() > currentHeight && !drawing) {
                drawLines(0, 0, currentWidth, t1.getMaxY() -1);
                currentHeight = t1.getMaxY();
            }
        });
    }


    public void addBox(DragableBox box){
        getChildren().add(box);
        box.translateXProperty().addListener((observableValue, number, t1) -> {
//            System.out.println("Bounds in local: " + getBoundsInLocal().getMaxX());
//            System.out.println("Bounds in parent: " + getBoundsInParent().getMaxX());
//            System.out.println("box x: " + box.getTranslateX());
//            System.out.println("Current width: " + currentWidth);
//            System.out.println();
            if (box.isDragging() && t1.doubleValue() > getBoundsInLocal().getMaxX()){
//                System.out.println("Bounds in local: " + getBoundsInLocal().getMaxX());
//                System.out.println("box x: " + box.getTranslateX());
//                drawLines(0, 0, getBoundsInLocal().getMaxX(), currentHeight);
            }
        });
    }

    public void addSizeListeners(Pane parent){
        host = parent;

//        parent.maxWidthProperty().addListener((observableValue, number, t1) -> {
////            System.out.println(t1.doubleValue());
//        });

        parent.widthProperty().addListener(((observableValue, number, t1) -> {
//            System.out.println(t1.doubleValue());
//            System.out.println(t1.doubleValue());
            if (t1.doubleValue() > number.doubleValue()) {
                System.out.println("Grids should grow");
//                drawLines(0, 0, t1.doubleValue(), currentHeight);
//                currentWidth = t1.doubleValue();
            }
        }));

        parent.heightProperty().addListener(((observableValue, number, t1) -> {
//            drawLines( 0, 0, currentWidth, t1.doubleValue());
//            currentHeight = t1.doubleValue();
        }));


        parent.layoutBoundsProperty().addListener((observableValue, bounds, t1) -> {
            System.out.println(t1);
//            if (t1.getMaxX() > currentWidth){
//                drawLines( 0, 0, parent.getBoundsInLocal().getMaxX(), currentHeight);
//                currentWidth = parent.getBoundsInLocal().getMaxX();
//            }
        });



//        bindingsForReference.add(gridsTooSkinny);
//        parent.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
//            System.out.println("Parent: " + t1);
//            System.out.println(parent.getBoundsInLocal().getMaxX());
//        });
////
//        gridsTooSkinny.addListener((observableValue, aBoolean, t1) -> {
//            if (t1){
////                System.out.println("Grids too skinny");
////                System.out.println(parent.widthProperty());
//                drawLines( 0, 0, parent.getBoundsInLocal().getMaxX(), currentHeight);
////                currentWidth = t1.doubleValue();
//                currentWidth = parent.getBoundsInLocal().getMaxX();
////                System.out.println(parent.getWidth());
//            }
//        });

    }

    public void registerBoxToSizeListener(Stage parent, DragableBox box){

        BooleanBinding parentTooSkinny = parent.widthProperty().subtract(stageWidthExtra)
                .lessThan(box.translateXProperty().add(box.getX() + box.getWidth()));

        bindingsForReference.add(parentTooSkinny);

        parentTooSkinny.addListener((observableValue, aBoolean, t1) -> {
                if (t1 && box.isDragging()) {
//                    parent.setWidth(box.translateXProperty().get() + box.getWidth() + box.getX() + stageWidthExtra);
                }
        });

        BooleanBinding parentTooShort = parent.heightProperty()
                .lessThan(box.translateYProperty().add(box.getHeight() + box.getY()));

        bindingsForReference.add(parentTooShort);

        parentTooShort.addListener((observableValue, aBoolean, t1) -> {
//                if (t1 && box.isDragging())
//                    parent.setHeight(box.translateYProperty().get() + box.getHeight() + box.getY());
        });
    }

    private static final int GRID_SPACING = 10;

    boolean drawing = false;

    private void drawLines(int startX, int startY, double width, double height){
//        System.out.println("Drawing lines: " + width +", " + height);
        drawing = true;

//        getChildren().add(gridLines);
//        gridLines.toBack();
//        if (gridLines.getChildren().size() > 0) {
        gridLines.getChildren().clear();
//        atomRef.getAndSet(new Group())


//        List<Line> newLines = new ArrayList<>();
//        }
        for (int i=startX; i<width; i+=GRID_SPACING) {
            // Vertical Lines
            Line vertLine = new Line(i, startY, i, height);
            vertLine.setStroke(Color.LIGHTGRAY);
//            newLines.add(vertLine);
            gridLines.getChildren().add(vertLine);
        }

        for (int i=startY; i<height; i+=GRID_SPACING){
            Line horiLine = new Line(startX, i, width, i);
            horiLine.setStroke(Color.LIGHTGRAY);
//            newLines.add(horiLine);
            gridLines.getChildren().add(horiLine);
        }

        drawing = false;
//        gridLines.getChildren().setAll(newLines);
    }


}
