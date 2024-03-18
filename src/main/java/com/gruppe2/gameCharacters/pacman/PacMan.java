package com.gruppe2.gameCharacters.pacman;

import com.gruppe2.gameCharacters.GameCharacter;
import com.gruppe2.utils.GameManager;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;


/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: Denne klassen representerer Pacman-karakteren. Den inneholder posisjonen og formen til Pacman.
 * Den har metoder for å hente hastighet, flytte Pacman og hente ut forskjellige verdier.
 */

public class PacMan extends GameCharacter {
    private int radius = 7;
    private int speed = 5;
    private Arc pacManShape;
    private boolean energizerActive;
    private int lives = 3;

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

    /*** Tegner pacman ***/
    private void initPacmanShape(){
        this.pacManShape = new Arc(this.getX(), this.getY(),  this.getRadius(), this.getRadius(), 45, 270);
        this.pacManShape.setType(ArcType.ROUND);
        this.pacManShape.setFill(Color.YELLOW);
        this.pacManShape.setStroke(Color.BLACK);
        pacManShape.setStartAngle(45);
    }



    public void move(double dx, double dy) {
        direction(dx, dy);
        setPosistion(dx, dy);
    }

    /*** vender pacman i riktig retning ***/
    private void direction(double dx, double dy){
        if(dx > x){
            pacManShape.setStartAngle(45);
        }else if(dx < x){
            pacManShape.setStartAngle(225);
        }else if(dy > y){
            pacManShape.setStartAngle(315);
        }else if(dy < y){
            pacManShape.setStartAngle(135);
        }
    }



    // fjerner liv og resetter pacman til startposisjon
    public void loseLife(){
        if(lives == 0) return;
        lives--;
        setPosistion(this.startLocation.getX(), this.startLocation.getY());
    }


    /*** SETTERS ***/
    public void setPosistion(double dx, double dy){
        this.x = dx;
        this.y = dy;
        pacManShape.setCenterX(x);
        pacManShape.setCenterY(y);
    }
    public void setEnergizerActive(){
        energizerActive = true;
        pacManShape.setFill(Color.ORANGE); // Energizeren: pac får oransje farge
        gm.setGhostFlee(); // Spøkelsene blir redde
        PauseTransition waitBeforeBlinking = new PauseTransition(Duration.seconds(3.5));

        waitBeforeBlinking.setOnFinished(event -> {

            Timeline blinkTimeline = new Timeline( // Blinker energizeren
                    new KeyFrame(Duration.seconds(0.15), evt -> pacManShape.setFill(pacManShape.getFill() == Color.ORANGE ? Color.YELLOW : Color.ORANGE)),
                    new KeyFrame(Duration.seconds(0.3))
            );

            blinkTimeline.setCycleCount(Timeline.INDEFINITE);

            PauseTransition stopBlinking = new PauseTransition(Duration.seconds(1.5));
            stopBlinking.setOnFinished(evt -> { // Slutter å blinke
                blinkTimeline.stop();
                energizerActive = false; // Energizeren er ikke lenger aktiv
                pacManShape.setFill(Color.YELLOW);
                gm.setGhostFlee(); // Spøkelsene blir ikke lenger redde
            });
            stopBlinking.play();
            blinkTimeline.play();
        });
        waitBeforeBlinking.play();
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    /*** GETTERS ***/
    public Arc getShape() {
        return pacManShape;
    }
    public int getLives(){
        return lives;
    }
    public int getRadius(){
        return radius;
    }
    public double getX(){
        return x ;
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
