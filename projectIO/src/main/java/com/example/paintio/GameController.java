package com.example.paintio;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static com.example.paintio.FixedValues.numberOfPlayer;

public class GameController {
    @FXML
    Pane pane;
    GridPane gridPane;
    int maxSize = 25;
    Label[][] labels;
    @FXML
    TextField tf_namePlayer, tf_scorePlayer;
    boolean[] threadPlayer;
    volatile int numberThread = 0;

    public void initialize() {
        init();
    }

    public void init() {
        this.gridPane = new GridPane();
        tf_namePlayer.setText(FixedValues.name);
        threadPlayer= new boolean[4];
        numberThread =0;

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
        new GamePlayer(labels, gameController);
        threadPlayer[0] = true;
        numberThread++;
        for (int i = 1; i < numberOfPlayer; i++) {
            new GameComputer(labels, gameController,i);
            threadPlayer[i] = true;
            numberThread++;
        }

        new Thread(() -> {
            while (numberThread > 1) Thread.onSpinWait();
            threadPlayer = new boolean[4];
            numberThread=0;
            Platform.runLater(this::showAlert);
        }).start();

    }
    public synchronized void paintLabel(double i , double j , String color){
        labels[(int) i][(int) j].setStyle("-fx-background-color: "+color);
    }

    public synchronized boolean checkColor(double x, double y, String ownColor) {
        return labels[(int) x][(int) y].getStyle().equals("-fx-background-color: " + ownColor);
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

    /*public void calculatorScoreComputer() {
        int sum = 0;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (labels[i][j].getStyle().equals("-fx-background-color: #be1717"))
                    sum++;
            }
        }
        //tf_scorePC.setText(String.valueOf(sum));
    }*/

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Paint IO");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to play again?");

        ButtonType playAgainButton = new ButtonType("Again");
        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(playAgainButton, exitButton);

        Optional<ButtonType> result = alert.showAndWait();
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

    public void backToStart() throws IOException {
        Stage stage = (Stage) tf_namePlayer.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("start_pane.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Game");
        stage.setResizable(false);
        stage.setScene(scene);

    }
}