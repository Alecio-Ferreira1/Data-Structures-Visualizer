package com.data_structures_visualizer.visual.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public final class QueueDelimiter extends Group{
    Line topLine = new Line();
    Line bottomLine = new Line();
    double width;
    double spacingBetweenLines;

    public QueueDelimiter(double width, double spacingBetweenLines, double strokeWidth){
        this.width = width;
        this.spacingBetweenLines = spacingBetweenLines;

        topLine.setStroke(Color.BLACK);
        bottomLine.setStroke(Color.BLACK);

        getChildren().addAll(topLine, bottomLine);
        update(width, spacingBetweenLines, strokeWidth);
    }

    public void update(double width, double spacingBetweenLines, double strokeWidth){
        this.width = width;
        this.spacingBetweenLines = spacingBetweenLines;

        topLine.setStrokeWidth(strokeWidth);
        topLine.setStartX(0);
        topLine.setStartY(0);
        topLine.setEndX(width);
        topLine.setEndY(0);  

        bottomLine.setStrokeWidth(strokeWidth);
        bottomLine.setStartX(0);
        bottomLine.setStartY(spacingBetweenLines);
        bottomLine.setEndX(width);
        bottomLine.setEndY(spacingBetweenLines); 
    }

    public double getWidth(){
        return width;
    }

    public double getSpacingBetweenLines(){
        return spacingBetweenLines;
    }
} 