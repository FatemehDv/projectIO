package com.example.paintio;

import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;
import java.util.List;

public class Players {
    protected int maxSize = 25;

    protected String stylePrevLabel = "#6dc46d";;
    protected Label[][] labels;
    protected Position currPos;
    protected List<Position> path;
    GameController gameController;
    protected int directX = 0, directY = 0;


    public void init(){}
    public void runMethod(){}
    public void move(double nextX, double nextY) {}

    public void completeColor(double x, double y, String color1, String color2){

        Polygon polygon = createPolygon(path, path.get(0).x, path.get(0).y);
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x1 = polygon.getPoints().get(i);
            double y1 = polygon.getPoints().get(i + 1);
            setColor(x1, y1, color1);
            if (!exist(new Position(x1, y1))) {
                path.add(new Position(x1, y1));
            }
        }
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (polygon.contains(i, j)) {
                    if (!exist(new Position(i, j))) {
                        path.add(new Position(i, j));
                    } else if (checkColor(i, j, "#968080"))
                        continue;
                    setColor(i, j, color1); //"#be1717"
                }
            }

        }
        fixBug();
        gameController.calculatorScoreComputer();
        gameController.calculatorScorePlayer();
        path.clear();
        currPos = new Position(x, y);
        setColor(x, y, color2); //"#690303"
    }

    public boolean exist(Position position){
        for (Position position1 : path) {
            if (position.compareTo(position1) > 0) return true;
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

        Translate translate = new Translate(translateX, translateY);
        for (Position point : positions) {
            double transformedX = point.x;
            double transformedY = point.y;
            polygon.getPoints().addAll(transformedX, transformedY);
        }
        polygon.getTransforms().add(translate);

        return polygon;
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
