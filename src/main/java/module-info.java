module com.gruppe2.packman {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens com.gruppe2.packman to javafx.fxml;
    exports com.gruppe2.packman;
    exports com.gruppe2.gameCharacters.ghost;
    opens com.gruppe2.gameCharacters.ghost to javafx.fxml;
    exports com.gruppe2.map;
    opens com.gruppe2.map to javafx.fxml;
    exports com.gruppe2.GUI;
    opens com.gruppe2.GUI to javafx.fxml;
    exports com.gruppe2.gameCharacters.pacman;
    opens com.gruppe2.gameCharacters.pacman to javafx.fxml;
    exports com.gruppe2.gameCharacters;
    opens com.gruppe2.gameCharacters to javafx.fxml;
    exports com.gruppe2.utils;
    opens com.gruppe2.utils to javafx.fxml;
}