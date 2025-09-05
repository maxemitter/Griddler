package com.mitmax.ui;

import javafx.beans.property.IntegerProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Cell extends StackPane {
    private final StackPane cross;

    public Cell(IntegerProperty bounded) {
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));

        cross = new StackPane(
                new Line(10, 10, this.getWidth() - 10, this.getHeight() - 10),
                new Line(this.getWidth() - 10, 10, 10, this.getHeight() - 10)
        );
        cross.setVisible(false);
        cross.setTranslateX(this.getWidth() / 2);
        cross.setTranslateY(this.getHeight() / 2);
        this.getChildren().add(cross);

        bounded.addListener(e -> {
            Color color = Color.WHITE;
            boolean showCross = false;

            if (bounded.get() == 1) {
                color = Color.BLACK;
            } else if (bounded.get() == -1) {
                showCross = true;
            }

            this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, null)));
            cross.setVisible(showCross);
        });
    }
}
