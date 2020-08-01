package sample;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    private boolean isAutomatic;
    private Button btn_automatic;

    @Override
    public void start(Stage primaryStage) {
        int gridSize = 15;
        isAutomatic = false;

        GridPane gridPane = new GridPane();

        GridPane controlGrid = new GridPane();

        Button btn_step = new Button("Next Step");
        btn_step.setOnAction(e -> Solver.nextStep());
        controlGrid.add(btn_step, 0, 0);

        btn_automatic = new Button("Start automatic step");
        btn_automatic.setOnAction(e -> btn_automaticAction());
        controlGrid.add(btn_automatic, 0, 3);

        controlGrid.add(new Label("Time between steps [ms]"), 0, 4);

        TextField txt_automaticTime = new TextField("10");
        txt_automaticTime.setPrefWidth(50);
        controlGrid.add(txt_automaticTime, 1, 4);

        Button btn_reset = new Button("Reset");
        controlGrid.add(btn_reset, 0, 6);

        Button btn_changeGrid = new Button("Change grid size");
        controlGrid.add(btn_changeGrid, 0, 8);

        controlGrid.setPadding(new Insets(10));
        controlGrid.setHgap(5);
        controlGrid.setVgap(5);
        gridPane.add(controlGrid, 0, 0);

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

        int maxBoxCount = (int) Math.ceil(gridSize / 2.0);
        //Add separately for better tab (focus) order
        for(int i = 1; i <= gridSize; i++) {
            gridPane.add(new ClueBoxes(true, maxBoxCount), 0, i);
        }
        for(int i = 1; i <= gridSize; i++) {
            gridPane.add(new ClueBoxes(false, maxBoxCount), i, 0);
        }

        IntegerProperty[][] grid = Solver.getGrid();
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                gridPane.add(new Cell(grid[i][j]), i + 1, j + 1);
            }
        }

        gridPane.setPadding(new Insets(10));
        primaryStage.setTitle("Nonogram-Solver");
        primaryStage.setScene(new Scene(gridPane, 800, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void btn_automaticAction() {
        isAutomatic = !isAutomatic;

        if(isAutomatic) {
            btn_automatic.setText("Stop automatic step");
        }
        else {
            btn_automatic.setText("Start automatic step");
        }
    }
}
