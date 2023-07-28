package com.example.paintio;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Game {
    GridPane gridPane;
    int maxSize = 25;
    Button[][] buttons ;
    Position currPos;
    ArrayList<Position> list = new ArrayList<>();
    int directX = 0, directY = 0;

    public Game(GridPane gridPane){
        this.gridPane = gridPane;
        directX = 1;
    }
    public void init (){
        gridPane.setAlignment(Pos.CENTER);
        buttons = new Button[maxSize][maxSize];

        for (int i = 0; i < maxSize; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(27));
        }
        for (int j = 0; j < maxSize; j++) {
            gridPane.getRowConstraints().add(new RowConstraints(27));
        }
        for (int i = 0; i < maxSize; i++){
            for (int j = 0; j < maxSize; j++){
                buttons[i][j] = new Button();
                GridPane.setHalignment(buttons[i][j], HPos.CENTER);
                buttons[i][j].setMaxSize(26, 26);
                gridPane.add(buttons[i][j], i, j);
                buttons[i][j].setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.UP) {
                        directX = 0;
                        directY = -1;
                    } else if (keyEvent.getCode() == KeyCode.DOWN) {
                        directX = 0;
                        directY = 1;
                    } else if (keyEvent.getCode() == KeyCode.LEFT) {
                        directX = -1;
                        directY = 0;
                    }else if (keyEvent.getCode() == KeyCode.RIGHT) {
                        directX = 1;
                        directY = 0;
                    }
                });
            }
        }
        currPos = new Position(12, 11);
        list.add(new Position(12, 11));
        buttons[0][1].setStyle("-fx-background-color: GREEN");
        buttons[0][0].setStyle("-fx-background-color: BLACK");
    }

    public void move(double nextX, double nextY){
        if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
            return;
        }

        currPos.x = nextX;
        currPos.y = nextY;

    }
    public void completeColor(){

    }
    public boolean checkColor(){

    }
    public void setColor(){

    }
    public static Polygon createPolygon(){}

}

