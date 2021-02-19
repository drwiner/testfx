package com.testfx;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class DragableBox extends Pane {

    private final Rectangle rectangle;
    private Optional<Point2D> previousDragPosition = Optional.empty();

    private Bounds scrollBoundary;

    private String latexText = "\\langle x, y \\rangle";

    public DragableBox(ScrollPane scrollPane, Pane gridRoot, double x, double y, double width, double height) {
        super();
        setWidth(width);
        setHeight(height);
        setTranslateX(x);
        setTranslateY(y);
        ReadOnlyObjectProperty<Bounds> boundsReadOnlyObjectProperty = scrollPane.boundsInParentProperty();

        addEventHandler(MouseEvent.DRAG_DETECTED, mouseEvent -> {

            this.scrollBoundary = boundsReadOnlyObjectProperty.get();

            if (mouseEvent.getSceneX() < this.scrollBoundary.getMinX()|| mouseEvent.getSceneY() < this.scrollBoundary.getMinY())
                return;

            previousDragPosition = Optional.of(new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY()));

        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            this.scrollBoundary = boundsReadOnlyObjectProperty.get();
            Point2D dragPosition = new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY());

            if (getTranslateX() < 0) {
                setTranslateX(1);
                return;
            }

            if (getTranslateY() < 0) {
                setTranslateY(1);
                return;
            }

            if (mouseEvent.getSceneX() < this.scrollBoundary.getMinX() || mouseEvent.getSceneY() < this.scrollBoundary.getMinY()) {
                return;
            }

            if (mouseEvent.getSceneX() > scrollPane.getViewportBounds().getMaxX() + scrollPane.getBoundsInParent().getMinX()){
                gridRoot.setMinWidth(getTranslateX() + getWidth());
                scrollPane.setHvalue(1);
            }

            if (mouseEvent.getSceneY() > scrollPane.getViewportBounds().getMaxY() + scrollPane.getBoundsInParent().getMinY()) {
                gridRoot.setMinHeight(getTranslateY() + getHeight());
                scrollPane.setVvalue(1);
            }

            if (previousDragPosition.isEmpty())
                return;

            Point2D delta = previousDragPosition.get().subtract(dragPosition);

            if (Math.abs(delta.getX()) < 4 && Math.abs(delta.getY()) < 4)
                return;

            previousDragPosition = Optional.of(dragPosition);
            this.setTranslateX(getTranslateX() - delta.getX());
            this.setTranslateY(getTranslateY() - delta.getY());

        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            previousDragPosition = Optional.empty();
        });

        StackPane stackPane = new StackPane();
        rectangle = new Rectangle( width, height);
        stackPane.getChildren().add(rectangle);

        getChildren().add(stackPane);
        
        LateXCanvas lateXCanvas = new LateXCanvas("\\hspace{0.17cm} \\tanh*\\sigma", 14);

        lateXCanvas.widthProperty().bind(stackPane.widthProperty());
        lateXCanvas.heightProperty().bind(stackPane.heightProperty());


        stackPane.getChildren().add(lateXCanvas);

//        lateXCanvas.draw();

//        LateXMathControl lateXMathControl = new LateXMathControl(latexText);
//        stackPane.getChildren().add(lateXMathControl);


    }

    public void setFill(Color azure) {
        rectangle.setFill(azure);
    }

    public void setStrokeWidth(int i) {
        rectangle.setStrokeWidth(i);
    }

    public void setStroke(Color black) {
        rectangle.setStroke(black);
    }
}
