package com.gruppe2.GUI;

import com.gruppe2.utils.GameManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: dette er en klasse som lager en pop-up melding som kan brukes til å vise meldinger til brukeren.
 */
public class PopUp extends StackPane {
    private String title;
    private String message;
    private Button button;
    private GameManager gm;
    public PopUp( String title, Button button, GameManager gm) {
        this.title = title;
        this.button = button;
        this.gm = gm;
        initButtonPopup();
    }
    public PopUp(String title, String message, GameManager gm) {
        this.title = title;
        this.message = message;
        this.gm = gm;
        initMessagePopup();
    }

    private void initButtonPopup() {

        Label messageLabel = new Label(title);
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setStyle("-fx-font-size: 25px; -fx-text-alignment: center;");
        VBox container = new VBox();
        container.getChildren().add(messageLabel);
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER);
        initPane(container);
    }

    private void initMessagePopup(){
        Label messageLabel = new Label(title);
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setStyle("-fx-font-size: 25px; -fx-text-alignment: center;");
        Label message = new Label(this.message);
        message.setTextFill(Color.WHITE);
        message.setStyle("-fx-font-size: 20px; -fx-text-alignment: center;");

        VBox container = new VBox();
        container.getChildren().add(messageLabel);
        container.getChildren().add(message);
        container.setAlignment(Pos.CENTER);

        initPane(container);
    }

    private void initPane(Pane node) {

        VBox root = new VBox();
        root.getChildren().add(node);

        root.setStyle("-fx-background-color: black;");
        root.setAlignment(Pos.CENTER);
        root.setMinHeight(300);
        AnchorPane anchorPane = new AnchorPane();

        anchorPane.getChildren().add(root);

        AnchorPane.setTopAnchor(root, 100.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);

        this.getChildren().add(anchorPane);

    }
}
