package com.mitmax.ui.fx;

import com.mitmax.logic.Solver;
import com.mitmax.ui.Cell;
import com.mitmax.ui.ClueBoxes;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.mitmax.ui.fx.SolverState.*;

public class Controller {
    public GridPane grp_root;
    public Button btn_step;
    public Button btn_automatic;
    public TextField txt_stepTime;
    public Button btn_startReset;
    public Button btn_changeSize;
    public Button btn_beginning;
    public Button btn_end;
    public Button btn_clear;

    private static Controller controller;
    private ClueBoxes[] clueBoxes;
    private final int initialGridSize = 15;
    private ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduledAutoStep;

    private final ObjectProperty<SolverState> solverState = new SimpleObjectProperty<>(INITIAL);

    @FXML
    public void initialize() {
        setButtonActions();

        solverState.addListener((observableValue, oldValue, newValue) -> solverStateListener(newValue));
        executor = Executors.newSingleThreadScheduledExecutor();

        txt_stepTime.setTextFormatter(new TextFormatter<>(c -> c.getText().matches("[0-9]*") ? c : null));
        txt_stepTime.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal && txt_stepTime.getText().isEmpty()) {
                txt_stepTime.setText("0");
            }
        });

        clueBoxes = new ClueBoxes[initialGridSize * 2];
        setClueBoxes(initialGridSize);

        Solver.initialize(initialGridSize);

        IntegerProperty[][] grid = Solver.getGrid();
        for(int i = 0; i < initialGridSize; i++) {
            for(int j = 0; j < initialGridSize; j++) {
                grp_root.add(new Cell(grid[i][j]), i + 1, j + 1);
            }
        }

        controller = this;
    }

    public GridPane getRoot() {
        return grp_root;
    }

    public int getGridSize() {
        return initialGridSize;
    }

    public static Controller getInstance() {
        return controller;
    }

    private void setButtonActions() {
        btn_startReset.setOnAction(e -> {
            if (solverState.getValue() == INITIAL) {
                if (start()) {
                    solverState.setValue(STARTED);
                }
            } else {
                if (solverState.getValue() == AUTOMATIC) {
                    scheduledAutoStep.cancel(false);
                }
                stop();
                solverState.setValue(INITIAL);
            }
        });

        btn_beginning.setOnAction(e -> {
            stop();
            start();
            solverState.setValue(STARTED);
        });

        btn_step.setOnAction(e -> {
            Solver.nextStep();
            solverState.setValue(Solver.isFinished() ? FINISHED : STEPPING);
        });

        btn_automatic.setOnAction(e -> {
            if (solverState.getValue() == AUTOMATIC) {
                scheduledAutoStep.cancel(false);
                solverState.setValue(Solver.isFinished() ? FINISHED : STEPPING);
            } else {
                int period = Integer.parseInt(txt_stepTime.getText());
                if (period > 0) {
                    scheduledAutoStep = executor.scheduleAtFixedRate(this::autoStep, 0, period, TimeUnit.MILLISECONDS);
                    solverState.setValue(AUTOMATIC);
                } else {
                    btn_end.fire();
                }
            }
        });

        btn_end.setOnAction(e -> {
            while (!Solver.isFinished()) {
                Solver.nextStep();
            }
            solverState.setValue(FINISHED);
        });

        btn_clear.setOnAction(e -> {
            new Alert(Alert.AlertType.NONE, "Not implemented", ButtonType.OK).showAndWait();
        });

        btn_changeSize.setOnAction(e -> {
            new Alert(Alert.AlertType.NONE, "Not implemented", ButtonType.OK).showAndWait();
        });
    }

    private void autoStep() {
        Solver.nextStep();

        if (Solver.isFinished()) {
            scheduledAutoStep.cancel(false);
            Platform.runLater(() -> solverState.setValue(FINISHED));
        }
    }

    private void solverStateListener(SolverState ss) {
        btn_startReset.setText(ss == INITIAL ? "Start" : "Reset");
        txt_stepTime.setDisable(ss == AUTOMATIC);
        btn_beginning.setDisable(ss == INITIAL || ss == STARTED || ss == AUTOMATIC);
        btn_step.setDisable(ss == INITIAL || ss == AUTOMATIC || ss == FINISHED);
        btn_automatic.setText(ss == AUTOMATIC ? "||" : "▶▶");
        btn_automatic.setDisable(ss == INITIAL || ss == FINISHED);
        btn_end.setDisable(ss == INITIAL || ss == AUTOMATIC || ss == FINISHED);
        btn_clear.setDisable(ss != INITIAL);
        btn_changeSize.setDisable(ss != INITIAL);

        if (ss == FINISHED) {
            btn_startReset.requestFocus();
        }
    }

    private void setClueBoxes(int size) {
        int maxBoxCount = (int) Math.ceil(size / 2.0);
        //Add separately for better tab (focus) order
        for (int i = 1; i <= size; i++) {
            clueBoxes[i - 1] = new ClueBoxes(true, maxBoxCount);
            grp_root.add(clueBoxes[i - 1], 0, i);
        }
        for (int i = 1; i <= size; i++) {
            clueBoxes[size + i - 1] = new ClueBoxes(false, maxBoxCount);
            grp_root.add(clueBoxes[size + i - 1], i, 0);
        }
    }

    private boolean start() {
        setClueBoxesEditable(false);

        int[][][] clues = new int[2][initialGridSize][];

        try {
            for(int i = 0; i < initialGridSize; i++) {
                clues[0][i] = clueBoxes[i].toIntArray();
                clues[1][i] = clueBoxes[i + initialGridSize].toIntArray();
            }
        } catch (IllegalArgumentException ex) {
            stop();
            return false;
        }

        Solver.setClues(clues);
        return true;
    }

    private void stop() {
        setClueBoxesEditable(true);
        Solver.reset();
    }

    private void setClueBoxesEditable(boolean editable) {
        for (ClueBoxes currentClueBox : clueBoxes) {
            currentClueBox.setEditable(editable);
        }
    }
}
