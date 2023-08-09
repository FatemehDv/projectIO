package com.example.paintio;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuideController {

    public void back(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("start_pane.fxml"));
        Stage s1 = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        Scene scene = new Scene (parent);
        s1.setScene(scene);
        s1.show();
    }
}
