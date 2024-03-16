package com.gruppe2.map;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    public double getTileSizeWidth() {
        return tileSizeWidth;
    }

    public void setTileSizeWidth( double tileSizeWidth ) {
        this.tileSizeWidth = tileSizeWidth;
    }

    public double getTileSizeHeight() {
        return tileSizeHeight;
    }

    public void setTileSizeHeight( double tileSizeHeight ) {
        this.tileSizeHeight = tileSizeHeight;
    }

    public Rectangle getWallShape() {
        return wallShape;
    }

}
