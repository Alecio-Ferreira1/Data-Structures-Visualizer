package com.data_structures_visualizer.visual.ui;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public final class VisualNode extends StackPane {
    private Rectangle rect;
    private Text text;

    public VisualNode(double width, double height, String labelText){
        createRectangle(width, height);
        drawText(labelText);
        getChildren().addAll(rect, text);
    }

    private void drawText(String labelText){
        text = new Text(labelText);
        text.setFont(new Font(rect.getHeight() / 4));
        text.setFill(Color.BLACK);
    }

    private void createRectangle(double width, double height){
        rect = new Rectangle(width, height);
        rect.setFill(Color.rgb(0, 255, 0));
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(0.05 * width);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void update(double width, double height, double strokeWidth){
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setStrokeWidth(strokeWidth);
    }
}