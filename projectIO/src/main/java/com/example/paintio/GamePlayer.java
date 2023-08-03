package com.example.paintio;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;

public class GamePlayer extends Players{

    public GamePlayer(Label[][] labels, GameController gameController) {
        super.color = new Color(Color.ownColorList[0], Color.moveColorList[0], Color.backgroundColorList[0]);

        this.path = new ArrayList<>();
        this.gameController = gameController;
        this.labels = labels;
        init();
        directX = 1;
        new Thread(this::runMethod).start();
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
                    } else if (keyEvent.getCode() == KeyCode.SPACE)
                        shootingA();
                });
                labels[i][j].setOnMouseClicked(mouseEvent -> shootingA() );

            }
        }
        for (int i = 8; i <= 10; i++) {
            for (int j = 8; j <= 10; j++) {
                path.add(new Position(i, j));
                gameController.paintLabel(i, j, color.backgroundColor);
            }
        }
        currPos = new Position(8, 8);
        gameController.paintLabel(8, 8, color.ownColor);
        labels[8][8].requestFocus();
    }

    public void move(double nextX, double nextY) {
        if (path.size() != 0 && gameController.checkColor(nextX, nextY, color.backgroundColor)) {
            completeColor(nextX, nextY, color.backgroundColor, color.ownColor);
            return;
        }
        if (!gameController.checkColor(nextX, nextY, color.backgroundColor)) {
            path.add(new Position(nextX, nextY));
            gameController.paintLabel(currPos.x, currPos.y, color.moveColor);

        }
        if (stylePrevLabel.equals(("-fx-background-color: " + color.backgroundColor)))
            gameController.paintLabel(currPos.x, currPos.y, color.backgroundColor);

        if (gameController.checkColor(currPos.x, currPos.y, color.ownColor)) {
            gameController.paintLabel(currPos.x, currPos.y, color.backgroundColor);
        }

        stylePrevLabel = labels[(int) nextX][(int) nextY].getStyle();
        currPos = new Position(nextX, nextY);
        gameController.paintLabel(nextX, nextY, color.ownColor);
        labels[(int) currPos.x][(int) currPos.y].setFocusTraversable(true);
    }

    public void runMethod() {
        while (gameController.threadPlayer[0]) {
            double nextX = currPos.x + directX;
            double nextY = currPos.y + directY;
            if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
                continue;
            }

            if (gameController.checkColor(nextX, nextY, Color.moveColorList[1]) ||
                    gameController.checkColor(nextX, nextY, Color.moveColorList[2]) ||
                    gameController.checkColor(nextX, nextY, Color.moveColorList[3])) {
                gameController.numberThread--;
                gameController.threadPlayer[0] = false;
                break;
            }


            move(nextX, nextY);

            try {
                Thread.sleep(FixedValues.speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("You win.");
    }

}