package com.gruppe2.packman;

import com.gruppe2.ghost.Ghost;
import com.gruppe2.ghost.GhostAI;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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

    public GameManager(){
        setupGameLoop();
    }
    //Setter game UI
    public void setGameUI(GameUI gameUI){
        this.gameUI = gameUI;
        this.pacMan = this.gameUI.gameBoard.getPacMan();
        this.ghostAI = new GhostAI();
    }

    public GameUI getGameUI() {
        return gameUI;
    }

    public void startGame(){
        setGameUI(new GameUI(this));
        gameLoop.start();
    }


    public void checkCollisionWithGhosts() {

    }


    private void nextLevel(){
        GameBoard nextLevel = gameUI.newGameBoard("/level2.txt");
        gameUI.setGameBoard(nextLevel);
    }

    private void resetLevel() {
        GameBoard nextLevel = gameUI.newGameBoard("/level2.txt");
        gameUI.setGameBoard(nextLevel);
    }

    private void gameOver() {
            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Block input events to other windows
            popupStage.initStyle(StageStyle.TRANSPARENT); // Transparent stage

            // Create a label for the message
            Label messageLabel = new Label("GAME OVER - Points" + points);
            messageLabel.setTextFill(Color.GREEN); // Set text color to green

            // Create a StackPane as the root for the scene
            StackPane root = new StackPane(messageLabel);
            root.setStyle("-fx-background-color: black;"); // Set background color to black
            Scene scene = new Scene(root, 300, 100); // Set size of the popup
            scene.setFill(Color.TRANSPARENT); // Make scene background transparent
            popupStage.setScene(scene);
            // Center the popupStage based on the main window
            Stage primaryStage = getPrimaryStage() /* Get your primary stage here */;
            popupStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - 150); // Adjust the popup position
            popupStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - 50);
            // Show the popup
            popupStage.show();
            // Use PauseTransition to hide and close the popup after 3 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(6));
            delay.setOnFinished(event -> {popupStage.close(); resetLevel();});
            delay.play();
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
        if (!gameUI.getGameBoard().willCollide(nextX, nextY)) {
            pacMan.move(nextX, nextY); // Flytter pacman bare hvis det er godkjent move.
            Tablet consumedTablet = gameUI.getGameBoard().willEatTablet(pacMan.getShape().getCenterX(), pacMan.getShape().getCenterY());
            if (consumedTablet != null) { // dot er blitt spist
                if(consumedTablet instanceof EnergyTablet){
                    pacMan.setEnergizerActive();
                }else{

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

    private void addPoint(int point){
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


    //oppdaterer posisjonen til pacMan

    private void updateGame(long now) {
        if(pacMan.getLives() == 0){
            gameOver();
            return;
        }
        pacMan.updatePosition();

        // Convert 'now' from nanoseconds to milliseconds for easier comparison
        if (lastUpdate == 0 || (now - lastUpdate) / 1_000_000 > 30) { // More than 500 ms since last chase
            for(Ghost ghost:gameUI.gameBoard.getGhostList()){
                ghost.chase();
            }
            lastUpdate = now; // Update the last chase time
        }
    }

    // håndterer keyPress
    public void handleKeyPress( KeyCode keyCode) {
        movePacMan(keyCode);
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
}