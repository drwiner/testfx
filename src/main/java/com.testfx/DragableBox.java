package com.testfx;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class DragableBox extends Rectangle {

    private Optional<Point2D> previousDragPosition = Optional.empty();

    public boolean isDragging() {
        return previousDragPosition.isPresent();
    }

    public DragableBox(double x, double y, double width, double height) {
        super(0, 0, width, height);
        setTranslateX(x);
        setTranslateY(y);

        addEventHandler(MouseEvent.DRAG_DETECTED, mouseEvent -> {

            if (mouseEvent.getSceneX() < 0 || mouseEvent.getSceneY() < 0)
                return;

            previousDragPosition = Optional.of(new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY()));
        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {

            Point2D dragPosition = new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY());

            if (getTranslateX() < 0) {
                setTranslateX(1);
                return;
            }

            if (getTranslateY() < 0) {
                setTranslateY(1);
                return;
            }

            if (mouseEvent.getSceneX() < 0 || mouseEvent.getSceneY() < 0)
                return;

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



    }
}
