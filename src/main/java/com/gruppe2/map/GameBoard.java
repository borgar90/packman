package com.gruppe2.map;

import com.gruppe2.gameCharacters.ghost.*;
import com.gruppe2.gameCharacters.pacman.PacMan;
import com.gruppe2.utils.GameManager;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/*
Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
Usage: Lager gameboard til skjermen. Klasse for å tegne det grafiske på spillet. Tegner pacman, spøkelser, vegger og dotter. samt energizers.
 */

public class GameBoard extends Pane {
    private final int tileSize = 20;
    private PacMan pacMan;
    private List<Wall> walls;
    private List<Circle> dots = new ArrayList<>();
    private List<EnergyTablet> energyTablets = new ArrayList<>();
    private List<SmallTablet> smallTablets = new ArrayList<>();
    private List<Tablet> tablets = new ArrayList<>();
    private GameManager gm;
    private List<Ghost> ghosts;
    private List<Portal> portals;

    /**
     * Oppretter et nytt GameBoard med angitt bane og GameManager.
     *
     * @param levelPath banefilen som skal lastes inn.
     * @param gm        GameManager-objektet som er knyttet til spillet.
     */

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
        String path = "src/main/resources/" + levelPath;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int y = 0;
            String line;
            while((line =  br.readLine()) != null){
                for(int x = 0; x < line.length(); x++){
                    char c = line.charAt(x);
                    double startLocationX = x * tileSize + tileSize  / 2.0;
                    double startLocationY = y * tileSize + tileSize / 2.0;
                    Point2D startLocation = new Point2D(startLocationX, startLocationY);

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
                            pacMan.setStartLocation(startLocation);
                            pacMan.setPosistion(startLocationX,startLocationY);
                            this.getChildren().add(pacMan.getShape());
                            break;
                        case 'B':
                            Blinky blinky;
                            blinky = new Blinky(startLocationX, startLocationY,  gm);
                            blinky.setStartLocation(startLocation);
                            blinky.setMoveType(new MoveAgressive(blinky, pacMan, this));
                            blinky.setPacMan(pacMan);
                            ghosts.add(blinky);
                            break;
                        case 'p':
                            Pinky pinky;
                            pinky = new Pinky(startLocationX, startLocationY,  gm);
                            pinky.setStartLocation(startLocation);
                            ghosts.add(pinky);
                            pinky.setMoveType(new MoveAgressive(pinky, pacMan, this));
                            break;
                        case 'I':
                            Inky inky;
                            inky = new Inky(startLocationX,  startLocationY, gm);
                            inky.setStartLocation(startLocation);
                            inky.setMoveType(new MoveAgressive(inky, pacMan, this));
                            ghosts.add(inky);
                            break;
                        case 'C':
                            Clyde clyde;
                            clyde = new Clyde(startLocationX, startLocationY,  gm);
                            clyde.setStartLocation(startLocation);
                            clyde.setMoveType(new MoveHoming(clyde, pacMan));
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


    // Legger til portaler på brettet
    public void addPortals(){
        for(Portal portal:portals){
            this.getChildren().add(portal.getPortalShape());
        }
    }

    // fjerner tablet fra GameBoard, brukes ved kollisjon mellom pac og tablet,
    // pac får poeng i gameManager-classen og her blir tablet fjernes fra brettet
    public void removeTablet(Tablet tablet){
        this.getChildren().remove(tablet.getShape());
        this.tablets.remove(tablet);
        dots.remove(tablet);
    }



    //* Getters *//

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
    //returnerer pacman
    public PacMan getPacMan(){
        return pacMan;
    }

}
