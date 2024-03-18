package com.gruppe2.GUI;
import com.gruppe2.map.GameBoard;
import com.gruppe2.utils.GameManager;
import com.gruppe2.gameCharacters.pacman.PacMan;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
* @Author: Borgar Flaen Stensrud
* @Usage: Denne klassen bygger GUI til spillet. har metoder for å returnere GameBoard.
*  samt oppdatere poeng og handleKeyPressed.
 */
public class GameUI extends StackPane {
    GameManager gameManager;
    GameBoard gameBoard;

    private BorderPane root;
    private Label points;
    private Paint scoreColor = Color.BLUE;
    private HBox livesBox;
    private PopUp resetPopup;
    public GameUI(GameManager gameManager){
        this.gameManager = gameManager;
        this.gameBoard = gameManager.getGameBoard();

        root = new BorderPane();

        setTitle();

        setGameBoard(gameBoard);

        root.setPadding(new Insets(30, 30, 30, 30));

        // GUI elementer
        gui();

        // fokuserbar for tastetrykk
        this.setFocusTraversable(true);
        this.setOnKeyPressed(this::handleKeyPressed);

        this.getChildren().add(root);

    }

    /*** Setter tittelen på nivået ***/
    public void setTitle() {
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);

        Label title = new Label(gameManager.getCurrentLevel());
        title.setFont(Font.font(30));
        title.setTextFill(Color.BLUE);
        title.setStyle("-fx-font-weight: bold; -fx-start-margin: 10px; -fx-end-margin: 10px; -fx-top-margin: 10px; -fx-bottom-margin: 10px; -fx-text-alignment: center;");

        titleBox.getChildren().add(title);

        root.setTop(titleBox);
    }

    /***
     * Setter en overlay for menyen
     * @param menuOverlay
     */
    public void setMenuOverlay(Node menuOverlay) {
        this.getChildren().clear();
        this.getChildren().add(menuOverlay);
    }
    public void removeMenuOverlay() {
        this.getChildren().clear();
        this.getChildren().add(root);
    }


    /*** setter opp GUI elementer, poeng og liv ***/
    public void gui(){
        // Points display
        points = new Label("0 points");
        points.setTextFill(scoreColor);
        points.setFont(Font.font(25));

        // Lives display setup
        livesBox = new HBox(10);
        updateLivesDisplay();

        HBox center = new HBox();
        center.getChildren().addAll(points, livesBox);
        center.setSpacing(20);

        // Top UI setup
        HBox topUI = new HBox();
        topUI.setAlignment(Pos.CENTER);
        topUI.getChildren().add(center);
        topUI.setPadding(new Insets(20, 10, 10, 10));

        // setter top UI til bunn av BorderPane
        root.setBottom(topUI);
    }

    /***
     * Updates the lives display to show the current amount of lives
     */
    private void updateLivesDisplay() {
        // Clear previous lives display
        livesBox.getChildren().clear();
        // Add pac-man lives icons to the HBox
        for (int i = 0; i < gameManager.getLives(); i++) {
            PacMan pacMan = new PacMan(5,0, 15, gameManager);
            livesBox.getChildren().add(pacMan.getShape());
        }
    }

    public void removePacMan() {
        updateLivesDisplay();
    }

    /*** POPUP METHODS ***/

    // Viser en popup som sier at spillet er klart til å starte på nytt
    public void showResetLevelPreparePopup() {
        PopUp popUp = new PopUp("Forbered på nivå", "Neste nivå starter straks", gameManager);
        this.getChildren().add(popUp);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            this.getChildren().remove(popUp);

        });
        delay.play();
    }
    // viser en popup som sier at neste nivå starter straks
    public void showLevelPreparePopup() {
        PopUp popUp = new PopUp("Forbered på neste nivå", "Neste nivå starter straks.", gameManager);
        this.getChildren().add(popUp);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            this.getChildren().remove(popUp);

        });
        delay.play();
    }
    public void hidePopup() {
        if (resetPopup != null) {
            this.getChildren().remove(resetPopup);
            resetPopup = null; // klargjør referansen for å ikke trenge den lenger
        }
    }
    public void setResetPopup(PopUp resetPopup) {
        this.resetPopup = resetPopup;
        this.getChildren().add(resetPopup); // legger til popupen
    }


    /*** Setter opp en ny GameBoard ***/
    public void setGameBoard(GameBoard gameBoard){
        this.gameBoard = gameBoard;
        HBox center = new HBox();
        center.getChildren().add(gameBoard);
        center.setAlignment(Pos.CENTER);
        setTitle();
        root.setCenter(null);
        root.setCenter(center);
    }

    /*** Oppdaterer poengene ***/
    public void refreshPoints(String pointUpdate){
        points.setText(pointUpdate);
    }

    /*** Håndterer tastetrykk ***/
    private void handleKeyPressed(KeyEvent event) {
        gameManager.handleKeyPress(event.getCode());
    }

}
