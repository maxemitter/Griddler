package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Cell extends Pane {
    private IntegerProperty value;

    Cell(IntegerProperty bounded) {
        value = new SimpleIntegerProperty();
        value.bind(bounded);

        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setPrefWidth(50);
        this.setPrefHeight(50);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));

        value.addListener(e -> {
            Color color = Color.BLUE;
            switch (value.get()) {
                case -1:
                    color = Color.RED;
                break;
                case 0:
                    color = Color.WHITE;
                break;
                case 1:
                    color = Color.BLACK;
                break;
            }
            this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, null)));
        });
    }
}
