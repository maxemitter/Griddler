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

        int[][][] clues = new int[2][gridSize][];
        clues[0][0] = new int[]{1, 3};
        clues[0][1] = new int[]{1, 1};
        clues[0][2] = new int[]{1, 1};
        clues[0][3] = new int[]{2, 2};
        clues[0][4] = new int[]{1, 1, 3};
        clues[0][5] = new int[]{1, 2, 5};
        clues[0][6] = new int[]{1, 9};
        clues[0][7] = new int[]{13};
        clues[0][8] = new int[]{10, 1};
        clues[0][9] = new int[]{10};
        clues[0][10] = new int[]{2, 3, 5};
        clues[0][11] = new int[]{1, 3, 3, 1};
        clues[0][12] = new int[]{4, 6, 1};
        clues[0][13] = new int[]{8, 1, 1};
        clues[0][14] = new int[]{1, 1, 3, 1};

        clues[1][0] = new int[]{1};
        clues[1][1] = new int[]{4};
        clues[1][2] = new int[]{5, 2, 3};
        clues[1][3] = new int[]{3, 3, 3};
        clues[1][4] = new int[]{1, 8};
        clues[1][5] = new int[]{7, 2};
        clues[1][6] = new int[]{8, 3};
        clues[1][7] = new int[]{2, 4, 3};
        clues[1][8] = new int[]{2, 9};
        clues[1][9] = new int[]{1, 8};
        clues[1][10] = new int[]{1, 9};
        clues[1][11] = new int[]{7, 3};
        clues[1][12] = new int[]{5, 2};
        clues[1][13] = new int[]{1, 2};
        clues[1][14] = new int[]{2};

        Solver.initialize(gridSize, clues);

        //Add separately for better tab (focus) order
        for(int i = 1; i <= gridSize; i++) {
            gridPane.add(new ClueBoxes(true), 0, i);
        }
        for(int i = 1; i <= gridSize; i++) {
            gridPane.add(new ClueBoxes(false), i, 0);
        }

        IntegerProperty[][] grid = Solver.getGrid();
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                gridPane.add(new Cell(grid[i][j]), i + 1, j + 1);
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
