package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

class ClueBoxes extends Pane {
    private Pane container;
    private int maxBoxCount;
    private boolean isHorizontal;
    private ClueField last;

    ClueBoxes(boolean isHorizontal, int maxBoxCount) {
        if (isHorizontal) {
            HBox hBox = new HBox(1);
            hBox.setPrefWidth(maxBoxCount * 30 + maxBoxCount);
            container = hBox;
        } else {
            VBox vBox = new VBox(1);
            vBox.setPrefHeight(maxBoxCount * 30 + maxBoxCount);
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

    int[] toIntArray() {
        int size = container.getChildren().size();
        int[] array = new int[size];
        for(int i = 0; i < size; i++) {
            array[i] = Integer.parseInt(((TextField) container.getChildren().get(i)).getText());
        }
        return array;
    }

    void setEditable(boolean value) {
        for(int i = 0; i < container.getChildren().size() - 1; i++) {
            ((TextField) container.getChildren().get(i)).setEditable(value);
        }

        if(last.getText().isEmpty()) {
            if(value) {
                container.getChildren().add(last);
            }
            else {
                container.getChildren().remove(last);
            }
        }
        else {
            last.setEditable(value);
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

    private class ClueField extends TextField {
        ClueField(String text) {
            this.setPrefSize(30, 30);
            this.setMaxHeight(Double.MAX_VALUE);
            this.setAlignment(Pos.CENTER);
            this.setText(text);
            this.setGrow(Priority.ALWAYS);

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