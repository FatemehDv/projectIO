package com.example.paintio;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    GridPane gridPane;

    int maxSize = 25;
    Button[][] buttons;
    Position currentPosition;

    ArrayList<Position> list = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        init();
        Scene scene = new Scene(gridPane, 700, 700);
        primaryStage.setTitle("Chessboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void init() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        buttons = new Button[maxSize][maxSize];
        for (int i = 0; i < maxSize; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(27));
        }
        for (int j = 0; j < maxSize; j++) {
            gridPane.getRowConstraints().add(new RowConstraints(27));
        }
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                buttons[i][j] = new Button();
                GridPane.setHalignment(buttons[i][j], HPos.CENTER);
                buttons[i][j].setMaxSize(26, 26);
                gridPane.add(buttons[i][j], i, j);
                buttons[i][j].setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.UP)
                        move(currentPosition.x, currentPosition.y - 1);
                    else if (keyEvent.getCode() == KeyCode.DOWN)
                        move(currentPosition.x, currentPosition.y + 1);
                    else if (keyEvent.getCode() == KeyCode.LEFT)
                        move(currentPosition.x - 1, currentPosition.y);
                    else if (keyEvent.getCode() == KeyCode.RIGHT)
                        move(currentPosition.x + 1, currentPosition.y);
                });
            }
        }
        currentPosition = new Position(0, 0);
        list.add(new Position(0, 0));
        buttons[0][0].setStyle("-fx-background-color: BLACK");
    }

    public void move(double x, double y) {
        if (x >= maxSize || x < 0 || y >= maxSize || y < 0) {
            System.out.println("Error");
            return;
        }
        if (list.size() != 0 && buttons[(int) x][(int) y].getStyle().equals("-fx-background-color: GREEN")) {
            Polygon polygon = createPolygon(list, list.get(0).x, list.get(0).y);
            for (int i = 0; i < polygon.getPoints().size(); i += 2) {
                double x1 = polygon.getPoints().get(i);
                double y1 = polygon.getPoints().get(i + 1);
                buttons[(int) x1][(int) y1].setStyle("-fx-background-color: GREEN");
            }
            for (int i = 0; i < maxSize; i++) {
                for (int j = 0; j < maxSize; j++) {
                    if (polygon.contains(i, j)) {
                        buttons[i][j].setStyle("-fx-background-color: GREEN");
                    }
                }

            }
            currentPosition.x = x;
            currentPosition.y = y;
            buttons[(int) currentPosition.x][(int) currentPosition.y].setStyle("-fx-background-color: BLACK");
            return;
        }
        if (!buttons[(int) x][(int) y].getStyle().equals("-fx-background-color: GREEN")) {
            list.add(new Position(x, y));
        }
        buttons[(int) currentPosition.x][(int) currentPosition.y].setStyle("-fx-background-color: GREEN");
        currentPosition.x = x;
        currentPosition.y = y;
        buttons[(int) currentPosition.x][(int) currentPosition.y].setStyle("-fx-background-color: BLACK");
    }

    public static Polygon createPolygon(List<Position> positions, double translateX, double translateY) {
        Polygon polygon = new Polygon();

        // ایجاد مبدل ترجمه
        Translate translate = new Translate(translateX, translateY);

        // تبدیل نقاط و افزودن آنها به چندضلعی
        for (Position point : positions) {
            double transformedX = point.x;
            double transformedY = point.y;
            polygon.getPoints().addAll(transformedX, transformedY);
        }

        // اعمال ترجمه به چندضلعی
        polygon.getTransforms().add(translate);

        return polygon;
    }
}