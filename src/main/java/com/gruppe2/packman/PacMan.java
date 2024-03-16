package com.gruppe2.packman;

import com.gruppe2.ghost.Ghost;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


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
    private boolean energizerActive;
    private int lives = 3;
    private List<Ghost> ghosts;

    public PacMan(double x, double y, GameManager gm) {
        this.x = x;
        this.y = y;
        this.gm = gm;
        initPacmanShape();
    }

    public PacMan(double x, double y, int radius, GameManager gm){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.gm = gm;
        initPacmanShape();
    }

    public void resetLives(){
        lives = 3;
    }

    private void initPacmanShape(){
        this.pacManShape = new Arc(this.getX(), this.getY(),  this.getRadius(), this.getRadius(), 45, 270);
        this.pacManShape.setType(ArcType.ROUND);
        this.pacManShape.setFill(Color.YELLOW);
        this.pacManShape.setStroke(Color.BLACK);
    }

    public void loseLife(){
        lives -= 1;
    }

    public int getLives(){
        return lives;
    }
    public void setEnergizerActive(){
        energizerActive = true;
        pacManShape.setFill(Color.ORANGE);
        gm.setGhostFlee();
        PauseTransition waitBeforeBlinking = new PauseTransition(Duration.seconds(3.5));

        waitBeforeBlinking.setOnFinished(event -> {

            Timeline blinkTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.15), evt -> pacManShape.setFill(pacManShape.getFill() == Color.ORANGE ? Color.YELLOW : Color.ORANGE)),
                    new KeyFrame(Duration.seconds(0.3))
            );

            blinkTimeline.setCycleCount(Timeline.INDEFINITE);

            PauseTransition stopBlinking = new PauseTransition(Duration.seconds(1.5));
            stopBlinking.setOnFinished(evt -> {
                blinkTimeline.stop();
                energizerActive = false;
                pacManShape.setFill(Color.YELLOW);
                gm.setGhostFlee();
            });
            stopBlinking.play();
            blinkTimeline.play();
        });
        waitBeforeBlinking.play();
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

    public boolean getActive(){
        return energizerActive;
    }
}
