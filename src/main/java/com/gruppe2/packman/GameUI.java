package com.gruppe2.packman;
import com.gruppe2.GUI.PopUp;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
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
public class GameUI extends StackPane {
    GameManager gameManager;
    GameBoard gameBoard;

    private int pointsInt;
    private Paint scoreColor = Color.BLUE;
    private HBox pacmansHBox;

    private StackPane mainPane;

    private Button resetButton;

    private PopUp resetPopup;

    private BorderPane root;
    private Label points;
    private HBox livesBox;
    public GameUI(GameManager gameManager){

        this.gameManager = gameManager;
        this.gameBoard = gameManager.getGameBoard();

        root = new BorderPane();


        HBox center = new HBox();
        center.getChildren().add(gameBoard);
        center.setAlignment(Pos.CENTER);
        root.setTop(center);

        root.setPadding(new Insets(30, 30, 30, 30));

        // GUI elements
        gui();

        // Focusable for key events
        this.setFocusTraversable(true);
        this.setOnKeyPressed(this::handleKeyPressed);

        this.getChildren().add(root);

    }

    public void hidePopup() {
        if (resetPopup != null) {
            this.getChildren().remove(resetPopup);
            resetPopup = null; // Clear the reference if you won't need it anymore
        }
    }

    public void setResetPopup(PopUp resetPopup) {
        this.resetPopup = resetPopup;
        this.getChildren().add(resetPopup);
    }


    public void gui(){
        // Points display
        points = new Label("0 points");
        points.setTextFill(scoreColor);
        points.setFont(Font.font(25));

        // Lives display setup
        livesBox = new HBox(10); // Spacing between each pac-man icon
        updateLivesDisplay();

        HBox center = new HBox();
        center.getChildren().addAll(points, livesBox);
        center.setSpacing(20);
        // Top UI setup
        HBox topUI = new HBox();
        topUI.setAlignment(Pos.CENTER);
        topUI.getChildren().add(center);
        topUI.setPadding(new Insets(20, 10, 10, 10));
        // Setting the top UI to the top of the BorderPane
        root.setCenter(topUI);
    }
    private void updateLivesDisplay() {
        // Clear previous lives display
        livesBox.getChildren().clear();
        // Add pac-man lives icons to the HBox
        for (int i = 0; i < gameBoard.getPacMan().getLives(); i++) {
            PacMan pacMan = new PacMan(5,0, 15, gameManager);
            livesBox.getChildren().add(pacMan.getShape());
        }
    }


    public void removePacMan() {
        updateLivesDisplay();
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
        points.setText(pointUpdate);
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
