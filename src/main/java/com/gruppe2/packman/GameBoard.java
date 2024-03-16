package com.gruppe2.packman;

import com.gruppe2.ghost.*;
import com.gruppe2.map.*;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/*
Author: Borgar Flaen Stensrud
Usage: Lager gameboard til skjermen. Klasse for å tegne det grafiske på spillet. Tegner pacman, vegger og dotter. samt energizers.
 */

public class GameBoard extends Pane {
    private final int tileSize = 20;
    private PacMan pacMan;
    private List<Wall> walls;
    private List<Circle> dots = new ArrayList<>();
    private List<EnergyTablet> energyTablets = new ArrayList<>();
    private List<SmallTablet> smallTablets = new ArrayList<>();
    private List<Tablet> tablets = new ArrayList<>();
    private Ghost blinky;
    private GameManager gm;
    private List<Ghost> ghosts;
    private List<Portal> portals;

    public GameBoard(String levelPath, GameManager gm){
        this.gm = gm;
        pacMan = new PacMan(50, 50, gm);
        ghosts = new ArrayList<>();
        portals = new ArrayList<>();
        walls = new ArrayList<>();
        loadLevel(levelPath);
        addPortals();
        addGhosts();
    }

    private void addGhosts(){
        for(Ghost ghost: ghosts){
            this.getChildren().add(ghost.getShape());
        }
    }

    public List<Tablet> getTablets(){
        return tablets;
    }
    // Laster level basert på tekstfil
    private void loadLevel(String levelPath){
        try ( InputStream is = getClass().getResourceAsStream(levelPath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int y = 0;
            String line;
            while((line =  br.readLine()) != null){
                for(int x = 0; x < line.length(); x++){
                    char c = line.charAt(x);
                    switch(c){
                        case '#':
                            Wall wall = new Wall(x*tileSize, y*tileSize, tileSize, tileSize);
                            this.getChildren().add(wall.getWallShape());
                            walls.add(wall);
                            break;
                        case '.':
                            SmallTablet smallTablet = new SmallTablet(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, tileSize / 4.0);
                            smallTablets.add(smallTablet);
                            tablets.add(smallTablet);
                            this.getChildren().add(smallTablet.getShape());
                            break;
                        case 'o':
                            EnergyTablet energyTablet = new EnergyTablet(x*tileSize + tileSize/2.0, y*tileSize + tileSize/2.0);
                            energyTablets.add(energyTablet);
                            tablets.add(energyTablet);
                            this.getChildren().add(energyTablet.getShape());
                            break;
                        case 'P':
                            pacMan.move(x * tileSize + tileSize  / 2.0, y * tileSize + tileSize / 2.0);
                            this.getChildren().add(pacMan.getShape());
                            break;
                        case 'B':
                            Image ghostBlinky = new Image(getClass().getResourceAsStream("/blinky.png"));
                            blinky = new Blinky(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, ghostBlinky, pacMan, this, gm);
                            ghosts.add(blinky);
                            break;
                        case 'p':
                            Image ghostPinky = new Image(getClass().getResourceAsStream("/pinky.png"));
                            Pinky pinky;
                            pinky = new Pinky(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, ghostPinky, this, gm);
                            ghosts.add(pinky);
                            break;
                        case 'I':
                            Image ghostInky = new Image(getClass().getResourceAsStream("/inky.png"));
                            Inky inky;
                            inky = new Inky(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, ghostInky, this, gm);
                            ghosts.add(inky);
                            break;
                        case 'C':
                            Image ghostClyde = new Image(getClass().getResourceAsStream("/clyde.png"));
                            Clyde clyde;
                            clyde = new Clyde(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, ghostClyde, this, gm);
                            ghosts.add(clyde);
                            break;
                        case '*':
                            Portal newPortal = new Portal(x*tileSize, y*tileSize, tileSize, this.getBoundsInParent().getWidth(), this.getBoundsInParent().getHeight());
                            portals.add(newPortal);
                            break;
                    }
                }
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Ghost> getGhostList(){
        return ghosts;
    }

    public double getTilesize(){
        return tileSize;
    }

    public List<Wall> getWalls(){
        return walls;
    }
    public List<Portal> getPortals(){
        return portals;
    }

    public void addPortals(){
        for(Portal portal:portals){
            this.getChildren().add(portal.getPortalShape());
        }
    }

    //fjerner tablet fra GameBoard
    public void removeTablet(Tablet tablet){
        this.getChildren().remove(tablet.getShape());
        this.tablets.remove(tablet);
        dots.remove(tablet); // Assuming 'dots' is a List<Circle> of all dot objects
    }

    public Ghost getBlinky(){
        return blinky;
    }


    //returnerer pacman
    public PacMan getPacMan(){
        return pacMan;
    }



}
