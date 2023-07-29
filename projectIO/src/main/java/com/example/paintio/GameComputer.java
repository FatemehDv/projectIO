package com.example.paintio;

import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameComputer {
    int maxSize = 25;
    Label[][] labels;
    Position currPos;
    ArrayList<Position> list = new ArrayList<>();
    int directX = 0, directY = 0;
    Random random;
    public GameComputer(Label[][] labels){
        this.labels = labels;
        random = new Random();
        init();
        runMethod();

    }
    public void init() {
        for (int i = 18; i < 22 ; i++) {
            for (int j = 18; j < 22 ; j++) {
                list.add(new Position(i, j));
                labels[i][j].setStyle("-fx-background-color: #be1717");
            }
        }
        currPos = new Position(17, 20);
        labels[17][20].setStyle("-fx-background-color: #690303");
    }
    public void runMethod(){
        new Thread(() -> {
            while (true) {
                int rand = random.nextInt(4) + 1;
                switch (rand) {
                    case 1 -> {
                        directX = 0;
                        directY = -1;
                    }
                    case 2 -> {
                        directX = 0;
                        directY = 1;
                    }
                    case 3 -> {
                        directX = -1;
                        directY = 0;
                    }
                    case 4 -> {
                        directX = 1;
                        directY = 0;
                    }
                }
                move(currPos.x + directX, currPos.y + directY);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
    public void completeColor(double x, double y){
        Polygon polygon = createPolygon(list, list.get(0).x, list.get(0).y);
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x1 = polygon.getPoints().get(i);
            double y1 = polygon.getPoints().get(i + 1);
            setColor(x1, y1, "#be1717");
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
                    setColor(i, j, "#be1717");
                }
            }

        }
        currPos.x = x;
        currPos.y = y;
        setColor(x, y, "#690303");
    }
    public boolean exist(Position position){
        for (Position position1 : list) {
            if (position.compareTo(position1) > 0) return true;
        }
        return false;
    }
    public void move(double nextX, double nextY){
        if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
            return;
        }
        if (checkColor(nextX, nextY, "#6dc46d")) {
            System.out.println("Computer win.");
            System.exit(0);
        }
        if (checkColor(nextX, nextY, "#b66363") && random.nextInt(100) < 70)
            return;
        if (list.size() != 0 && checkColor(nextX, nextY, "#be1717")) {
            completeColor(nextX, nextY);
            return;
        }

        if (!checkColor(nextX, nextY, "#be1717")) {
            list.add(new Position(nextX, nextY));
        }
        if (!checkColor(currPos.x, currPos.y, "#be1717"))
            setColor(currPos.x, currPos.y, "#b66363");

        currPos.x = nextX;
        currPos.y = nextY;
        setColor(nextX, nextY, "#690303");
        labels[(int) nextX][(int) nextY].setFocusTraversable(true);
    }
    public boolean checkColor(double x, double y, String color){
        return labels[(int) x][(int) y].getStyle().equals("-fx-background-color: " + color);
    }
    public void setColor(double x, double y, String color){
        labels[(int) x][(int) y].setStyle("-fx-background-color: " + color);
    }
    public static Polygon createPolygon(List<Position> positions, double translateX, double translateY) {
        Polygon polygon = new Polygon();
        Translate translate = new Translate(translateX, translateY);
        for (Position point : positions) {
            double transformedX = point.x;
            double transformedY = point.y;
            polygon.getPoints().addAll(transformedX, transformedY);
        }
        polygon.getTransforms().add(translate);

        return polygon;
    }
}
