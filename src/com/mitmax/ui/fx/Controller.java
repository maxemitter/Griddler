package com.mitmax.ui.fx;

import com.mitmax.logic.Solver;
import com.mitmax.ui.Cell;
import com.mitmax.ui.ClueBoxes;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    public GridPane grp_root;
    public Button btn_step;
    public Button btn_automatic;
    public TextField txt_stepTime;
    public Button btn_reset;
    public Button btn_changeSize;

    private ClueBoxes[] clueBoxes;
    private final int initialGridSize = 15;
    private boolean isAutomatic;
    private boolean hasStarted;
    private Timer automaticTimer;
    private TimerTask automaticTimerTask;

    @FXML
    public void initialize() {
        setActions();

        clueBoxes = new ClueBoxes[initialGridSize * 2];
        setClueBoxes(initialGridSize);

        Solver.initialize(initialGridSize);

        IntegerProperty[][] grid = Solver.getGrid();
        for(int i = 0; i < initialGridSize; i++) {
            for(int j = 0; j < initialGridSize; j++) {
                grp_root.add(new Cell(grid[i][j]), i + 1, j + 1);
            }
        }
    }

    public GridPane getRoot() {
        return grp_root;
    }

    private void setClueBoxes(int size) {
        int maxBoxCount = (int) Math.ceil(size / 2.0);
        //Add separately for better tab (focus) order
        for(int i = 1; i <= size; i++) {
            clueBoxes[i - 1] = new ClueBoxes(true, maxBoxCount);
            grp_root.add(clueBoxes[i - 1], 0, i);
        }
        for(int i = 1; i <= size; i++) {
            clueBoxes[size + i - 1] = new ClueBoxes(false, maxBoxCount);
            grp_root.add(clueBoxes[size + i - 1], i, 0);
        }
    }

    private void setActions() {
        btn_step.setOnAction(e -> {
            if(!hasStarted) {
                setStarted(true);
            }
            Solver.nextStep();
        });

        automaticTimer = new Timer("AutomaticTimer", true);
        automaticTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(!isAutomatic) {
                    this.cancel();
                }

                Solver.nextStep();

                if(Solver.isFinished()) {
                    this.cancel();
                    Platform.runLater(() -> {
                        toggleAutomatic();
                        btn_step.setDisable(true);
                        btn_automatic.setDisable(true);
                        grp_root.requestFocus();
                    });
                }
            }
        };
        btn_automatic.setOnAction(e -> btn_automaticAction());

        btn_reset.setOnAction(e -> {
            if(hasStarted) {
                setStarted(false);
            }

            btn_step.setDisable(false);
            btn_automatic.setDisable(false);
        });
    }

    private void btn_automaticAction() {
        toggleAutomatic();

        if(isAutomatic) {
            if(!hasStarted) {
                setStarted(true);
            }

            automaticTimer.schedule(automaticTimerTask, 0, Integer.parseInt(txt_stepTime.getText()));
        }
    }

    private void toggleAutomatic() {
        isAutomatic = !isAutomatic;
        btn_step.setDisable(isAutomatic);
        txt_stepTime.setDisable(isAutomatic);
        btn_reset.setDisable(isAutomatic);
        btn_changeSize.setDisable(isAutomatic);
        btn_automatic.setText((isAutomatic ? "Stop" : "Start") + " automatic step");
    }

    private void setStarted(boolean value) {
        hasStarted = value;

        for(ClueBoxes currentClueBox : clueBoxes) {
            currentClueBox.setEditable(!value);
        }

        if(value) {
            int[][][] clues = new int[2][initialGridSize][];

            for(int i = 0; i < initialGridSize; i++) {
                clues[0][i] = clueBoxes[i].toIntArray();
                clues[1][i] = clueBoxes[i + initialGridSize].toIntArray();
            }

            Solver.setClues(clues);
        }
        else {
            Solver.reset();
        }
    }
}
