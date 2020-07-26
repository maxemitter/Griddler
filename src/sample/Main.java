package sample;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        int gridSize = 15;
        VBox root = new VBox();
        GridPane gridPane = new GridPane();

        Button btn_step = new Button("Next Step");
        btn_step.setOnAction(e -> Solver.nextStep());

        Solver.initialize(gridSize);
        IntegerProperty[][] grid = Solver.getGrid();

        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                gridPane.add(new Cell(grid[i][j]), i, j);
            }
        }

        root.getChildren().addAll(btn_step, gridPane);

        primaryStage.setTitle("Nonogram-Solver");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
