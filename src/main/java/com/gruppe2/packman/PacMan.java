package com.gruppe2.packman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PacMan extends GameCharacter {
    private int radius = 20;
    private int dx = 0;
    private int dy = 0;
    private double x;
    private double y;

    private Circle pacManShape;




    public PacMan(double x, double y) {
        this.x = x;
        this.y = y;
        this.pacManShape = new Circle(x, y, radius, Color.YELLOW);
    }

    public Circle getShape() {
        return pacManShape;
    }
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
        pacManShape.setCenterX(x);
        pacManShape.setCenterY(y);
    }

    public int getRadius(){
        return radius;
    }

    public void setDx(int dx){
        this.dx = dx;
    }
    public void setDy(int dx){
        this.dx = dx;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
