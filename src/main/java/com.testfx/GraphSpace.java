package com.testfx;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphSpace extends Group {

    private DoubleProperty widthContext = new SimpleDoubleProperty();
    private DoubleProperty heightContext = new SimpleDoubleProperty();

    private Group scrollGroup;
    private Pane host;
    private ScrollPane scrollPane;
//    private Group moreLinesGroup;

    private double currentHeight;
    private double currentWidth;


    double maxX;
    double maxY;
    double prefWidth;
    double prefHeight;

    Pane shapeHost;
    Group boxHost;
    Group lineHost;
    List<DragableBox> boxes;

    /*
     * Lol you actually need this or else boolean bindings give up (they are weak listeners)
     */
    private List<BooleanBinding> bindingsForReference = new ArrayList<>();



    public GraphSpace(ReadOnlyDoubleProperty [] spatialContext, double prefWidth, double prefHeight) {
        super();
        HBox.setHgrow(this, Priority.NEVER);

//        this.prefWidth = prefWidth;
//        this.prefHeight = prefHeight;
//        this.maxWidth(this.prefWidth);

        maxX = 0;
        maxY = 0;

//        this.maxWidth(prefWidth);
//        this.maxHeight(prefHeight);
//        this.setPrefSize(prefWidth, prefHeight);

        host = new Pane();
        host.setPrefWidth(prefWidth);
        host.setPrefHeight(prefHeight);


        Rectangle initialSlab = new Rectangle(0, 0, prefWidth, prefHeight);
        host.getChildren().add(initialSlab);
        initialSlab.setFill(Color.DARKRED);

        shapeHost = new Pane();
        shapeHost.setMaxHeight(prefHeight);
        shapeHost.setMaxWidth(prefWidth);
        getChildren().add(shapeHost);



        lineHost = new Group();
        shapeHost.getChildren().add(lineHost);

        boxHost = new Group();
//        boxHost.maxHeight(prefHeight);
//        boxHost.maxWidth(prefWidth);
        shapeHost.getChildren().add(boxHost);

        scrollPane = new ScrollPane(this);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        scrollPane.setPannable(true);
//        scrollPane.setViewportBounds(host.getBoundsInParent());
//        scrollPane.setMaxWidth(prefWidth);
//        scrollPane.setMaxHeight(prefHeight);

//        host.widthProperty().addListener((observableValue, number, t1) -> {
//            scrollPane.setMaxWidth(t1.doubleValue());
////            scrollPane.setMaxHeight(t1.doubleValue());
//        });

        host.getChildren().add(scrollPane);

//        host.boundsInLocalProperty().addListener((observableValue, bounds, t1) -> {
//            scrollPane.setMaxWidth(t1.getMaxX());
//            scrollPane.setMaxHeight(t1.getMaxY());
////            drawLines(0, 0, Math.max(maxX, prefWidth), Math.max(maxY, prefHeight));
//        });

//        addSizeListeners();
//
//        scrollPane.hvalueProperty().addListener((observableValue, number, t1) -> {
//            drawLines(0, 0, currentWidth + (t1.doubleValue() * currentWidth), currentHeight);
////            System.out.println("TEST");
////            drawMoreLines(currentWidth, 0, currentWidth + (t1.doubleValue() * currentWidth), currentHeight);
////            System.out.println(t1.doubleValue() * currentWidth);
//
////            drawLines(0, 0, maxX, currentHeight);
//        });

        widthContext.bind(spatialContext[0].add(spatialContext[2]));
        heightContext.bind(spatialContext[1].add(spatialContext[3]));

        drawLines(0, 0, prefWidth, prefHeight);
    }

    public void addSizeListeners(){
        host.widthProperty().addListener(((observableValue, number, t1) -> {

            /*
             * Making it larger, draw more lines.
             */
//            if (number.doubleValue() < t1.doubleValue()) {
//                drawLines(0, 0, t1.doubleValue(), currentHeight);
//            } else {
//                // Making it smaller, draw less lines.
//                drawLines(0, 0, number.doubleValue(), currentHeight);
//            }
//            if (t1.doubleValue() < maxX) {
////                currentWidth = maxX;
//                drawLines(0, 0, maxX, currentHeight);
////                scrollPane
////                System.out.println(maxX);
//            } else {
//                drawLines(0, 0, t1.doubleValue(), currentHeight);
//            }
//            drawLines(0, 0, t1.doubleValue() + (scrollPane.getHvalue() * t1.doubleValue()), currentHeight);
            drawLines(0, 0, t1.doubleValue(), currentHeight);
            currentWidth = t1.doubleValue();
        }));

        host.heightProperty().addListener(((observableValue, number, t1) -> {
            drawLines( 0, 0, currentWidth, t1.doubleValue());
            currentHeight = t1.doubleValue();
        }));


    }

    public void registerBoxToSizeListener(Stage stage, DragableBox box){

        /*
         * Boolean bindings for size
         */

        BooleanBinding parentTooSkinny = stage.widthProperty().subtract(widthContext)
                .lessThan(box.translateXProperty().add(box.getX() + box.getWidth()));

        bindingsForReference.add(parentTooSkinny);

        parentTooSkinny.addListener((observableValue, aBoolean, t1) -> {
            if (t1 && box.isDragging())
                stage.setWidth(box.translateXProperty().add(widthContext).get() + box.getWidth() + box.getX());
        });


        BooleanBinding parentTooShort = stage.heightProperty().subtract(heightContext)
                .lessThan(box.translateYProperty().add(box.getHeight() + box.getY()));

        bindingsForReference.add(parentTooShort);

        parentTooShort.addListener((observableValue, aBoolean, t1) -> {
            if (t1 && box.isDragging())
                stage.setHeight(box.translateYProperty().add(heightContext).get() + box.getHeight() + box.getY());

        });

        /*
         * Listeners for max size of graph space
         */
        box.translateXProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() + box.getWidth() > maxX){
                maxX = t1.doubleValue() + box.getWidth();
//                currentWidth = maxX;
//            }
//            else if (doubleEquals(number.doubleValue(), maxX) && t1.doubleValue() < maxX){
//                Optional<Double> max = boxes.stream().filter(b -> ! b.equals(box))
//                        .map(b -> b.getTranslateX() + b.getWidth()).max(Double::compare);
//                if (max.isPresent() && max.get().compareTo(t1.doubleValue() + box.getWidth()) < 0){
//                    maxX = max.get();
//                } else
//                    maxX = t1.doubleValue() + box.getWidth();
//                scrollPane.setViewportBounds(maxX);
//                System.out.println(scrollPane.getHvalue());

//                drawLines(0, 0, Math.max(maxX + box.getWidth(), prefWidth), Math.max(maxY + box.getHeight(), prefHeight));
            } else {
                Optional<Double> max = boxes.stream().filter(b -> ! b.equals(box))
                        .map(b -> b.getTranslateX() + b.getWidth()).max(Double::compare);
                if (max.isPresent() && max.get().compareTo(t1.doubleValue() + box.getWidth()) < 0){
                    maxX = max.get();
                } else
                    maxX = t1.doubleValue() + box.getWidth();
            }

            System.out.println(maxX);

        });

        box.translateYProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() > maxY){
                maxY = t1.doubleValue();
            } else if (doubleEquals(number.doubleValue(), maxY) && t1.doubleValue() < maxY){
                Optional<Double> max = boxes.stream().map(Node::getTranslateY).max(Double::compare);
                if (max.isPresent() && max.get().compareTo(t1.doubleValue()) > 0){
                    maxY = max.get();
                } else
                    maxY = t1.doubleValue();
            }

        });
    }

    private static final int GRID_SPACING = 10;

    private void drawLines(double startX, double startY, double width, double height){
        lineHost.getChildren().clear();
        int startx = (int) Math.floor(startX);
        int starty = (int) Math.floor(startY);
        for (int i=startx; i<width; i+=GRID_SPACING) {
            // Vertical Lines
            Line vertLine = new Line(i, startY, i, height);
            vertLine.setStroke(Color.LIGHTGRAY);
            lineHost.getChildren().add(vertLine);
        }

        for (int i=starty; i<height; i+=GRID_SPACING){
            Line horiLine = new Line(startX, i, width, i);
            horiLine.setStroke(Color.LIGHTGRAY);
            lineHost.getChildren().add(horiLine);
        }

//        getChildren().addAll(boxes);

    }


    public void addBox(DragableBox box){
        if (boxes == null)
            boxes = new ArrayList<>();

        boxes.add(box);

        boxHost.getChildren().add(box);

//        getChildren().add(box);
    }


    public Pane getHost() {
        return host;
    }

    static boolean doubleEquals(double d1, double d2) {
        double d = d1 / d2;
        return (Math.abs(d - 1.0) < 0.001);
    }
}
