package com.gruppe2.GUI;

import com.gruppe2.utils.GameManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * @Author: Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @Usage: Dette er in-game menyen
 * her kan man restarte spillet eller gå tilbake til hovedmenyen
 */
public class TopMenu extends BorderPane {
    private GameManager gm;
    public TopMenu( GameManager gm ) {
        this.gm = gm;

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        MenuItem menuOption = new MenuItem("Menu");
        menuOption.setOnAction(event -> {
            gm.showMainMenuAddToScene();
        });

        MenuItem restartOption = new MenuItem("Restart");
        restartOption.setOnAction(event -> {
            gm.resetLevel();
        });

        fileMenu.getItems().addAll(menuOption, restartOption);

        menuBar.getMenus().add(fileMenu);

        this.setTop(menuBar);
    }

}
