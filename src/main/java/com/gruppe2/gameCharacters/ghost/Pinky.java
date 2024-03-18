package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.utils.GameManager;
import javafx.scene.image.Image;

public class Pinky extends Ghost {

    public Pinky( double x, double y,  GameManager gm ) {
        super(x, y, 4, gm, 10);
        this.setImage(new Image(getClass().getResourceAsStream("/pinky.png")));
    }

    @Override
    public void chase() {
        if(this.lineOfSightToPacMan()){
            moveToPacManDirectly();
        }else {
            this.getMoveType().move();
        }
    }

}
