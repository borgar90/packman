package com.gruppe2.packman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Ghost extends GameCharacter {
    protected Circle ghostShape;
    protected double x, y; // Ghost's position
    protected final double radius = 10; // Adjust size as needed

    public Ghost(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        ghostShape = new Circle(x, y, radius, color);
    }

    public Circle getShape() {
        return ghostShape;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
        ghostShape.setCenterX(x);
        ghostShape.setCenterY(y);
    }

    // Abstract method for ghost's unique behavior
    public abstract void chase();
}
