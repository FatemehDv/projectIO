package com.example.paintio;

import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.Random;

public class GameComputer extends Players{
    Random random;
    Color color;
    int numberPlayer ;
    public GameComputer(Label[][] labels, GameController gameController, int numberPlayer) {
        this.numberPlayer = numberPlayer;
        color = new Color(Color.ownColorList[numberPlayer], Color.moveColorList[numberPlayer]
                , Color.backgroundColorList[numberPlayer]);
        this.path = new ArrayList<>();
        this.gameController = gameController;
        this.labels = labels;
        random = new Random();
        init();

        new Thread(this::runMethod).start();
    }

    public void init() {
        int rand = random.nextInt(21);
        while (rand == 8)
            rand = random.nextInt(21);

        for (int i = rand ; i <= rand + 2 ; i++) {
            for (int j = rand ; j <= rand + 2; j++) {
                path.add(new Position(i, j));
                gameController.paintLabel(i, j, color.backgroundColor);
            }
        }
        currPos = new Position(rand , rand + 1);
        gameController.paintLabel(rand, rand + 1, color.ownColor);
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
        if (stylePrevLabel.equals(("-fx-background-color: " + color.backgroundColor))) {
            gameController.paintLabel(currPos.x, currPos.y, color.backgroundColor);
        }

        if (gameController.checkColor(currPos.x, currPos.y, color.ownColor)) {
            gameController.paintLabel(currPos.x, currPos.y, color.backgroundColor);
        }
        stylePrevLabel = labels[(int) nextX][(int) nextY].getStyle();
        currPos = new Position(nextX, nextY);
        gameController.paintLabel(nextX, nextY, color.ownColor);
    }

    public void runMethod() {
        while (gameController.threadPlayer[numberPlayer]) {
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
            double nextX = currPos.x + directX;
            double nextY = currPos.y + directY;

            if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
                continue;
            }
            if (gameController.checkColor(nextX, nextY, color.moveColor) && random.nextInt(100) < 70)
                continue;

            if ((gameController.checkColor(nextX, nextY, Color.moveColorList[0]) ||
                    gameController.checkColor(nextX, nextY, Color.moveColorList[1]) ||
                    gameController.checkColor(nextX, nextY, Color.moveColorList[2]) ||
                    gameController.checkColor(nextX, nextY, Color.moveColorList[3])) &&
                    !(gameController.checkColor(nextX, nextY, color.moveColor))) {
                gameController.threadPlayer[numberPlayer] = false;
                gameController.numberThread--;
                break;
            }


            move(nextX, nextY);
            try {
                Thread.sleep(FixedValues.speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Player number "+numberPlayer+" lost");
    }
}