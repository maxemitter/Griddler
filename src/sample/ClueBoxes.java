package sample;

import javafx.geometry.Insets;
import javafx.scene.Node;
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
        addTextField();
        addTextField();
    }

    private void addTextField() {
        TextField txt = new TextField();
        txt.setPrefSize(30, 30);
        txt.setMaxHeight(Double.MAX_VALUE);

        int size = container.getChildren().size();
        setGrow(txt, Priority.NEVER);
        if(size > 0) {
            setGrow(container.getChildren().get(size - 1), Priority.ALWAYS);
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
                    break;
                }

                TextField toRemove = ((TextField) container.getChildren().get(i + 1));
                toRemove.textProperty().removeListener(e -> listener());
                //It is possible to remove items from the list while iterating over it
                //because we iterate from the back to the front
                container.getChildren().remove(toRemove);
            }

            //Use call to size() because size might have changed since last call
            setGrow(container.getChildren().get(container.getChildren().size() - 1), Priority.NEVER);
        }
        else {
            if(size < maxBoxCount) {
                addTextField();
            }
        }
    }

    private void setGrow(Node node, Priority priority) {
        if(isHorizontal) {
            HBox.setHgrow(node, priority);
        }
        else {
            VBox.setVgrow(node, priority);
        }
    }
}
