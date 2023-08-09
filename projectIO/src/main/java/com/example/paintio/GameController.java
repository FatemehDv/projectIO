package com.example.paintio;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.paintio.FixedValues.numberOfPlayer;

public class GameController {
    @FXML
    Pane pane;
    GridPane gridPane;
    int maxSize = 25;
    Label[][] labels;
    @FXML
    TextField tf_namePlayer, tf_scorePlayer, tf_countShoot;

    boolean[] threadPlayer;

    AtomicInteger counterThread;

    public void initialize() {
        init();
    }

    public void init() {
        this.gridPane = new GridPane();
        tf_namePlayer.setText(FixedValues.name);
        threadPlayer = new boolean[4];
        counterThread = new AtomicInteger(0);

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
                GridPane.setValignment(labels[i][j], VPos.CENTER);
                labels[i][j].setMaxSize(25, 25);
                labels[i][j].setStyle("-fx-background-color: #968080");
                gridPane.add(labels[i][j], i, j);
            }
        }

        pane.getChildren().add(gridPane);

        threadPlayer[0] = true;
        counterThread.getAndIncrement();
        new GamePlayer(labels, this);

        for (int i = 1; i < numberOfPlayer; i++) {
            threadPlayer[i] = true;
            counterThread.getAndIncrement();
            new GameComputer(labels, this, i);
        }

        new Thread(() -> {
            while (threadPlayer[0] && counterThread.get() > 1) Thread.onSpinWait();
            String text;
            if (counterThread.get() > 1)
                text = "you lost";
            else
                text = "you win";
            String finalText = text;
            threadPlayer = new boolean[4];
            counterThread.set(0);
            Platform.runLater(() -> showAlert(finalText));
        }).start();

    }

    public synchronized void paintLabel(int i, int j, String color) {
        labels[i][j].setStyle("-fx-background-color: " + color);
    }

    public synchronized boolean checkColor(int x, int y, String ownColor) {
        return labels[x][y].getStyle().equals("-fx-background-color: " + ownColor);
    }

    public void calculatorScorePlayer() {
        int sum = 0;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (labels[i][j].getStyle().equals("-fx-background-color: #1b811b"))
                    sum++;
            }
        }
        int finalSum = sum;
        Platform.runLater(() -> {
            tf_scorePlayer.setText(String.valueOf(finalSum));
        });
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Paint IO");
        alert.setHeaderText(null);
        alert.setContentText(message + "\nDo you want to play again?");

        ButtonType playAgainButton = new ButtonType("Again");
        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(playAgainButton, exitButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == playAgainButton) {
                init();
            } else {
                alert.close();
                try {
                    backToStart();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public void backToStart() throws IOException {
        Stage stage = (Stage) tf_namePlayer.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("start_pane.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Game");
        stage.setResizable(false);
        stage.setScene(scene);

    }

}