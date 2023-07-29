package com.example.paintio;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class GamePlayer {
    int maxSize = 25;
    Label[][] labels;
    Position currPos;
    ArrayList<Position> list = new ArrayList<>();
    int directX = 0, directY = 0;

    public GamePlayer(Label[][] labels){
        this.labels = labels;
        init();
        directX = 1;
        runMethod();
    }
    public void init() {
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
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
                    } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                        directX = 1;
                        directY = 0;
                    }
                });
            }
        }
        for (int i = 6; i < 10 ; i++) {
            for (int j = 6; j < 10 ; j++) {
                list.add(new Position(i, j));
                labels[i][j].setStyle("-fx-background-color: #1b811b");
            }
        }
        currPos = new Position(10, 8);
        labels[10][8].setStyle("-fx-background-color: #024d02");
        labels[10][8].requestFocus();
    }

    public void runMethod() {
        new Thread(() -> {
            while (true) {
                move(currPos.x + directX, currPos.y + directY);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void completeColor(double x, double y) {
        Polygon polygon = createPolygon(list, list.get(0).x, list.get(0).y);
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x1 = polygon.getPoints().get(i);
            double y1 = polygon.getPoints().get(i + 1);
            setColor(x1, y1, "#1b811b");
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
                    setColor(i, j, "#1b811b");
                }
            }

        }
        currPos.x = x;
        currPos.y = y;
        setColor(x, y, "#024d02");
    }

    public boolean exist(Position position) {
        for (Position position1 : list) {
            if (position.compareTo(position1) > 0) return true;
        }
        return false;
    }

    public void move(double nextX, double nextY) {

        if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
            return;
        }
        if (checkColor(nextX, nextY, "#b66363")) {
            System.out.println("You win.");
            System.exit(0);
        }
        if (list.size() != 0 && checkColor(nextX, nextY, "#1b811b")) {
            completeColor(nextX, nextY);
            return;
        }


        if (!checkColor(nextX, nextY, "#1b811b")) {
            list.add(new Position(nextX, nextY));
        }
        if (!checkColor(currPos.x, currPos.y, "#1b811b"))
            setColor(currPos.x, currPos.y, "#6dc46d");

        currPos.x = nextX;
        currPos.y = nextY;
        setColor(nextX, nextY, "#024d02");
        labels[(int) nextX][(int) nextY].setFocusTraversable(true);
    }

    public boolean checkColor(double x, double y, String color) {
        return labels[(int) x][(int) y].getStyle().equals("-fx-background-color: " + color);
    }

    public void setColor(double x, double y, String color) {
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
