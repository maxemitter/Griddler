package sample;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

class ClueBoxes extends Pane {
    private Pane container;
    private int maxBoxCount;

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
        addTextField();
        addTextField();
    }

    private void addTextField() {
        TextField txt = new TextField();
        txt.setPrefSize(30, 30);

        int size = container.getChildren().size();
        if(container instanceof HBox) {
            HBox.setHgrow(txt, Priority.NEVER);
            if(size > 0) {
                HBox.setHgrow(container.getChildren().get(size - 1), Priority.ALWAYS);
            }
        }
        else {
            txt.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(txt, Priority.NEVER);
            if(size > 0) {
                VBox.setVgrow(container.getChildren().get(size - 1), Priority.ALWAYS);
            }
        }

        txt.textProperty().addListener(e -> listener());
        txt.setTextFormatter(new TextFormatter<>(change -> {
            if(change.isAdded()) {
                return change.getControlNewText().matches("[0-9]{1,2}") ? change : null;
            }
            return change;
        }));

        container.getChildren().add(txt);
    }

    private void listener() {
        int size = container.getChildren().size();

        TextField last = (TextField) container.getChildren().get(size - 1);

        if(last.getText().isEmpty()) {
            for(int i = size - 2; i >= 1; i--) {
                if(!((TextField) container.getChildren().get(i)).getText().isEmpty()) {
                    if(container instanceof HBox) {
                        HBox.setHgrow(container.getChildren().get(i + 1), Priority.NEVER);
                    }
                    else {
                        VBox.setVgrow(container.getChildren().get(i + 1), Priority.NEVER);
                    }
                    break;
                }

                TextField toRemove = ((TextField) container.getChildren().get(i + 1));
                toRemove.textProperty().removeListener(e -> listener());
                container.getChildren().remove(toRemove);
            }
        }
        else {
            if(size < maxBoxCount) {
                addTextField();
            }
        }
    }
}
