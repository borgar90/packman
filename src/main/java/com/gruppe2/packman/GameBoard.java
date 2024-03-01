package com.gruppe2.packman;

import com.gruppe2.ghost.*;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
    private List<Rectangle> walls = new ArrayList<>();

    private List<Circle> dots = new ArrayList<>();
    private List<EnergyTablet> energyTablets = new ArrayList<>();
    private List<SmallTablet> smallTablets = new ArrayList<>();

    private List<Tablet> tablets = new ArrayList<>();
    private Ghost blinky;
    private GameManager gm;
    private List<ImageView> ghosts;

    private List<Ghost> ghostList;

    public GameBoard(String levelPath, GameManager gm){
        this.gm = gm;
        pacMan = new PacMan(50, 50);
        ghosts = new ArrayList<>();
        ghostList = new ArrayList<>();
        loadLevel(levelPath);
        setGhostShapes();
        addGhosts();
    }
    public List<Ghost> getGhostList(){
        return ghostList;
    }

    public void setGhostShapes(){
        for(Ghost theGhost:ghostList){
            ghosts.add(theGhost.ghostShape);
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
                            Rectangle wall = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
                            wall.setFill(Color.BLUE);
                            this.getChildren().add(wall);
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
                            this.ghostList.add(blinky);


                            break;
                        case 'p':
                            Image ghostPinky = new Image(getClass().getResourceAsStream("/pinky.png"));
                            Pinky pinky;
                            pinky = new Pinky(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, ghostPinky, this, gm);
                            this.ghostList.add(pinky);
                            break;
                        case 'I':
                            Image ghostInky = new Image(getClass().getResourceAsStream("/inky.png"));
                            Inky inky;
                            inky = new Inky(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, ghostInky, this, gm);
                            this.ghostList.add(inky);
                            break;
                        case 'C':
                            Image ghostClyde = new Image(getClass().getResourceAsStream("/clyde.png"));
                            Clyde clyde;
                            clyde = new Clyde(x * tileSize + tileSize / 2.0, y * tileSize + tileSize / 2.0, ghostClyde, this, gm);
                            this.ghostList.add(clyde);
                    }
                }
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addGhosts(){
        for(ImageView ghost : ghosts) {
            this.getChildren().add(ghost);
        }
    }

    public double getTilesize(){
        return tileSize;
    }

    public List<Rectangle> getWalls(){
        return walls;
    }


    public boolean hasWall(double x, double y) {
        // You might need to adjust the logic based on how walls are stored
        // For example, if walls are stored in a boolean array or similar
        return walls.stream().anyMatch(wall -> {
            // Assuming each wall's position can be determined for comparison
            return wall.getBoundsInParent().contains(x, y, x + tileSize, y+tileSize);
        });
    }



    //Kolisjons test, tester om pacman vill kolidere på gitte kordinater.
    public boolean willCollide(double nextX, double nextY) {
        Bounds pacManBounds = new BoundingBox(nextX - pacMan.getRadius(), nextY - pacMan.getRadius(),
                2 * pacMan.getRadius(), 2 * pacMan.getRadius());
        for (Rectangle wall : walls) {
            if (wall.getBoundsInParent().intersects(pacManBounds)) {
                return true; // kollisjon
            }
        }
        return false; // ingen kollisjon
    }

    public boolean ghostWillCollide(double nextX, double nextY, Ghost ghost) {
        Bounds ghostBounds = new BoundingBox(nextX, nextY, ghost.ghostShape.getFitWidth(), ghost.ghostShape.getFitHeight());

        for (Rectangle wall : walls) {
            if (wall.getBoundsInParent().intersects(ghostBounds)) {
                return true; // kollisjon
            }
        }
        return false; // ingen kollisjon
    }


    //Kolisjonstest sjekker om pacman vill spise en dot på gitte kordinater.
    public Tablet willEatTablet(double nextX, double nextY){
        Bounds pacManBounds = new BoundingBox(nextX - pacMan.getRadius(), nextY - pacMan.getRadius(),
                2 * pacMan.getRadius(), 2 * pacMan.getRadius());
        for (Tablet tablet: tablets) {
            if (tablet.getShape().getBoundsInParent().intersects(pacManBounds)) {
                return tablet;
            }
        }
        return null; // No dot eaten
    }



    //fjerner tablet fra GameBoard
    public void removeTablet(Tablet tablet){
        this.getChildren().remove(tablet.getShape());
        this.tablets.remove(tablet);
        dots.remove(tablet); // Assuming 'dots' is a List<Circle> of all dot objects
    }

    public  Ghost getBlinky(){
        return blinky;
    }


    //returnerer pacman
    public PacMan getPacMan(){
        return pacMan;
    }

}
