package com.gruppe2.packman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Tablet {
    private double x;
    private double y;
    private double radius;
    private boolean isConsumed;
    private Color color;
    private Circle shape;
    private int points;
    public Tablet( double x, double y, double radius, Color color, int points ){
       this.x = x;
       this.y = y;
       this.radius = radius;
       this.color = color;
       this.shape = new Circle(x, y, radius);
       this.shape.setFill(color);
       this.points = points;
    }

    public int getPoints(){
        return points;
    }


    public boolean isConsumed(){
        return isConsumed;
    }

    public Circle getShape(){
        return shape;
    }




}
