package com.testfx;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class DragableBox extends Rectangle {

    private boolean runningUpdate = false;
    private Optional<Point2D> previousDragPosition = Optional.empty();
    private Point2D previousTranslate;

    public DragableBox(ScrollPane scrollPane, Pane gridRoot, double x, double y, double width, double height) {
        super(0, 0, width, height);
        setTranslateX(x);
        setTranslateY(y);

//        previousTranslate = new Point2D(x, y);
        setPreviousTranslate();

        addEventHandler(MouseEvent.DRAG_DETECTED, mouseEvent -> {

            /*
             * Scroll pane's bounds are where it is relative to entire scene, since this is a first child.
             */
            Bounds scrollBoundary = scrollPane.getBoundsInParent();

            if (mouseEvent.getSceneX() < scrollBoundary.getMinX()
                    || mouseEvent.getSceneY() < scrollBoundary.getMinY())
                return;

            previousDragPosition = Optional.of(new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY()));

        });

        scrollPane.hvalueProperty().addListener((observableValue, number, t1) -> {
            System.out.println("From Hvalue (" + number + ") to (" + t1 + ")");
        });


        addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            Bounds scrollBounds = scrollPane.getBoundsInParent();
            Bounds viewportBounds = scrollPane.getViewportBounds();

//            System.out.println("mouses scene x: " + mouseEvent.getSceneX());
//            System.out.println("viewport bounds: " + viewportBounds.getMaxX());
//            System.out.println("viewport widdth: " + viewportBounds.getWidth());
//            System.out.println("scroll bounds: " + scrollBounds.getMinX());


//            System.out.println("Scroll Bounds: " + scrollBounds);
//            System.out.println("viewport Bounds: " + viewportBounds);
            Point2D dragPosition = new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY());

            if (getTranslateX() < 0) {
                setTranslateX(1);
                return;
            }

            if (getTranslateY() < 0) {
                setTranslateY(1);
                return;
            }

//            if (getTranslateX() > scrollPane.getWidth() + getWidth() + 1){
//                double translateX = getTranslateX();
//                setTranslateX(scrollPane.getWidth() + getWidth());
//                double v = translateX - (scrollPane.getWidth() + getWidth());
//
//                if (previousDragPosition.isPresent()){
//                    previousDragPosition = Optional.of(new Point2D(previousDragPosition.get().getX() - v, previousDragPosition.get().getY()));
//                }
//                System.out.println("Blocked");
//                return;
//            }
//
//            if (getTranslateY() > scrollPane.getHeight() + getHeight() + 1) {
//                setTranslateY(scrollPane.getHeight() + getHeight());
//                return;
//            }

            /*
             * Mechanism for Scrolling scrollpane left when mouse drags
             */
            if (mouseEvent.getSceneX() < scrollBounds.getMinX()) {

//                double percentIntoGrid = (getTranslateX()) / gridRoot.getMinWidth();
                double widthPercent = getWidth() / (gridRoot.getMinWidth());
                System.out.println("Width percent: " + widthPercent);
//                System.out.println(scrollPane.getHmax());

                double dragDelta = dragPosition.getX() - previousDragPosition.get().getX();
                double percentDrag = dragDelta / (gridRoot.getMinWidth());


                if (scrollPane.getHvalue() > 0 && dragDelta < 0) {

//                    double originalHvalue = scrollPane.getHvalue();
//                    /*
//                     * The precent to adjust should be about the width of the box itself.
//                     */
//                    double increment = originalHvalue + (percentDrag) + (0.5  * percentDrag);

                    System.out.println("percent drag " + percentDrag);
                    scrollPane.setHvalue(Math.max(0, scrollPane.getHvalue() + percentDrag + (.5* percentDrag)));

                    setTranslateX(getTranslateX() + dragDelta);

                    double amountIntoGridRoot = scrollPane.getHvalue() * gridRoot.getMinWidth();

                    setTranslateX(Math.max(getTranslateX() + dragDelta, amountIntoGridRoot));

                    previousDragPosition = Optional.of(dragPosition);

                    return;

                    /*
                     * The box's x position should be at 0
                     */
//                    System.out.println("Before, X set to: " + getTranslateX());

                    /*
                     * Get Hvalue's worth of
                     */

//                    setTranslateX(scrollBounds.getWidth() + );
//                    System.out.println("Translate X set to Min: " + getTranslateX());
//                    previousDragPosition = Optional.of(new Point2D(previousDragPosition));
//                    return;
//                    return;
                }else  {
                    /*
                     * If the mouse is traveling right, update the previous ddrag position. otherwise do nothing
                     */
                    if (previousDragPosition.isPresent()){
                        // moved right
                        if (previousDragPosition.get().getX() < mouseEvent.getScreenX()) {
                            previousDragPosition = Optional.of(new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY()));
                        }
                    }
                }
//                else
//                    return;
            }

            if (mouseEvent.getSceneY() < scrollBounds.getMinY()) {
                if (scrollPane.getVvalue() > 0){
                    scrollPane.setVvalue(Math.max(0, scrollPane.getVvalue() - .01));

//                    System.out.println("Before, Y set to: " + getTranslateY());
//                    setTranslateY(scrollBounds.getMinY());
//                    System.out.println("Translate Y set to Min: " + getTranslateY());
                    return;
                } else
                    return;
            }


            /*
             * Mechanism for scrolling down
             */
            if (mouseEvent.getSceneY() > viewportBounds.getHeight() + scrollBounds.getMinY()) {

                if (gridRoot.getMinHeight() < getTranslateY() + getHeight()) {
                    gridRoot.setMinHeight(getTranslateY() + getHeight());
//                    scrollPane.setVvalue(1);
                    return;
                }


                if (scrollPane.getVvalue() < 1) {
                    /*
                     * Then there is room to scroll
                     */
//                    if (previousDragPosition.isPresent()){
//                        double yDrag = mouseEvent.getScreenY() - previousDragPosition.get().getY();
//                        scrollPane.setVvalue(Math.min(1, scrollPane.getVvalue() - yDrag));
//
//                    }
                    scrollPane.setVvalue(Math.min(1, scrollPane.getVvalue() + .01));
//                    System.out.println("Before, Y set to: " + getTranslateY());
//                    setTranslateY(scrollBounds.getMaxY());
//                    System.out.println("Translate Y set to Max: " + getTranslateY());

                    return;
                }


                /*
                 * When the the box is past the grid's min size, then we need to expand this gridRoot.
                 * Recall, this is not the screenpane viewport
                 */
                if (gridRoot.getMinHeight() < getTranslateY() + getHeight()) {
                    gridRoot.setMinHeight(getTranslateY() + getHeight());
//                    scrollPane.setVvalue(1);
                }

            }

            /*
             * If the mouse position is past the scroll pane bounds
             */
            if (mouseEvent.getSceneX() > viewportBounds.getWidth() + scrollBounds.getMinX()  ){
//                System.out.println(mouseEvent.getSceneX() + " greater than " + (viewportBounds.getWidth() + scrollBounds.getMinX()));
//                System.out.println("mouses scene x: " + mouseEvent.getSceneX());
//                System.out.println("viewport bounds: " + viewportBounds.getMaxX());
//                System.out.println("scroll bounds: " + scrollBounds.getMinX());

                /*
                 * The mouse has exceeded
                 */

                /*
                 * If the brick has exceeded the gridRoot range, then update the size of the grid root.
                 */


                /*
                 * If the scrollPane is not all the way scrolled, then scroll for at least the delta to where the grid is.
                 *
                 * Let hvalue be the percent x into the width of the scrollpane.
                 * Let the scroll pane have size S
                 * Let the total size be gridRoot.minWidth = G
                 * Let the position of the box be position B
                 *
                 * B > S and H < 1, then we want to scroll S so that B is in view.
                 * The correct value of H is the proporition of B into G that B has taken
                 * B / G = H
                 */
                if (gridRoot.getMinWidth() < getTranslateX() + getWidth()) {
                    gridRoot.setMinWidth(getTranslateX() + getWidth());
                    scrollPane.setHvalue(1);
                    return;
                }

                double percentIntoGrid = (getTranslateX()) / gridRoot.getMinWidth();
                double widthPercent = getWidth() / (gridRoot.getMinWidth());
//                System.out.println(scrollPane.getHmax());

                double dragDelta = dragPosition.getX() - previousDragPosition.get().getX();
                double percentDrag = dragDelta / (gridRoot.getMinWidth());

                if (scrollPane.getHvalue() < percentIntoGrid - widthPercent && dragDelta>= 0) {
                    runningUpdate = true;


//                    System.out.println("percent into grid - width: " + (percentIntoGrid - (widthPercent)));
//                    System.out.println("Grid width: " + gridRoot.getMinWidth());
//                    System.out.println("percent : " + (getTranslateX() / gridRoot.getMinWidth()));
//                    System.out.println("width percent: " + getWidth() + ",  " + widthPercent);
//                    System.out.println("percentDrag " + percentDrag);

//                    scrollPane.setHvalue(Math.min(1, getTranslateX() / gridRoot.getMinWidth()));
                    double originalHvalue = scrollPane.getHvalue();
                    /*
                     * The precent to adjust should be about the width of the box itself.
                     */
                    double increment = originalHvalue + (percentDrag) + (0.5  * percentDrag);
                    scrollPane.setHvalue(Math.min(1, increment));

//                    double actualMovePercent = (scrollPane.getHvalue() - originalHvalue);
//                    double deltaMovePercent = percentDrag - actualMovePercent;

//                    double widthMove = (scrollPane.getHvalue() - originalHvalue) * (gridRoot.getMinWidth());
                    System.out.println("Scrolling Right: " + dragDelta);
                    setTranslateX(getTranslateX() + dragDelta);
//                (percentDrag * gridRoot.getMinWidth()));

                    previousDragPosition = Optional.of(dragPosition);

                    return;
//                    return;
//                } else if (scrollPane.getHvalue() < (getTranslateX() / gridRoot.getMinWidth()))
//                } else if(scrollPane.getHvalue() < percentIntoGrid - widthPercent) {
//                    double percentHValue = (scrollPane.getHvalue() * scrollPane.getWidth());
//                    setTranslateX(percentHValue + (scrollPane.getHvalue() * gridRoot.getMinWidth()));
//                    previousDragPosition = Optional.of(dragPosition);
//                    return;
                } else  {
                    /*
                     * If the mouse is traveling left, update the previous ddrag position. otherwise do nothing
                     */
                    if (previousDragPosition.isPresent()){
                        // moved left
                        if (previousDragPosition.get().getX() > mouseEvent.getScreenX()) {
                            previousDragPosition = Optional.of(new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY()));
                        }
                    }
                }

//                if (scrollPane.getHvalue())
//                return;

//                return;
//                else





//                if (scrollPane.getHvalue() < 1) {
//                    scrollPane.setHvalue(Math.min(1, scrollPane.getHvalue() + .01));
//                    System.out.println("Before, X set to: " + getTranslateX());
//                    setTranslateX(scrollPane.get);
//                    Point2D delta = previousDragPosition.get().subtract(dragPosition);

//                    if (Math.abs(delta.getX()) < 4 && Math.abs(delta.getY()) < 4)
//                        return;
//
//                    previousDragPosition = Optional.of(dragPosition);
//
//                    this.setTranslateX(getTranslateX() - delta.getX());
//                    System.out.println("Translate X set to Max: " + getTranslateX());
//                    return;
//                }

//                if (gridRoot.getMinWidth() < getTranslateX() + getWidth())
//                    gridRoot.setMinWidth(getTranslateX() + getWidth());
            }

            if (previousDragPosition.isEmpty())
                return;

            Point2D delta = previousDragPosition.get().subtract(dragPosition);

            if (Math.abs(delta.getX()) < 4 && Math.abs(delta.getY()) < 4)
                return;

            previousDragPosition = Optional.of(dragPosition);

            this.setTranslateX(getTranslateX() - delta.getX());
            this.setTranslateY(getTranslateY() - delta.getY());
            System.out.println("SET");
        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            previousDragPosition = Optional.empty();
        });

    }


    private Point2D deltaTranslate() {
        Point2D point2D = new Point2D(previousTranslate.getX(), previousTranslate.getY());
        return point2D.subtract(localToScreen(new Point2D(this.getTranslateX(), this.getTranslateY())));
    }

    private void setPreviousTranslate(){
        this.previousTranslate = localToScreen(new Point2D(this.getTranslateX(), this.getTranslateY()));
    }
}
