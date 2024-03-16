package com.gruppe2.packman;

import com.gruppe2.GUI.PopUp;
import com.gruppe2.ghost.Flee;
import com.gruppe2.ghost.Ghost;
import com.gruppe2.ghost.GhostAI;
import com.gruppe2.map.EnergyTablet;
import com.gruppe2.map.Portal;
import com.gruppe2.map.Tablet;
import com.gruppe2.map.Wall;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.List;

import static com.gruppe2.packman.PacManGame.getPrimaryStage;

/*
Author: Borgar Flaen Stensrud
Usage: Denne klassen er en manager for hele spillet. den starter game loop og den setter GUI.
Denne klassen håndterer også bevegelse av Pacman
 */

public class GameManager {
    private PacMan pacMan;
    private GameUI gameUI;
    private AnimationTimer gameLoop;
    private int points = 0;
    private GhostAI ghostAI;
    private int lives = 3;
    private long lastUpdate = 0;
    private Flee flee;
    private List<Portal> portals;
    private List<Ghost> ghosts;
    private boolean ghostOnFlee = false;
    private List<Wall> walls;
    private List<Tablet> tablets;
    private GameBoard gameBoard;
    private StackPane mainPane;
    private Button restartButton;

    private String currentLevel = "/level3.txt";

    private boolean resetIsActive = false;

    public GameManager(){
        this.gameBoard = new GameBoard(currentLevel, this);
        this.mainPane = new StackPane();
        this.mainPane.getChildren().add(gameBoard);
        this.restartButton = new Button("Restart");
        loadGameBoardObjects();
        this.gameUI = new GameUI(this);
    }

    public void loadGameBoardObjects(){
        this.ghosts = gameBoard.getGhostList();
        this.pacMan = gameBoard.getPacMan();
        this.ghostAI = new GhostAI();
        this.portals = gameBoard.getPortals();
        this.walls = gameBoard.getWalls();
        this.tablets = gameBoard.getTablets();
    }


    public StackPane getMainPane(){
        return mainPane;
    }
    public void initGameManager(){
        setupGameLoop();
        startGame();
    }

    public void hidePopup(){
        gameUI.hidePopup();
    }

    //Setter game UI
    public void setGameUI(GameUI gameUI){
        this.gameUI = gameUI;
    }

    public GameBoard getGameBoard(){
        return gameBoard;
    }

    public GameUI getGameUI() {
        return gameUI;
    }

    public void setGhostFlee(){
        ghostOnFlee = !ghostOnFlee;
        if(ghostOnFlee){
        for ( Ghost ghost : ghosts) {
            ghost.flee();
        }}else{
        for (Ghost ghost : ghosts) {
            ghost.stopFleeing();
        }}
    }

    public void startGame(){
        gameLoop.start();
    }

    private void nextLevel(){
        GameBoard nextLevel = gameUI.newGameBoard("/level2.txt");
        gameUI.setGameBoard(nextLevel);
    }

    public void resetLevel() {
        hidePopup();
        GameBoard nextLevel = new GameBoard(currentLevel, this);
        this.gameBoard = nextLevel;
        loadGameBoardObjects();
        this.gameUI = new GameUI(this);

        resetIsActive = false;

    }

    private void gameOver() {
        // Create a new stage for the popup
        PopUp resetPopup = new PopUp("Game Over - you got " + points + " points" + " \n Press enter to restart", restartButton, this);
        gameUI.setResetPopup(resetPopup);
        resetIsActive = true;

    }



    public void movePacMan(KeyCode keyCode) {

        double nextX = pacMan.getX();
        double nextY = pacMan.getY();

        switch (keyCode) {
            case W: nextY = nextY - pacMan.getSpeed(); break;
            case S: nextY = nextY + pacMan.getSpeed(); break;
            case A: nextX = nextX - pacMan.getSpeed(); break;
            case D: nextX = nextX + pacMan.getSpeed(); break;
        }

        // Example of how to implement tunnel logic in your game loop or Pac-Man's move method
        if (nextX < 0) { // Left edge
            pacMan.move(gameUI.gameBoard.getWidth() - pacMan.getRadius() * 2, nextY); // Teleport to the right edge
        } else if (gameUI.gameBoard.getWidth() > pacMan.getRadius()) { // Right edge
            pacMan.getRadius(); // Teleport to the left edge
        }

        // wall detection
        if (!willCollide(nextX, nextY)) {
            pacMan.move(nextX, nextY);
            teleportPacMan(nextX, nextY);
            Tablet consumedTablet = willEatTablet(pacMan.getShape().getCenterX(), pacMan.getShape().getCenterY());
            if (consumedTablet != null) {
                if(consumedTablet instanceof EnergyTablet ){
                    pacMan.setEnergizerActive();
                }

                gameUI.getGameBoard().removeTablet(consumedTablet); // fjerner dot

                addPoint(consumedTablet.getPoints()); // leger til poeng for spist dot
                String score = Integer.toString(points); // ny score til string.
                gameUI.refreshPoints(score); // oppdaterer score

                //check number of tablets left.
                if(gameUI.getGameBoard().getTablets().size() < 1){
                    nextLevel();
                    gameUI.refreshPoints("0");
                }

            }
        }

    }

    public void addPoint(int point){
        points += point;
    }

    // setter opp game loop for å oppdatere skjerm bassert på kalkulasjoner og algoritmer.
    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Game logic to be executed in each frame
                updateGame(now);
            }
        };
    }

    private void flee(){
        flee.fleeFromPacman();
    }


    //oppdaterer posisjonen til pacMan

    private void updateGame(long now) {
        if(pacMan.getLives() == 0){
            gameOver();
            return;
        }
        pacMan.updatePosition();

        // Convert 'now' from nanoseconds to milliseconds for easier comparison
        if (lastUpdate == 0 || (now - lastUpdate) / 1_000_000 > 30) { // More than 500 ms since last chase
            if (this.ghosts != null) {
                for ( Ghost ghost : ghosts ) {
                    if ( !pacMan.getActive() ) {
                        ghost.setSpeed(3);
                        ghost.chase();
                    } else {
                        ghost.flee();
                    }
                }
            }
            lastUpdate = now; // Update the last chase time
        }
    }

    // håndterer keyPress
    public void handleKeyPress( KeyCode keyCode) {
        if (keyCode == KeyCode.ENTER && resetIsActive) {
            resetLevel();
        }else {
            movePacMan(keyCode);
        }
        }

    //Håndterer keyRelease
    public void handleKeyRelease(KeyCode keyCode) {
        switch (keyCode) {
            case W:
            case S:
            case A:
            case D:
                pacMan.setDx(0);
                pacMan.setDy(0);
                break;
            default:
                break;
        }
    }
    public void stopGame() {
        gameLoop.stop();
    }



    //Game Board Methods...
    public Portal findPortal( double x, double y) {
        for ( Portal portal : portals){
            if(intersects(portal.getPortalShape(), x, y, pacMan.getRadius())){
                return portal;
            }
        }
        return null;
    }

    private boolean intersects( Rectangle portal, double centerX, double centerY, double radius) {
        double leftX = centerX - radius;
        double topY = centerY - radius;
        return portal.getBoundsInParent().intersects(leftX, topY, 2 * radius, 2 * radius);
    }

    public void teleportPacMan(double x, double y) {
        Portal foundPortal = findPortal(x , y );

        if (foundPortal != null) {
            Rectangle newPortal = null;
            for(Portal aPortal: portals){
                if(!foundPortal.equals(aPortal)){
                    newPortal = aPortal.getPortalShape();
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


    //**** Wall methods ***/
    public boolean hasWall(double x, double y) {
        return walls.stream().anyMatch(wall -> {
            // Assuming each wall's position can be determined for comparison
            return wall.getWallShape().getBoundsInParent().contains(x, y);
        });
    }


    //Kolisjons test, tester om pacman vill kolidere på gitte kordinater.
    public boolean willCollide(double nextX, double nextY) {
        Bounds pacManBounds = new BoundingBox(nextX - pacMan.getRadius(), nextY - pacMan.getRadius(),
                2 * pacMan.getRadius(), 2 * pacMan.getRadius());

        for ( Wall wall : walls) {
            if (wall.getWallShape().getBoundsInParent().intersects(pacManBounds)) {
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
        for (Wall wall : walls) {
            if (wall.getWallShape().getBoundsInParent().intersects(newMinX, newMinY, ghostWidth, ghostHeight)) {
                return true; // Collision detected
            }
        }

        for (Portal portal: portals) {
            if (portal.getPortalShape().getBoundsInParent().intersects(newMinX, newMinY, ghostWidth, ghostHeight)) {
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

    public boolean willEatGhost(){
        if (this.ghosts != null) {

            double centerX = pacMan.getShape().getCenterX();
            double centerY = pacMan.getShape().getCenterY();
            double radius = pacMan.getRadius();


            // Calculate the top-left corner (minX, minY) and the size (width, height) of the bounding box
            double minX = centerX - radius;
            double minY = centerY - radius;
            double width = 2 * radius; // Diameter is twice the radius
            double height = 2 * radius; // Diameter is twice the radius

            Bounds pacManBounds = new BoundingBox(minX, minY, width, height);

            for ( Ghost ghost : ghosts ) {
                ImageView ghostShapeView = ghost.ghostShape;
                Bounds ghostBounds = new BoundingBox(ghostShapeView.getX()-10, ghostShapeView.getY()-10, ghostShapeView.getFitWidth()+5, ghostShapeView.getFitHeight()+5);

                if (ghostBounds.intersects(pacManBounds)){
                   return true;
                }
            }
            return false;
        }
        return false;
    }


}