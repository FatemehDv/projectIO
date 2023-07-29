module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.paintio.Controller;
    opens com.example.paintio.Controller to javafx.fxml;
    exports com.example.paintio.players;
    opens com.example.paintio.players to javafx.fxml;
    exports com.example.paintio.Main;
    opens com.example.paintio.Main to javafx.fxml;
}