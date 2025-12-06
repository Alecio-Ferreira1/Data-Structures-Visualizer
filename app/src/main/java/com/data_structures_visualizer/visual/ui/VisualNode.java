package com.data_structures_visualizer.visual.ui;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class VisualNode extends StackPane {
    private Rectangle rect;
    private Text text;

    public VisualNode(double width, double height, String labelText){
        createRectangle(width, height);
        drawText(labelText);
        getChildren().addAll(rect, text);
    }

    private void drawText(String labelText){
        text = new Text(labelText);
        text.setFont(new Font(rect.getHeight() / 5));
        text.setFill(Color.BLACK);
    }

    private void createRectangle(double width, double height){
        rect = new Rectangle(width, height);
        rect.setFill(Color.rgb(0, 255, 0));
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setStroke(Color.BLACK);
    }

    public Rectangle getRect() {
        return rect;
    }
}