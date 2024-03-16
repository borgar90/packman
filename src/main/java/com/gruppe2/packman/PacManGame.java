package com.gruppe2.packman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PacManGame extends Application {
    public static Stage primaryStage;
    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start( Stage primaryStage ) {
        try {
            this.primaryStage = primaryStage;
            GameManager gameManager = new GameManager();
            gameManager.initGameManager();
            Scene scene = new Scene(gameManager.getGameUI(), 1000, 800);
            scene.setOnKeyPressed(event -> gameManager.handleKeyPress(event.getCode()));
            scene.setOnKeyReleased(event -> gameManager.handleKeyRelease(event.getCode()));

            primaryStage.setTitle("Pac-man Game");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception ex){
            System.out.println(ex);
        }
    }



    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}