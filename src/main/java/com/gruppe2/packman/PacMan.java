package com.gruppe2.packman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;


/*
Author: Borgar Flaen Stensrud
Usage: Denne klassen er pacman character. Den inneholder både posisjonen og shape av pacman. Den har
metoder for å hente speed og flytte pacman, samt hente ut alt av verdier.
 */

public class PacMan extends GameCharacter {
    private int radius = 7;
    private int dx = 0;
    private int dy = 0;
    private double x;
    private double y;
    private int speed = 10;
    private Arc pacManShape;




    public PacMan(double x, double y) {
        this.x = x;
        this.y = y;
        this.pacManShape = new Arc(this.getX(), this.getY(),  this.getRadius(), this.getRadius(), 45, 270);
        this.pacManShape.setType(ArcType.ROUND);
        this.pacManShape.setFill(Color.YELLOW);
    }

    public Arc getShape() {
        return pacManShape;
    }
    //Flytter på pacman
    public void move(double dx, double dy) {
        this.x = dx;
        this.y = dy;
        pacManShape.setCenterX(x);
        pacManShape.setCenterY(y);
    }

    public void updatePosition() {
    }

    public int getRadius(){
        return radius;
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }
    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
    public int getSpeed(){
        return speed;
    }
}
