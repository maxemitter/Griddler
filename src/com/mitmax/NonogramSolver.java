package com.mitmax;

import com.mitmax.ui.fx.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NonogramSolver extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui/fx/layout.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();

        primaryStage.setTitle("Griddler - Nonogram Solver");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        controller.getRoot().requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
