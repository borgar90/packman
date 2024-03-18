package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.utils.GameManager;
import javafx.scene.image.Image;


public class Inky extends Ghost {


    public Inky( double x, double y,  GameManager gm ) {
        super(x, y, 4, gm, 8);
        this.setImage(new Image(getClass().getResourceAsStream("/inky.png")));
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
