package com.example.paintio.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Parent fxmlLoader = FXMLLoader.load(Main.class.getResource("game_pane.fxml"));
        Scene scene = new Scene(fxmlLoader, 700, 800);
        stage.setTitle("Paint_IO");
        stage.setScene(scene);
        stage.show();



        /*GridPane gridPane = new GridPane();
        new Game(gridPane, millis);
        Scene scene = new Scene(gridPane, 700, 700);
        primaryStage.setTitle("Paint.IO");
        primaryStage.setScene(scene);
        primaryStage.show();*/
    }
    @Override
    public void stop() {
        System.exit(0);
    }

}