package com.gruppe2.packman;

import com.gruppe2.utils.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PacManGame extends Application {
    public static Stage primaryStage;
    public static Scene scene;
    private static Stage stage;
    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start( Stage primaryStage ) {
        try {
            stage = primaryStage;
            GameManager gameManager = new GameManager();

            scene = new Scene(gameManager.getGameUI(), 1000, 800);
            scene.setOnKeyPressed(event -> gameManager.handleKeyPress(event.getCode()));
            scene.setOnKeyReleased(event -> gameManager.handleKeyRelease(event.getCode()));

            primaryStage.setTitle("Pac-man Game");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception ex){
            System.out.println(ex);
        }
    }


    /*** statisk metode for å sette en ny scene i stage
     * @param newScene ny scene som skal settes
     * @param gameManager gameManager som skal brukes til å håndtere keyEvents
     */
    public static void setScene(Scene newScene, GameManager gameManager) {
        scene = newScene;
        scene.setOnKeyPressed(event -> gameManager.handleKeyPress(event.getCode()));
        scene.setOnKeyReleased(event -> gameManager.handleKeyRelease(event.getCode()));
        stage.setScene(scene);
    }

    /*** statisk metode for å hente primaryStage
     * @return primaryStage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}