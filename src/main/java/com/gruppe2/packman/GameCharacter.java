package com.gruppe2.packman;

import javafx.scene.control.skin.TextInputControlSkin;

public abstract class GameCharacter {
    protected int x, y; // Position
    protected TextInputControlSkin.Direction direction;
    protected GameManager gm;


    protected abstract void move(double x, double y);

}