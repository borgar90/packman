package com.gruppe2.packman;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class GameManager {
    private PacMan pacMan;
    private GameUI gameUI;

    public GameManager(){
        this.pacMan = new PacMan(50,50);
    }

    public void setGameUI(GameUI gameUI){
        this.gameUI = gameUI;
    }

    public void startGame(){
        Arc pacManArc = new Arc(pacMan.getX(), pacMan.getY(),  pacMan.getRadius(), pacMan.getRadius(), 45, 270);
        pacManArc.setType(ArcType.ROUND);
        gameUI.addGameElement(pacManArc);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                pacMan.move(50,50);
                pacManArc.setCenterX(pacMan.getX());
                pacManArc.setCenterY(pacMan.getY());
            }
        }.start();
    }

    public void handleKeyPress( KeyCode keyCode) {
        // Update Pac-Man's direction based on key press
        switch (keyCode) {
            case W: pacMan.setDy(-5); pacMan.setDx(0); break;
            case S: pacMan.setDy(5); pacMan.setDx(0); break;
            case A: pacMan.setDx(-5); pacMan.setDy(0); break;
            case D: pacMan.setDx(5); pacMan.setDy(0); break;
            default: break; // No action on other keys
        }
    }

}