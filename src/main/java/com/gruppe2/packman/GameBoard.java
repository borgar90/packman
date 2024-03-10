package com.gruppe2.packman;

import com.gruppe2.ghost.*;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


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

    private List<Rectangle> portals;


    public GameBoard(String levelPath, GameManager gm){
        this.gm = gm;
        pacMan = new PacMan(50, 50, this);
        ghosts = new ArrayList<>();
        ghostList = new ArrayList<>();
        portals = new ArrayList<>();
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
                        case '-':
                            if(portals.size() < 1) {
                                teleportAdd(y * tileSize);
                            }
                    }
                }
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teleportAdd(double y){
            double paneWidth = this.getBoundsInParent().getWidth(); // Assuming this method is called after the pane is laid out
            Rectangle entryPortal = new Rectangle(0, y, tileSize, tileSize); // Assuming square portals
            Rectangle exitPortal = new Rectangle(paneWidth - tileSize, y, tileSize, tileSize); // Positioned at the opposite end
            this.portals.add(entryPortal);
            this.getChildren().add(entryPortal);
            this.portals.add(exitPortal);
            this.getChildren().add(exitPortal);
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

    public Rectangle findPortal( double x, double y) {
        for ( Rectangle portal : portals){
            if(intersects(portal, x, y, pacMan.getRadius())){
                return portal;
            }
        }
        return null;
    }
    private boolean intersects(Rectangle portal, double centerX, double centerY, double radius) {
        double leftX = centerX - radius;
        double topY = centerY - radius;
        return portal.getBoundsInParent().intersects(leftX, topY, 2 * radius, 2 * radius);
    }
    public void teleportPacMan(double x, double y) {
        Rectangle foundPortal = findPortal(x , y );

        if (foundPortal != null) {
            Rectangle portal = foundPortal;
            Rectangle newPortal = null;
            for(Rectangle aPortal: portals){
                if(portal != aPortal){
                    newPortal = aPortal;
                }
            }
            if (newPortal != null) {
                double newX = newPortal.getX();

                if(newX == 0){
                    newX+= 21;
                }else{
                    newX-=1;
                }

                double newY = newPortal.getY() + newPortal.getHeight() / 2;

                pacMan.move(newX, newY);
            }
        }
    }

    public boolean hasWall(double x, double y) {
        return walls.stream().anyMatch(wall -> {
            // Assuming each wall's position can be determined for comparison
            return wall.getBoundsInParent().contains(x, y);
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
        // Get current bounds of the ghost
        Bounds ghostBounds = ghost.getShape().getBoundsInParent();

        // Calculate the ghost's width and height
        double ghostWidth = ghostBounds.getWidth();
        double ghostHeight = ghostBounds.getHeight();

        // Assuming nextX and nextY represent the new top-left corner of the ghost,
        // calculate new bounds considering the ghost's width and height
        double newMinX = nextX;
        double newMinY = nextY;

        // Now, you'll need to check if these new bounds would collide with any walls or obstacles
        // This will depend on how your game's collision detection is structured.
        // For illustration, you might check against walls like so:
        for (Rectangle wall : walls) {
            if (wall.getBoundsInParent().intersects(newMinX, newMinY, ghostWidth, ghostHeight)) {
                return true; // Collision detected
            }
        }

        for (Rectangle portal: portals) {
            if (portal.getBoundsInParent().intersects(newMinX, newMinY, ghostWidth, ghostHeight)) {
                return true; // Collision detected
            }
        }


        return false; // No collision detected
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


    public void willEatGhost(){
        for(Ghost ghost : ghostList){
            if(pacMan.getShape().getBoundsInParent().intersects(ghost.getShape().getBoundsInParent())){
                ghost.resetPosition();
                gm.addPoint(10);
            }
        }
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
