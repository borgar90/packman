package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.GameCharacter;
import com.gruppe2.packman.GameManager;
import com.gruppe2.packman.PacMan;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Ghost extends GameCharacter {
    public ImageView ghostShape;
    protected double x, y;
    protected final double width = 16;
    protected final double height = 16;

    protected GameBoard gameBoard;

    private double speed;
    private double initialX;
    private double initialY;
    protected GameManager gm;
    private Flee flee;

    public Ghost( double x, double y, Image ghost, GameBoard gameBoard, double speed, GameManager gm ) {
        this.gm = gm;
        this.x = x - width /2 ;
        this.y = y - height / 2;
        this.initialX = this.x;
        this.initialY = this.y;
        this.gameBoard = gameBoard;
        ghostShape = new ImageView(ghost);
        ghostShape.setFitWidth(width);
        ghostShape.setFitHeight(height);
        ghostShape.setX(this.x);
        ghostShape.setY(this.y);
        ghostShape.setPreserveRatio(false);
        this.speed = speed;
        this.flee = new Flee(this, gameBoard);
    }

    public double getWidth(){
        return width;
    }

    public ImageView getShape() {
        return ghostShape;
    }
    public void move(double dx, double dy) {
        if(!this.gm.ghostWillCollide(dx, dy, this) ){
            x = dx;
            y = dy;
            ghostShape.setX(x);
            ghostShape.setY(y);
            checkCollide();
        }
    }
    protected double getX(){
        return x;
    }

    protected double getY(){
        return y;
    }

    protected double getSpeed(){
        return speed;
    }

    public abstract void chase();

    public void flee(){
        flee.fleeFromPacman();
    }

    public abstract void stopFleeing();

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void checkCollide() {
        double pacManX = gameBoard.getPacMan().getX();
        double pacManY = gameBoard.getPacMan().getY();

        double distance = Math.sqrt(Math.pow(x - pacManX, 2) + Math.pow(y - pacManY, 2));

        double collisionThreshold = gameBoard.getPacMan().getRadius() + 2;
        if (gm.willEatGhost()) {
            gameBoard.getPacMan().loseLife();
            gm.getGameUI().removePacMan();
            resetPosition();
        }
    }

    public void resetPosition() {
        // Reset the ghost's position to its initial position
        move(initialX, initialY); // assuming initialX and initialY represent the ghost's initial position
    }
}
