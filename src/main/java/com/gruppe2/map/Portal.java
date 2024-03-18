package com.gruppe2.map;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: Dette er en klasse som representerer en portal. Den arver fra MapObject-klassen.
 */
public class Portal extends MapObject{

    private double tileSize;

    private double width;

    private Rectangle portalShape;
    private double boardWidth;

    private double boardHeight;
    public Portal( double x, double y, double tileSize, double boardWidth, double boardHeight ) {
        super(x, y);
        this.tileSize = tileSize;
        this.width = 3;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPortalShape();
        portalShape.setFill(Color.LIGHTGREEN);
    }


    /**
     * Metode for å sette portalens form.
     */
    private void setPortalShape(){
        double portalX;
        double portalY;

        if(this.getX()+tileSize == boardWidth){
            portalX = boardWidth -width;
        }else{
            portalX = this.getX();
        }

        if(this.getY() + tileSize*2 == boardHeight){
            portalY = boardHeight - tileSize;
        }else{
            portalY = this.getY();
        }

        this.portalShape = new Rectangle(portalX , portalY , width, tileSize);
    }

    public Rectangle getPortalShape(){
        return portalShape;
    }

}
