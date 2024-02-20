package com.gruppe2.packman;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/*
Author: Borgar Flaen Stensrud
Usage: Denne klassen bygger GUI til spillet. har metoder for å returnere GameBoard.
samt oppdatere poeng og handleKeyPressed.
 */
public class GameUI extends VBox{
    GameManager gameManager;
    GameBoard gameBoard;
    private Label points;
    private Paint scoreColor = Color.BLUE;
    public GameUI(GameManager gameManager){
        this.gameManager = gameManager;
        this.gameBoard = new GameBoard("/level1.txt");
        this.setFocusTraversable(true);
        this.requestFocus();
        this.setOnKeyPressed(this::handleKeyPressed);
        this.getChildren().add(gameBoard);
        this.points = new Label("0");
        points.setTextFill(scoreColor);
        points.setFont(Font.font(25));
        this.getChildren().add(points);
    }

    public void refreshPoints(String pointUpdate){
        this.getChildren().remove(points);
        points.setText(pointUpdate);
        this.getChildren().add(points);
    }


    private void handleKeyPressed(KeyEvent event) {
        gameManager.handleKeyPress(event.getCode());
    }

    public void addGameElement(javafx.scene.Node element) {
        this.getChildren().add(element);
    }

    public void updateScore(){

    }





    public GameBoard getGameBoard(){
        return gameBoard;
    }
}
