package sample;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

class ClueBoxes extends Pane {
    private Pane container;

    ClueBoxes(boolean isHorizontal) {
        container = isHorizontal ? new HBox() : new VBox();
        this.getChildren().add(container);
        addTextField();
        addTextField();
    }

    private void addTextField() {
        TextField txt = new TextField();
        txt.setPrefWidth(30);

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
                container.getChildren().remove(toRemove);
            }
        }
        else {
            addTextField();
        }
    }
}
