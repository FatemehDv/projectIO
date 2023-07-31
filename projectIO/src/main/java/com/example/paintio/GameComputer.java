package com.example.paintio;

import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.Random;

public class GameComputer extends Players {
    Random random;
    Color color;
    public GameComputer(Label[][] labels, GameController gameController, String ownColor, String moveColor, String backgroundColor) {
        color = new Color(ownColor, moveColor , backgroundColor );
        this.path = new ArrayList<>();
        this.gameController = gameController;
        this.labels = labels;
        random = new Random();
        init();
        runMethod();
    }

    public void init() {
        int rand = random.nextInt(21);
        while (rand == 8)
            rand = random.nextInt(21);

        for (int i = rand ; i <= rand + 2 ; i++) {
            for (int j = rand ; j <= rand + 2; j++) {
                path.add(new Position(i, j));
                labels[i][j].setStyle("-fx-background-color: " + color.backgroundColor);
            }
        }
        currPos = new Position(rand , rand + 1);
        labels[rand ][rand + 1 ].setStyle("-fx-background-color: " + color.ownColor);
    }

    public void runMethod() {
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
                double nextX = currPos.x + directX;
                double nextY = currPos.y + directY;
                if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
                    continue;
                }

                if (checkColor(nextX, nextY, "#6dc46d") ) {   //|| path.size() == 0
                    break;
                }

                /*for(int n = 1; n < 4; n++)
                    if (checkColor(nextX, nextY, Color.moveColorList[n]) &&
                            !(Color.moveColorList[n].equals(color.moveColor)))
                        break;*/


                move(nextX, nextY);
                try {
                    Thread.sleep(FixedValues.speed);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Computer win.");
            System.exit(0);
        }).start();

    }

    public void move(double nextX, double nextY) {
        if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
            return;
        }

        if (checkColor(nextX, nextY, color.moveColor) && random.nextInt(100) < 70)
            return;

        if (path.size() != 0 && checkColor(nextX, nextY, color.backgroundColor)) {
            completeColor(nextX, nextY, color.backgroundColor, color.ownColor);
            return;
        }
        if (!checkColor(nextX, nextY, color.backgroundColor)) {
            path.add(new Position(nextX, nextY));
            setColor(currPos.x, currPos.y, color.moveColor);
        }
        if (checkColor(currPos.x, currPos.y, color.backgroundColor))
            setColor(currPos.x, currPos.y, color.moveColor);

        if ( stylePrevLabel.equals(("-fx-background-color: " + color.backgroundColor))) {
            setColor(currPos.x, currPos.y, color.backgroundColor);
        }
        stylePrevLabel = labels[(int) nextX][(int) nextY].getStyle();
        currPos = new Position(nextX, nextY);
        setColor(nextX, nextY, color.ownColor);
        labels[(int) nextX][(int) nextY].setFocusTraversable(true);
    }
}