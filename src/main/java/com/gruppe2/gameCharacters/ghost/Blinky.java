package com.gruppe2.gameCharacters.ghost;

import com.gruppe2.utils.GameManager;
import com.gruppe2.gameCharacters.pacman.PacMan;
import javafx.scene.image.Image;
import java.util.Random;

public class Blinky extends Ghost {
    private PacMan pacMan;
    private Random rand = new Random();


    public Blinky( double x, double y, GameManager gm ) {
        super(x, y,3, gm, 6);
        this.setImage(new Image(getClass().getResourceAsStream("/blinky.png")));
    }

    @Override
    public void chase() {
        if(this.lineOfSightToPacMan()){
            moveToPacManDirectly();
        }else {
            this.getMoveType().move();
        }
    }

    public void setPacMan(PacMan pacMan){
        this.pacMan = pacMan;
    }




}
