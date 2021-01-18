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
        FXMLLoader fxmlLoader = new FXMLLoader();
        Controller controller = fxmlLoader.getController();
        Parent root = fxmlLoader.load(getClass().getResource("ui/fx/layout.fxml").openStream());

        primaryStage.setTitle("Nonogram Solver");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        controller.getRoot().requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
