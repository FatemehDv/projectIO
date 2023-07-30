package com.example.paintio.Controller;

import com.example.paintio.players.GameComputer;
import com.example.paintio.players.GamePlayer;
import com.example.paintio.players.Position;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;

public class MainController implements Initializable {

    public AnchorPane mainAnchor;
    public Button startGame;
    @FXML
    Pane pane;

    GridPane gridPane;
    int maxSize = 25;
    Label[][] labels;

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
        //if (!SplashScreenController.isPlayed) {
         //   LoadSplash();
        //}
        this.gridPane = new GridPane();
        init();
    }

    public void init() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        labels = new Label[maxSize][maxSize];
        for (int i = 0; i < maxSize; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(26));
        }
        for (int j = 0; j < maxSize; j++) {
            gridPane.getRowConstraints().add(new RowConstraints(26));
        }
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                labels[i][j] = new Label("");
                GridPane.setHalignment(labels[i][j], HPos.CENTER);
                labels[i][j].setMaxSize(25, 25);
                labels[i][j].setStyle("-fx-background-color: #968080");
                gridPane.add(labels[i][j], i, j);
            }
        }
        pane.getChildren().add(gridPane);
        List<Position> playePositionsList = new ArrayList<>();
        List<Position> computerPositionsList = new ArrayList<>();
        new GameComputer(labels, computerPositionsList, playePositionsList);
        new GamePlayer(labels, playePositionsList, computerPositionsList);
    }

    public void fixBug() {
        new Thread(() -> {
            for (int i = 1; i < maxSize - 1; i++) {
                for (int j = 1; j < maxSize - 1; j++) {
                    if (labels[i - 1][j].getStyle().equals(labels[i][j - 1].getStyle())
                            && labels[i - 1][j].getStyle().equals(labels[i + 1][j].getStyle())
                            && labels[i - 1][j].getStyle().equals(labels[i][j + 1].getStyle()))
                        labels[i][j].setStyle(labels[i - 1][j].getStyle());
                }
            }
        }).start();
    }
}
