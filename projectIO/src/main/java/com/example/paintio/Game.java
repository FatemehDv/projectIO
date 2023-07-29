package com.example.paintio;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class Game {
    GridPane gridPane;
    int maxSize = 25;
    Label[][] labels;
    Button[][] buttons ;
    Position currPos;
    ArrayList<Position> list = new ArrayList<>();
    int directX = 0, directY = 0;

    public Game(GridPane gridPane){
        this.gridPane = gridPane;
        init();
        directX = 1;
        runMethod();
    }
    public void runMethod(){
        new Thread(() -> {
            while (true){
                move(currPos.x + directX , currPos.y + directY );
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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
                gridPane.add(labels[i][j], i, j);

                labels[i][j].setOnKeyPressed(keyEvent -> {
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

        for (int i = 10; i < 13; i++){
            for (int j = 10; j < 13; j++){
                labels[i][j].setStyle("-fx-background-color: GREEN");
                list.add(new Position(i, j));
            }
        }

        currPos = new Position(12, 11);
        list.add(new Position(12, 11));
    }

    public void move(double nextX, double nextY){
        if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
            return;
        }
        if (list.size() != 0 && checkColor(nextX, nextY, "GREEN")) {
            completeColor(nextX, nextY);
            return;
        }

        if (!checkColor(nextX, nextY, "GREEN")) {
            list.add(new Position(nextX, nextY));
        }

        if (!checkColor(currPos.x, currPos.y, "GREEN"))
            setColor(currPos.x, currPos.y, "#6dc46d");

        currPos.x = nextX;
        currPos.y = nextY;
        setColor(nextX, nextY, "BLACK");

    }

    public void completeColor(double x, double y){
        Polygon polygon = createPolygon(list, list.get(0).x, list.get(0).y);

        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x1 = polygon.getPoints().get(i);
            double y1 = polygon.getPoints().get(i + 1);
            setColor(x1, y1, "GREEN");
            if (!exist(new Position(x1, y1))) {
                list.add(new Position(x1, y1));
            }
        }

        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (polygon.contains(i, j)) {
                    if (!exist(new Position(i, j))) {
                        list.add(new Position(i, j));
                    }
                    setColor(i, j, "GREEN");
                }
            }

        }
        currPos.x = x;
        currPos.y = y;
        setColor(x , y , "BLACK");
    }

    public boolean exist(Position position){
        for (Position position1 : list) {
            if (position.compareTo(position1) > 0)
                return true;
        }
        return false;
    }

    public boolean checkColor(double x, double y, String color){
        return labels[(int) x][(int) y].getStyle().equals("-fx-background-color: " + color);
    }

    public void setColor(double x, double y, String color){
        labels[(int) x][(int) y].setStyle("-fx-background-color: " + color);
    }

    public static Polygon createPolygon(List<Position> positions, double translateX, double translateY){
        Polygon polygon = new Polygon();

        // ایجاد مبدل ترجمه
        Translate translate = new Translate(translateX, translateY);

        // تبدیل نقاط و افزودن آنها به چندضلعی
        for (Position point : positions) {
            double transformedX = point.x;
            double transformedY = point.y;
            polygon.getPoints().addAll(transformedX, transformedY);
        }

        // اعمال ترجمه به چندضلعی
        polygon.getTransforms().add(translate);

        return polygon;
    }
}

