package com.gruppe2.gameCharacters;

import com.gruppe2.utils.GameManager;
import javafx.geometry.Point2D;
import javafx.scene.control.skin.TextInputControlSkin;

public abstract class GameCharacter {
    protected double x, y; // Position
    protected TextInputControlSkin.Direction direction;
    protected GameManager gm;
    protected Point2D startLocation;
    /**
     * Abstrakt metode for å bevege spillkarakteren til en ny posisjon.
     *
     * @param x den nye x-posisjonen.
     * @param y den nye y-posisjonen.
     */

    protected abstract void move(double x, double y);

    /**
     * Abstrakt metode for å gi spillkarakteren ny start-posisjon.
     *
     * @param startLocation den nye x,y-posisjonen.
     */

    public void setStartLocation(Point2D startLocation) {
        this.startLocation = startLocation;
    }


}