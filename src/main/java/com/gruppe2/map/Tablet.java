package com.gruppe2.map;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Tablet extends MapObject {

    private double radius;
    private boolean isConsumed;
    private Color color;
    private Circle shape;
    private int points;
    public Tablet( double x, double y, double radius, Color color, int points ){
        super(x, y);

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
