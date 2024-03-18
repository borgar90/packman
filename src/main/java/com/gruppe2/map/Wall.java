package com.gruppe2.map;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: Dette er en klasse som representerer en vegg. Den arver fra MapObject-klassen.
 * En vegg har en størrelse og en form.
 * Denne klassen har metoder for å hente og sette størrelsen til veggen, og for å hente formen til veggen.
 */

public class Wall extends MapObject{
    protected double tileSizeWidth;
    protected double tileSizeHeight;
    protected Rectangle wallShape;


    public Wall( double x, double y, double tileSizeWidth, double tileSizeHeight ) {
        super(x, y);
        this.tileSizeWidth = tileSizeWidth;
        this.tileSizeHeight = tileSizeHeight;
        this.wallShape = new Rectangle(x, y, tileSizeWidth, tileSizeHeight);
        this.wallShape.setFill(Color.BLUE);
    }

    public Rectangle getWallShape() {
        return wallShape;
    }

}
