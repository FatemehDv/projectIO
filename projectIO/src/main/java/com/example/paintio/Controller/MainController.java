package com.example.paintio.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public AnchorPane mainAnchor;
    public Button startGame;

    @FXML
    private void LoadSplash() {
        try {
            SplashScreenController.isPlayed = true;
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("splashScreen.fxml"));
            mainAnchor.getChildren().setAll(anchorPane);

            FadeTransition faidIn = new FadeTransition(Duration.seconds(3), anchorPane);
            faidIn.setFromValue(0);
            faidIn.setToValue(1);
            faidIn.setCycleCount(1);

            FadeTransition faidOut = new FadeTransition(Duration.seconds(2), anchorPane);
            faidOut.setFromValue(1);
            faidOut.setToValue(0);
            faidOut.setCycleCount(1);

            faidIn.play();
            faidIn.setOnFinished((e) -> {
                faidOut.play();
            });
            faidOut.setOnFinished((e) -> {
                try {
                    AnchorPane anchorPane1 = FXMLLoader.load(getClass().getResource("main.fxml"));
                    mainAnchor.getChildren().setAll(anchorPane1);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!SplashScreenController.isPlayed) {
            LoadSplash();
        }
    }

    public void StartGame(ActionEvent event) {

    }
}
