package com.ceos.widgetfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Widget widget = new Widget();
        Scene scene = new Scene(widget, 400, 400);
        primaryStage.setTitle("Widget Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
