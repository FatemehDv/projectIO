package com.example.paintio;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;

public class GamePlayer extends Players {
    Color color = new Color(Color.ownColorList[0], Color.moveColorList[0], Color.backgroundColorList[0] );

    public GamePlayer(Label[][] labels, GameController gameController) {
        this.path = new ArrayList<>();
        this.gameController = gameController;
        this.labels = labels;
        init();
        directX = 1;
        runMethod();
    }

    public void init() {
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                labels[i][j].setOnKeyPressed(keyEvent -> {
                    labels[(int) currPos.x][(int) currPos.y].setFocusTraversable(true);
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
        for (int i = 8; i <= 10; i++) {
            for (int j = 8; j <= 10; j++) {
                path.add(new Position(i, j));
                labels[i][j].setStyle("-fx-background-color:" + color.backgroundColor);
            }
        }
        currPos = new Position(8, 8);
        labels[8][8].setStyle("-fx-background-color:" + color.ownColor);
        labels[8][8].requestFocus();
    }

    public void runMethod() {
        new Thread(() -> {
            while (true) {
                double nextX =currPos.x + directX;
                double nextY =currPos.y + directY;
                if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
                    continue;
                }

                if (checkColor(nextX, nextY, Color.moveColorList[1]) ||
                        checkColor(nextX, nextY, Color.moveColorList[2]) ||
                        checkColor(nextX, nextY, Color.moveColorList[3]) )
                    break;


                move(nextX, nextY);

                try {
                    Thread.sleep(FixedValues.speed);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("You win.");
            System.exit(0);
        }).start();

    }

    public void move(double nextX, double nextY) {

        if (path.size() != 0 && checkColor(nextX, nextY, color.backgroundColor)) {
            completeColor(nextX, nextY, color.backgroundColor, color.ownColor);
            return;
        }
        if (!checkColor(nextX, nextY, color.backgroundColor)) {
            path.add(new Position(nextX, nextY));
            setColor(currPos.x, currPos.y, color.moveColor);
        }
        if (checkColor(currPos.x, currPos.y, color.ownColor)) {
            setColor(currPos.x, currPos.y, color.backgroundColor);
        }
        if ( stylePrevLabel.equals(("-fx-background-color:" + color.backgroundColor))) {
            setColor(currPos.x, currPos.y,  color.backgroundColor);
        }
        stylePrevLabel = labels[(int) nextX][(int) nextY].getStyle();
        currPos = new Position(nextX,nextY);
        setColor(nextX, nextY, color.ownColor);
        labels[(int) currPos.x][(int) currPos.y].setFocusTraversable(true);
    }
}