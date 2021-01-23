package com.mitmax.ui;

import com.mitmax.ui.fx.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ClueBoxes extends Pane {
    private final Pane container;
    private final int maxBoxCount;
    private final boolean isHorizontal;
    private final ClueField last;

    public ClueBoxes(boolean isHorizontal, int maxBoxCount) {
        int prefSize = maxBoxCount * 30 + maxBoxCount;
        if (isHorizontal) {
            HBox hBox = new HBox(1);
            hBox.setPrefWidth(prefSize);
            container = hBox;
        } else {
            VBox vBox = new VBox(1);
            vBox.setPrefHeight(prefSize);
            container = vBox;
        }
        this.setPadding(new Insets(1));
        this.getChildren().add(container);
        this.maxBoxCount = maxBoxCount;
        this.isHorizontal = isHorizontal;

        last = new ClueField();
        last.textProperty().addListener(e -> update());
        last.setGrow(Priority.NEVER);
        container.getChildren().add(last);
        addTextField("");
    }

    public int[] toIntArray() {
        int size = container.getChildren().size();
        int[] array = new int[size];
        int sum = 0;

        for(int i = 0; i < size; i++) {
            TextField txt = (TextField) container.getChildren().get(i);
            try {
                array[i] = Integer.parseInt(txt.getText());
                sum += array[i];
            } catch (NumberFormatException ex) {
                highlight(txt, true);
                throw new IllegalArgumentException();
            }
        }

        if(sum + size - 1 > Controller.getInstance().getGridSize()) {
            highlight(null, true);
            throw new IllegalArgumentException();
        }

        return array;
    }

    public void setEditable(boolean value) {
        for(int i = 0; i < container.getChildren().size(); i++) {
            ((TextField) container.getChildren().get(i)).setEditable(value);
        }

        last.setEditable(value);

        if(last.getText().isEmpty()) {
            if(value) {
                container.getChildren().add(last);
            }
            else {
                container.getChildren().remove(last);
            }
        }
    }

    private void update() {
        int size = container.getChildren().size();
        if(!last.getText().isEmpty() && size < maxBoxCount) {
            TextField first = (TextField) container.getChildren().get(0);
            if(first.getText().isEmpty()) {
                first.setText(last.getText());
                first.requestFocus();
                first.end();
            }
            else {
                addTextField(last.getText());
            }
            last.clear();
        }
    }

    private void addTextField(String text) {
        ClueField txt = new ClueField(text);
        txt.addFocusListener();

        container.getChildren().add(container.getChildren().size() - 1, txt);
        txt.requestFocus();
        txt.end();
    }

    private void highlight(Node node, boolean value) {
        String style = value ? "-fx-text-box-border: red; -fx-focus-color: red;" : "";

        if(node == null) {
            for(int i = 0; i < container.getChildren().size(); i++) {
                container.getChildren().get(i).setStyle(style);
            }
        }
        else {
            node.setStyle(style);
            node.requestFocus();
        }
    }

    private class ClueField extends TextField {
        ClueField(String text) {
            this.setPrefSize(30, 30);
            this.setMaxHeight(Double.MAX_VALUE);
            this.setAlignment(Pos.CENTER);
            this.setText(text);
            this.setGrow(Priority.ALWAYS);

            this.textProperty().addListener((observable, oldValue, newValue) -> highlight(null, false));
            addTextFormatter();
        }

        ClueField() {
            this("");
        }

        void addFocusListener() {
            this.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if(!newValue && this.getText().isEmpty() && container.getChildren().size() > 2) {
                    container.getChildren().remove(this);
                    update();
                }
            });
        }

        void setGrow(Priority priority) {
            if(isHorizontal) {
                HBox.setHgrow(this, priority);
            }
            else {
                VBox.setVgrow(this, priority);
            }
        }

        private void addTextFormatter() {
            this.setTextFormatter(new TextFormatter<>(change -> {
                if(change.isAdded()) {
                    return change.getControlNewText().matches("[0-9]{1,2}") ? change : null;
                }
                return change;
            }));
        }
    }
}