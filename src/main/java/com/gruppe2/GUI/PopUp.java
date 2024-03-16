package com.gruppe2.GUI;

import com.gruppe2.packman.GameManager;
import com.gruppe2.packman.Node;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static com.gruppe2.packman.PacManGame.getPrimaryStage;

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
        Label message = new Label(this.message);
        messageLabel.setTextFill(Color.GREEN);

        VBox container = new VBox();
        container.getChildren().add(messageLabel);
        container.getChildren().add(message);

        initPane(container);
    }

    private void initPane(Pane node) {
        // Create the VBox container for your node
        VBox root = new VBox();
        root.getChildren().add(node); // Add your node to the VBox
        root.setStyle("-fx-background-color: black;");
        root.setAlignment(Pos.CENTER); // Align content to center
        root.setMinHeight(300);
        AnchorPane anchorPane = new AnchorPane();

        anchorPane.getChildren().add(root);

        AnchorPane.setTopAnchor(root, 100.0); // 300 pixels from the top
        AnchorPane.setLeftAnchor(root, 0.0); // Align left edge to parent
        AnchorPane.setRightAnchor(root, 0.0); // Align right edge to parent

        this.getChildren().add(anchorPane);


    }


}
