package com.example.paintio;

import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;
import java.util.ArrayList;
import java.util.List;

public class Players {
    int maxSize = 25;
    Label[][] labels;
    Position currPos;
    ArrayList<Position> list = new ArrayList<>();
    int directX = 0, directY = 0;
    public void init(){}
    public void runMethod(int millis){}
    public void completeColor(double x, double y, String color1, String color2){

        Polygon polygon = createPolygon(list, list.get(0).x, list.get(0).y);
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x1 = polygon.getPoints().get(i);
            double y1 = polygon.getPoints().get(i + 1);
            setColor(x1, y1, color1);
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
                    setColor(i, j, color1);
                }
            }

        }
        currPos.x = x;
        currPos.y = y;
        setColor(x, y, color2);
    }

    public boolean exist(Position position){
        for (Position position1 : list) {
            if (position.compareTo(position1) > 0) return true;
        }
        return false;
    }
    public void move(double nextX, double nextY) {}

    public boolean checkColor(double x, double y, String color){
        return labels[(int) x][(int) y].getStyle().equals("-fx-background-color: " + color);
    }

    public void setColor(double x, double y, String color){
        labels[(int) x][(int) y].setStyle("-fx-background-color: " + color);
    }
    public static Polygon createPolygon(List<Position> positions, double translateX, double translateY){
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
