package com.gruppe2.GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ButtonController {
    @FXML
    private Button restartButton;

    @FXML
    protected void handleButtonClick( ) {
        System.out.println("Button clicked");
        // Any additional logic for when the button is clicked
    }
}
