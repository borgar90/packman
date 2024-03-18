package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.utils.GameManager;
import javafx.scene.image.Image;



public class Clyde extends Ghost {

    public Clyde( double x, double y, GameManager gm) {
        super(x, y,3, gm, 7);
        this.setImage(new Image(getClass().getResourceAsStream("/clyde.png")));
    }

    @Override
    public void chase() {
        if(super.lineOfSightToPacMan()){
            super.moveToPacManDirectly();
        }else {
            super.getMoveType().move();
        }
    }

}
