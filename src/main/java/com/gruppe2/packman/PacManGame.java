package com.gruppe2.packman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PacManGame extends Application {

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start( Stage primaryStage ) {
        try {
            GameBoard gameBoard = new GameBoard("/level1.txt");
            Scene scene = new Scene(gameBoard, 1000, 600);
            primaryStage.setTitle("Pac-man Game");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
}