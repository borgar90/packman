package com.gruppe2.ghost;

import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.GameManager;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Clyde extends Ghost {

    private MoveHoming homing;
    public Clyde( double x, double y, Image ghost, GameBoard gameBoard, GameManager gm) {
        super(x, y, ghost, gameBoard, 3, gm);
        this.homing = new MoveHoming(this, gameBoard.getPacMan(), gameBoard);
    }

    @Override
    public void chase() {
        homing.move();
    }

    @Override
    public void stopFleeing() {
        homing.move();
    }
}
