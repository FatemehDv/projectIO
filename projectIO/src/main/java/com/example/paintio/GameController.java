package com.example.paintio;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import java.util.Optional;

import static com.example.paintio.FixedValues.numberOfPlayer;

public class GameController {
    @FXML
    Pane pane;
    GridPane gridPane;
    int maxSize = 25;
    Label[][] labels;
    @FXML
    TextField tf_namePlayer, tf_scorePlayer, tf_scorePC;

    public void initialize() {
        this.gridPane = new GridPane();
        tf_namePlayer.setText(FixedValues.name);
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
        GameController gameController = this;
        for (int i  = 1; i < numberOfPlayer; i++ ) {
            new GameComputer(labels, gameController, Color.ownColorList[i],
                    Color.moveColorList[i], Color.backgroundColorList[i]);
        }

        new GamePlayer(labels, gameController);

        Runtime.getRuntime().addShutdownHook(new Thread(this::showAlert));
    }

    public void calculatorScorePlayer() {
        int sum = 0;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (labels[i][j].getStyle().equals("-fx-background-color: #1b811b"))
                    sum++;
            }
        }
        tf_scorePlayer.setText(String.valueOf(sum));
    }

    public void calculatorScoreComputer() {
        int sum = 0;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (labels[i][j].getStyle().equals("-fx-background-color: #be1717"))
                    sum++;
            }
        }
        tf_scorePC.setText(String.valueOf(sum));
    }
    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("بازی");
        alert.setHeaderText("آیا می‌خواهید دوباره بازی کنید؟");

        ButtonType playAgainButton = new ButtonType("دوباره بازی کردن");
        ButtonType exitButton = new ButtonType("خروج از بازی", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(playAgainButton, exitButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == playAgainButton) {
            // کدی که برای دوباره بازی کردن نیاز دارید
        } else {
            // کدی که برای خروج از بازی نیاز دارید
        }
        alert.showAndWait();
    }
}