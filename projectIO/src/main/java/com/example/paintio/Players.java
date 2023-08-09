package com.example.paintio;

import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Polygon;

import java.io.File;
import java.util.List;

public class Players {
    protected int maxSize = 25;

    protected String stylePrevLabel = "#6dc46d";
    protected Label[][] labels;
    protected Position currPos;
    protected List<Position> path;
    protected int directX = 0, directY = 0;
    GameController gameController;
    Color color;
    int numberPlayer;
    protected int countShootA;
    protected float timeShootB;

    public Players(int numberPlayer) {
        this.numberPlayer = numberPlayer;
        this.countShootA = 5;
        this.timeShootB = 3;
    }

    public static Polygon createPolygon(List<Position> positions) {
        Polygon polygon = new Polygon();
        for (Position point : positions)
            polygon.getPoints().addAll(Double.valueOf(String.valueOf(point.x)), Double.valueOf(String.valueOf(point.y)));
        return polygon;
    }

    public void completeColor(int x, int y, String backgroundColor, String ownColor) {
        Polygon polygon = createPolygon(path);
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            int x1 = polygon.getPoints().get(i).intValue();
            int y1 = polygon.getPoints().get(i + 1).intValue();
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
        final int[] pos = {currPos.x, currPos.y};

        new Thread(() -> {
            int x = pos[0];
            int y = pos[1];

            if (x == 0 || x == 24 || y == 0 || y == 24)
                return;
            if (directY == -1 && y < 6)
                return;
            if (directY == 1 && y < 18)
                return;
            if (directX == -1 && x < 6)
                return;
            if (directX == 1 && x > 18)
                return;

            String prevStyle = labels[x][y].getStyle();
            for (int i = 0; i < 6; i++) {
                labels[x][y].setStyle(prevStyle);
                x += directX;
                y += directY;
                prevStyle = labels[x][y].getStyle();
                labels[x][y].setStyle("-fx-background-color: #282727");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            countShootA--;
            playAudio("shoot.wav");
            gameController.tf_countShoot.setText(String.valueOf(countShootA));
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    checkPlayerDeath(i, j);
                    labels[i][j].setStyle("-fx-background-color: " + color.backgroundColor);
                }
            }
        }).start();
    }

    public void shootingB() {
        int directX = this.directX;
        int directY = this.directY;
        final int[] pos = {currPos.x, currPos.y};
        new Thread(() -> {
            int x = pos[0];
            int y = pos[1];

            if (x != 0 && x != 24 && y != 0 && y != 24) {
                x += directX;
                y += directY;
            }
            timeShootB = 3;
            playAudio("shoot.wav");
            String prevStyle = labels[x][y].getStyle();
            while (x > 0 && x < 24 && y > 0 && y < 24) {
                x += directX;
                y += directY;
                prevStyle = labels[x][y].getStyle();
                if (checkPlayerDeath(x, y))
                    return;
                gameController.paintLabel(x, y, "#3d3b3b");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                labels[x][y].setStyle(prevStyle);
            }
            labels[x][y].setStyle(prevStyle);
        }).start();
    }
    public void playAudio(String name){
        String currentPath = System.getProperty("user.dir");
        String  audioPath = currentPath + "/audio/"+name;
        Media media = new Media(new File(audioPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
    public void lost(int nPlayer) {
        System.out.println("Player number " + (nPlayer + 1) + " lost");
        gameController.threadPlayer[nPlayer] = false;
        gameController.counterThread.getAndDecrement();
        if (nPlayer == 0) {
            playAudio("my_game_over.wav");
        } else {
            playAudio("game_over.wav");
        }
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                if (gameController.checkColor(i, j, Color.ownColorList[nPlayer]) ||
                        gameController.checkColor(i, j, Color.backgroundColorList[nPlayer]) ||
                        gameController.checkColor(i, j, Color.moveColorList[nPlayer]))
                    gameController.paintLabel(i, j, "#968080");
            }
        }
    }

    public int findNumberPlayer(int nextX, int nextY) {
        for (int i = 0; i < 4; i++) {
            if (i == numberPlayer)
                continue;
            if (gameController.checkColor(nextX, nextY, Color.moveColorList[i]))
                return i;
        }
        return -1;
    }

    public boolean checkPlayerDeath(int i, int j) {
        for (int k = 0; k < 4; k++) {
            if (labels[i][j].getStyle().equals("-fx-background-color: " + Color.ownColorList[k])) {
                if (gameController.threadPlayer[k]) {
                    lost(k);
                }
                return true;
            }
        }
        return false;
    }
}