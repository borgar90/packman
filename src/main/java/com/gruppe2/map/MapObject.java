package com.gruppe2.map;
/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: Dette er en klasse som representerer et objekt på kartet.
 * Denne klassen er en superklasse for alle objekter på kartet.
 * Denne klassen inneholder x- og y-koordinater.
 */
public class MapObject {

    private double x;

    private double y;

    public MapObject(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX( double x ) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY( double y ) {
        this.y = y;
    }
}
