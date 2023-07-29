package com.example.paintio;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

public class GamePlayer extends Players {

    public GamePlayer(Label[][] labels, int millis){
        this.labels = labels;
        init();
        directX = 1;
        runMethod(millis);
    }

    @Override
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
        for (int i = 6; i < 9 ; i++) {
            for (int j = 6; j < 9 ; j++) {
                list.add(new Position(i, j));
                labels[i][j].setStyle("-fx-background-color: #1b811b");
            }
        }
        currPos = new Position(8, 8);
        labels[8][8].setStyle("-fx-background-color: #024d02");
        labels[8][8].requestFocus();
    }

    @Override
    public void runMethod(int millis) {
        new Thread(() -> {
            while (true) {
                move(currPos.x + directX, currPos.y + directY);
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void move(double nextX, double nextY) {

        if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
            return;
        }
        if (checkColor(nextX, nextY, "#b66363")) {
            System.out.println("You win.");
            System.exit(0);
        }
        if (list.size() != 0 && checkColor(nextX, nextY, "#1b811b")) {
            completeColor(nextX, nextY, "#1b811b", "#024d02");
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
}
