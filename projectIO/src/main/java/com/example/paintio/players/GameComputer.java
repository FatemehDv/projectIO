package com.example.paintio.players;

import javafx.scene.control.Label;

import java.util.List;
import java.util.Random;

public class GameComputer extends Players {
    Random random;
    String stylePrevLabel = "#6dc46d";
    List<Position> playerPositionsList;
    public GameComputer(Label[][] labels, List<Position> myList, List<Position> playerList){
        this.myPositionsList = myList;
        this.playerPositionsList = playerList;
        this.labels = labels;
        random = new Random();
        init();
        runMethod();
    }

    @Override
    public void init() {
        int rand = random.nextInt(26);
        for (int i = rand; i < rand + 3; i++) {
            for (int j = rand; j < rand + 3; j++) {
                myPositionsList.add(new Position(i, j));
                labels[i][j].setStyle("-fx-background-color: #be1717");
            }
        }
        currPos = new Position(rand , rand + 2);
        labels[rand][rand + 2].setStyle("-fx-background-color: #690303");
    }

    @Override
    public void runMethod(){
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
                move(currPos.x + directX, currPos.y + directY);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void move(double nextX, double nextY){
        if (nextX >= maxSize || nextX < 0 || nextY >= maxSize || nextY < 0) {
            return;
        }
        if (checkColor(nextX, nextY, "#6dc46d")) {
            System.out.println("Computer win.");
            System.exit(0);
        }
        if (checkColor(nextX, nextY, "#b66363") && random.nextInt(100) < 70)
            return;

        if (myPositionsList.size() != 0 && checkColor(nextX, nextY, "#be1717")) {
            completeColor(nextX, nextY,"#be1717", "#690303");
            return;
        }

        if (!checkColor(nextX, nextY, "#be1717")) {
            myPositionsList.add(new Position(nextX, nextY));
            setColor(currPos.x, currPos.y, "#b66363");
        }
        if (!checkColor(currPos.x, currPos.y, "#be1717"))
            setColor(currPos.x, currPos.y, "#b66363");

        if ( stylePrevLabel.equals(("-fx-background-color: #be1717"))) {
            setColor(currPos.x, currPos.y, "#be1717");
        }

        stylePrevLabel = labels[(int) nextX][(int) nextY].getStyle();
        currPos = new Position(nextX, nextY);
        setColor(nextX, nextY, "#690303");
        labels[(int) nextX][(int) nextY].setFocusTraversable(true);
    }
}
