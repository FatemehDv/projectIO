package com.example.paintio.Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {
    int millis = 300;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        /*Parent fxmlLoader = FXMLLoader.load(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader);
        primaryStage.setScene(scene);
        primaryStage.show();*/

        GridPane gridPane = new GridPane();
        new Game(gridPane, millis);
        Scene scene = new Scene(gridPane, 700, 700);
        primaryStage.setTitle("Paint.IO");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @Override
    public void stop() {
        System.exit(0);
    }

}