module com.example.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.inventorysystem to javafx.fxml;
    exports com.example.inventorysystem;
}