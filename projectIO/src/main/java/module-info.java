module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.paintio to javafx.fxml;
    exports com.example.paintio;
}