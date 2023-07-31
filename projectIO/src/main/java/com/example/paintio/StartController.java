package com.example.paintio;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class StartController {

    @FXML
    TextField tf_name;
    @FXML
    ComboBox<String> cm_level, cm_speed;
    @FXML
    ComboBox<String> num_player;
    @FXML
    private Pane mainAnchor;
    public void initialize() {

        if (!SplashScreenController.isPlayed) {
            LoadSplash();
        }

        ObservableList<String> list_speed = FXCollections.observableArrayList("Slow", "Normal", "Fast");
        cm_speed.setItems(list_speed);
        cm_speed.setValue("Slow");

        ObservableList<String> number_player = FXCollections.observableArrayList("2","3","4");
        num_player.setItems(number_player);
        num_player.setValue("2");

        ObservableList<String> list_level = FXCollections.observableArrayList("Easy", "Normal", "Hard");
        cm_level.setItems(list_level);
        cm_level.setValue("Easy");
    }

    public void exit() {
        System.exit(0);
    }

    public void start() throws IOException {
        if (tf_name.getText().isEmpty())
            showMessage("Player name not entered", "Error");
        else {
            FixedValues.name = tf_name.getText();
            FixedValues.level = cm_level.getValue();
            FixedValues.numberOfPlayer = num_player.getValue().equals("2") ? 2
                    : (num_player.getValue().equals("3") ? 3 : 4);
            FixedValues.speed = cm_speed.getValue().equals("Slow") ? 800
                    : (cm_speed.getValue().equals("Normal") ? 500 : 200);

            Stage stage = (Stage) tf_name.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("game_pane.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 800);
            stage.setTitle("Game");
            stage.setResizable(false);
            stage.setScene(scene);

        }
    }

    @FXML
    private void LoadSplash() {
        try {
            SplashScreenController.isPlayed = true;
            Pane anchorPane = FXMLLoader.load(getClass().getResource("splashScreen.fxml"));
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
                    Pane anchorPane1 = FXMLLoader.load(getClass().getResource("start_pane.fxml"));
                    mainAnchor.getChildren().setAll(anchorPane1);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showMessage(String message, String type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (type.equals("Error"))
            alert.setAlertType(Alert.AlertType.ERROR);
        alert.getDialogPane().setPrefSize(300, 100);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}