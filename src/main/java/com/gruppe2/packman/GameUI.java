package com.gruppe2.packman;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static com.gruppe2.packman.PacManGame.getPrimaryStage;

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
        this.gameBoard = new GameBoard("/level1.txt", gameManager);
        this.setFocusTraversable(true);
        this.requestFocus();
        this.setOnKeyPressed(this::handleKeyPressed);
        this.getChildren().add(gameBoard);
        gui();
    }

    public void gui(){
        this.points = new Label("0");
        points.setTextFill(scoreColor);
        points.setFont(Font.font(25));
        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.getChildren().add(points);

        VBox lives = new VBox();
        Label label = new Label("Lives:");
        lives.getChildren().add(label);
        HBox pacmans = new HBox();
        for(int i = gameBoard.getPacMan().getLives(); i > 0; i--){
            PacMan pacMan = new PacMan(5,0, 15);
            pacmans.getChildren().add(pacMan.getShape());
        }
        lives.getChildren().add(pacmans);
        hbox.getChildren().add(lives);
        this.getChildren().add(hbox);

    }

    public void removePacMan() {
        // Retrieve the VBox containing Pac-Man images
        HBox lives = (HBox) ((VBox) ((HBox) this.getChildren().get(1)).getChildren().get(1)).getChildren().get(1);

        // Remove the last Pac-Man image (assuming it's at index 1)
        int numPacMen = lives.getChildren().size();
        if (numPacMen > 0) {
            lives.getChildren().remove(numPacMen - 1);
        }
    }

    public void showLevelPreparePopup() {
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block input events to other windows
        popupStage.initStyle(StageStyle.TRANSPARENT); // Transparent stage

        // Create a label for the message
        Label messageLabel = new Label("Prepare for next level");
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
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> popupStage.close());
        delay.play();
    }
    public void setGameBoard(GameBoard gameBoard){
        this.gameBoard = gameBoard;
    }

    public GameBoard newGameBoard(String newLevel){
        showLevelPreparePopup();
        this.gameBoard = new GameBoard(newLevel, gameManager);
        this.getChildren().remove(0);
        this.getChildren().add(0, gameBoard);
        return gameBoard;
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
