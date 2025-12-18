package com.data_structures_visualizer.visual.ui;

import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public final class Arrow extends Group{
    private Line line;
    private Polyline head;
    private double baseLenght;

    public Arrow(double lenght){
        baseLenght = lenght;
        line = new Line(0, 0, lenght, 0);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2.0);

        head = new Polyline();
        head.getPoints().setAll(lenght - 6, -6.0, lenght, 0.0, lenght - 6, 6.0);
        head.setStroke(Color.BLACK);
        head.setStrokeWidth(2.0);
        head.setFill(null);

        getChildren().addAll(line, head);
    }

    public Arrow(Arrow otherArrow){
        baseLenght = otherArrow.baseLenght;
        
        line = new Line(
            otherArrow.line.getStartX(),
            otherArrow.line.getStartY(),
            otherArrow.line.getEndX(),
            otherArrow.line.getEndY()
        );

        line.setStroke(otherArrow.line.getStroke());
        line.setStrokeWidth(otherArrow.line.getStrokeWidth());

        head = new Polyline();
        head.getPoints().addAll(otherArrow.head.getPoints());
        head.setStroke(otherArrow.head.getStroke());
        head.setStrokeWidth(otherArrow.head.getStrokeWidth());
        head.setFill(null);

        getChildren().addAll(line, head);
    }

    public void setStrokeWidth(double width){
        line.setStrokeWidth(width);  
        head.setStrokeWidth(width);      
    }

    public Line getLine(){
        return line;
    }

    public Polyline getHeadPolyline(){
        return head;
    }

    public double getBaseLenght(){
        return baseLenght;
    }

    public void setLenght(double lenght){
        baseLenght = lenght;
        update();
    }

    public void update(){
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(baseLenght);
        line.setEndY(0);

        double arrowSize = 0.2 * baseLenght;

        head.getPoints().setAll(
            baseLenght - arrowSize, -arrowSize, 
            baseLenght, 0.0,
            baseLenght - arrowSize, arrowSize 
        );
    }
}
