package com.gruppe2.ghost;

import com.gruppe2.ghost.Ghost;
import com.gruppe2.packman.GameBoard;
import com.gruppe2.packman.GameManager;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Pinky extends Ghost {

    public Pinky( double x, double y, Image ghost, GameBoard gameBoard, GameManager gm ) {
        super(x, y, ghost, gameBoard, 4, gm);
    }


    @Override
    public void chase() {

    }

    @Override
    public void stopFleeing() {
        chase();
    }
}
