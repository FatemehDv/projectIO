module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.paintio to javafx.fxml;
    exports com.example.paintio;
}