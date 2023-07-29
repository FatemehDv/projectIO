package com.example.paintio.Main;

import com.example.paintio.players.GameComputer;
import com.example.paintio.players.GamePlayer;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class Game {
    GridPane gridPane;
    int maxSize = 25;
    Label[][] labels;
    int millis;

    public Game(GridPane gridPane,int millis){
        this.gridPane = gridPane;
        this.millis = millis;
        init();
    }

    public void init (){
        gridPane.setAlignment(Pos.CENTER);
        labels = new Label[maxSize][maxSize];

        for (int i = 0; i < maxSize; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(27));
        }
        for (int j = 0; j < maxSize; j++) {
            gridPane.getRowConstraints().add(new RowConstraints(27));
        }

        for (int i = 0; i < maxSize; i++){
            for (int j = 0; j < maxSize; j++){
                labels[i][j] = new Label("");
                GridPane.setHalignment(labels[i][j], HPos.CENTER);
                labels[i][j].setMaxSize(26, 26);
                labels[i][j].setStyle("-fx-background-color: #968080");
                gridPane.add(labels[i][j], i, j);
            }
        }
        new GamePlayer(labels, millis);
        new GameComputer(labels, millis);
    }
}

