package com.gruppe2.packman;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameUI extends Pane {
    GameManager gameManager;
    public GameUI(GameManager gameManager){
        this.gameManager = gameManager;
        this.gameManager.setGameUI(this);

        this.setFocusTraversable(true);
        this.requestFocus();

        this.setOnKeyPressed(this::handleKeyPressed);


    }

    private void handleKeyPressed(KeyEvent event) {
        gameManager.handleKeyPress(event.getCode());
    }

    public void addGameElement(javafx.scene.Node element) {
        this.getChildren().add(element);
    }
}
