package com.gruppe2.packman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PacManGame extends Application {

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start( Stage primaryStage ) {
        try {
            GameManager gameManager = new GameManager();
            gameManager.startGame();
            Scene scene = new Scene(gameManager.getGameUI(), 1000, 600);
            scene.setOnKeyPressed(event -> gameManager.handleKeyPress(event.getCode()));
            scene.setOnKeyReleased(event -> gameManager.handleKeyRelease(event.getCode()));
            primaryStage.setTitle("Pac-man Game");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
}