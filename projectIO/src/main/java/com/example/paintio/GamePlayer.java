package com.example.paintio;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;

public class GamePlayer extends Players {
    public GamePlayer(Label[][] labels, GameController gameController) {
        super(0);
        super.color = new Color(Color.ownColorList[numberPlayer]
                , Color.moveColorList[numberPlayer], Color.backgroundColorList[numberPlayer]);
        this.path = new ArrayList<>();
        this.gameController = gameController;
        this.labels = labels;
        init();
        directX = 1;
        gameController.tf_countShoot.setText(String.valueOf(countShootA));
        new Thread(this::runMethod).start();
    }

    public void init() {
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                labels[i][j].setOnKeyPressed(keyEvent -> {
                    labels[currPos.x][currPos.y].setFocusTraversable(true);
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
                    else if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (countShootA > 0)
                            shootingA();
                    }else if (keyEvent.getCode() == KeyCode.SPACE) {
                        if (timeShootB <= 0)
                            shootingB();
                    }
                });
                labels[i][j].setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        if (countShootA > 0)
                            shootingA();
                    } else {
                        if (timeShootB <= 0)
                            shootingB();
                    }
                });
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
        labels[currPos.x][currPos.y].setFocusTraversable(true);
    }

    public void move(int nextX, int nextY) {
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

        stylePrevLabel = labels[nextX][nextY].getStyle();
        currPos = new Position(nextX, nextY);
        gameController.paintLabel(nextX, nextY, color.ownColor);
        labels[currPos.x][currPos.y].setFocusTraversable(true);
        Platform.runLater(() -> labels[currPos.x][currPos.y].requestFocus());
    }

    public void runMethod() {
        WHILE:
        while (gameController.threadPlayer[numberPlayer]) {
            labels[currPos.x][currPos.y].setFocusTraversable(true);
            int nextX = currPos.x + directX;
            int nextY = currPos.y + directY;
            if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
                continue;
            }
            for (int i = 0; i < 4; i++) {
                if (gameController.checkColor(nextX, nextY, Color.ownColorList[i])) {
                    lost(i);
                    lost(numberPlayer);
                    break WHILE;
                }
            }

            int n = findNumberPlayer(nextX, nextY);
            if (n != -1) {
                lost(n);
            }
            move(nextX, nextY);
            if (timeShootB > 0)
                timeShootB -= FixedValues.speed / 1000.0;
            try {
                Thread.sleep(FixedValues.speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}