module com.gruppe2.packman {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gruppe2.packman to javafx.fxml;
    exports com.gruppe2.packman;
}