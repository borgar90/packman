package com.gruppe2.packman;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

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
    public GameManager(){
        setupGameLoop();
    }

    //Setter game UI
    public void setGameUI(GameUI gameUI){
        this.gameUI = gameUI;
        this.pacMan = this.gameUI.gameBoard.getPacMan();
    }

    public GameUI getGameUI() {
        return gameUI;
    }

    // For å starte spillet
    public void startGame(){
        setGameUI(new GameUI(this));
        gameLoop.start();
    }

    // Håndtering for å flytte pacman samt spising av poeng. Også wall detection
    public void movePacMan(KeyCode keyCode) {
        double nextX = pacMan.getX();
        double nextY = pacMan.getY();

        switch (keyCode) {
            case W: nextY = nextY - pacMan.getSpeed(); break;
            case S: nextY = nextY + pacMan.getSpeed(); break;
            case A: nextX = nextX - pacMan.getSpeed(); break;
            case D: nextX = nextX + pacMan.getSpeed(); break;
        }

        // wall detection
        if (!gameUI.getGameBoard().willCollide(nextX, nextY)) {
            pacMan.move(nextX, nextY); // Flytter pacman bare hvis det er godkjent move.
            Circle eatenDot = gameUI.getGameBoard().willEatDot(pacMan.getShape().getCenterX(), pacMan.getShape().getCenterY()); // doten pacman evt spiser
            if (eatenDot != null) { // dot er blitt spist
                gameUI.getGameBoard().removeDot(eatenDot); // fjerner dot
                addPoint(); // leger til poeng for spist dot
                String score = Integer.toString(points); // ny score til string.
                gameUI.refreshPoints(score); // oppdaterer score
            }
        }

    }

    private void addPoint(){
        points += 1;
    }

    // setter opp game loop for å oppdatere skjerm bassert på kalkulasjoner og algoritmer.
    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Game logic to be executed in each frame
                updateGame();
            }
        };
    }

    //oppdaterer posisjonen til pacMan
    private void updateGame() {
        pacMan.updatePosition();
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