package com.example.paintio;

import com.example.paintio.GameController;
import com.example.paintio.Position;
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
    Color color;


    public void init(){}

    public void move(double nextX, double nextY) {}

    public void completeColor(double x, double y, String backgroundColor, String ownColor){

        Polygon polygon = createPolygon(path);
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x1 = polygon.getPoints().get(i);
            double y1 = polygon.getPoints().get(i + 1);
            gameController.paintLabel(x1, y1, backgroundColor);
            if (existPosition(new Position(x1, y1))) {
                path.add(new Position(x1, y1));
            }
        }
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (polygon.contains(i, j)) {
                    if (existPosition(new Position(i, j))) {
                        path.add(new Position(i, j));
                    } else if (gameController.checkColor(i, j, "#968080"))
                        continue;
                    gameController.paintLabel(i, j, backgroundColor); //"#be1717"
                }
            }

        }
        fixBug();
        gameController.calculatorScorePlayer();
        path.clear();
        currPos = new Position(x, y);
        gameController.paintLabel(x, y, ownColor); //"#690303"
    }


    public boolean existPosition(Position position) {
        for (Position position1 : path) {
            if (position.compareTo(position1) > 0) return false;
        }
        return true;
    }

    public static Polygon createPolygon(List<Position> positions) {
        Polygon polygon = new Polygon();
        for (Position point : positions)
            polygon.getPoints().addAll(point.x, point.y);
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
    public void shootingA() {
        int directX = this.directX;
        int directY = this.directY;
        final int[] pos = {(int) currPos.x, (int) currPos.y};

        new Thread(() -> {
            int x = pos[0];
            int y = pos[1];


            if (directY == -1 && y < 7)
                return;
            if (directY == 1 && y > 16)
                return;
            if (directX == -1 && x < 7)
                return;
            if (directX == 1 && x > 16)
                return;

            String prevStyle = labels[x][y].getStyle();
            for (int i = 0; i < 6; i++) {
                labels[x][y].setStyle(prevStyle);
                x += directX;
                y += directY;
                prevStyle = labels[x][y].getStyle();
                labels[x][y].setStyle("-fx-background-color: #282727");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = x-1; i <= x+1 ; i++) {
                for (int j = y-1; j <= y+1 ; j++) {
                    checkPlayerDeath(i , j);
                    labels[i][j].setStyle("-fx-background-color: " + color.backgroundColor);
                }
            }
        }).start();
    }
    public void checkPlayerDeath(int i , int j){
        for (int k = 0; k < 4; k++) {
            if (labels[i][j].getStyle().equals("-fx-background-color: " + Color.ownColorList[k])) {
                if (gameController.threadPlayer[k]){
                    gameController.threadPlayer[k] = false;
                    gameController.numberThread--;
                }
                return;
            }
        }
    }
}

