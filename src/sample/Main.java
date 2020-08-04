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
    private boolean hasStarted;
    private ClueBoxes[] clueBoxes;
    private TextField txt_automaticTime;
    private int gridSize = 15;

    @Override
    public void start(Stage primaryStage) {
        isAutomatic = false;
        hasStarted = false;

        clueBoxes = new ClueBoxes[gridSize * 2];

        GridPane gridPane = new GridPane();

        GridPane controlGrid = new GridPane();

        Button btn_step = new Button("Next Step");
        btn_step.setOnAction(e -> {
            if(!hasStarted) {
                setStarted(true);
            }
            Solver.nextStep();
        });
        controlGrid.add(btn_step, 0, 0);

        btn_automatic = new Button("Start automatic step");
        btn_automatic.setOnAction(e -> btn_automaticAction());
        controlGrid.add(btn_automatic, 0, 3);

        controlGrid.add(new Label("Time between steps [ms]"), 0, 4);

        txt_automaticTime = new TextField("10");
        txt_automaticTime.setPrefWidth(50);
        controlGrid.add(txt_automaticTime, 1, 4);

        Button btn_reset = new Button("Reset");
        btn_reset.setOnAction(e -> {
            if(hasStarted) {
                setStarted(false);
            }
        });
        controlGrid.add(btn_reset, 0, 6);

        Button btn_changeGrid = new Button("Change grid size");
        controlGrid.add(btn_changeGrid, 0, 8);

        controlGrid.setPadding(new Insets(10));
        controlGrid.setHgap(5);
        controlGrid.setVgap(5);
        gridPane.add(controlGrid, 0, 0);

        Solver.initialize(gridSize);

        int maxBoxCount = (int) Math.ceil(gridSize / 2.0);
        //Add separately for better tab (focus) order
        for(int i = 1; i <= gridSize; i++) {
            clueBoxes[i - 1] = new ClueBoxes(true, maxBoxCount);
            gridPane.add(clueBoxes[i - 1], 0, i);
        }
        for(int i = 1; i <= gridSize; i++) {
            clueBoxes[gridSize + i - 1] = new ClueBoxes(false, maxBoxCount);
            gridPane.add(clueBoxes[gridSize + i - 1], i, 0);
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

            if(!hasStarted) {
                setStarted(true);
            }

            int delay = Integer.parseInt(txt_automaticTime.getText());
            new Thread(() -> {
                while(isAutomatic && !Solver.isFinished()) {
                    Solver.nextStep();

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else {
            btn_automatic.setText("Start automatic step");
        }
    }

    private void setStarted(boolean value) {
        hasStarted = value;

        for(ClueBoxes currentClueBox : clueBoxes) {
            currentClueBox.setEditable(!value);
        }

        if(value) {
            int[][][] clues = new int[2][gridSize][];

            for(int i = 0; i < gridSize; i++) {
                clues[0][i] = clueBoxes[i].toIntArray();
                clues[1][i] = clueBoxes[i + gridSize].toIntArray();
            }

            Solver.setClues(clues);
        }
        else {
            Solver.reset();
        }
    }
}
