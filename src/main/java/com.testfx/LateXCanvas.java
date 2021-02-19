package com.testfx;

import javafx.scene.canvas.Canvas;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.Box;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class LateXCanvas extends Canvas {

    private FXGraphics2D g2;
    private Box box;

    public LateXCanvas(String latexText, float size) {
        this.g2 = new FXGraphics2D(getGraphicsContext2D());
        this.g2.scale(size, size);

//        String oldText="x=\\frac{-b \\pm \\sqrt {b^2-4ac}}{2a}";
        // create a formula
        TeXFormula formula = null;
        try {
            formula = new TeXFormula(latexText);
        } catch (org.scilab.forge.jlatexmath.ParseException e){
            e.printStackTrace();
        }
        if (formula != null) {
            TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);

            // the 'Box' seems to be the thing we can draw directly to Graphics2D
            this.box = icon.getBox();

            // Redraw canvas when size changes.
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
        }
    }

    public void draw() {
        double width = getWidth();
        double height = getHeight();
        getGraphicsContext2D().clearRect(0, 0, width, height);
        this.box.draw(g2, 0, 1);
//        System.out.println("box width: " + this.box.getWidth());
//        System.out.println("Box height: " + this.box.getHeight());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) { return getWidth(); }

    @Override
    public double prefHeight(double width) { return getHeight(); }
}