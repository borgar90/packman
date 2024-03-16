module com.gruppe2.packman {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gruppe2.packman to javafx.fxml;
    exports com.gruppe2.packman;
    exports com.gruppe2.ghost;
    opens com.gruppe2.ghost to javafx.fxml;
    exports com.gruppe2.map;
    opens com.gruppe2.map to javafx.fxml;
}