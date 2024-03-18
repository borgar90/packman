package com.gruppe2.map;

import javafx.scene.paint.Color;
/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: Dette er en klasse som representerer en tablett. Den arver fra Tablet-klassen.
 */
public class SmallTablet extends Tablet {
    public SmallTablet(double x, double y, double radius){
        super(x, y, radius, Color.BLACK, 1);
    }
}
