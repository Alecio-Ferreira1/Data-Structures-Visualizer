package com.data_structures_visualizer.visual.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;

public final class CurvedArrow extends Group {
    private QuadCurve curve;
    private Line arrow1;
    private Line arrow2;

    public CurvedArrow(double startX, double startY, double endX, double endY){
        curve = new QuadCurve();

        curve.setStroke(Color.BLACK);
        curve.setStrokeWidth(2.0);
        curve.setFill(null);

        arrow1 = new Line();
        arrow2 = new Line();

        arrow1.setStroke(Color.BLACK);
        arrow1.setStrokeWidth(2.0);
        arrow2.setStroke(Color.BLACK);
        arrow2.setStrokeWidth(2);

        getChildren().addAll(curve, arrow1, arrow2);
        update(startX, startY, endX, endY);
    }

    private void updateArrow(){
        double startX = curve.getStartX();
        double startY = curve.getStartY(); 
        double endX = curve.getEndX();
        double endY = curve.getEndY();

        double angle = Math.atan2(endY - curve.getControlY(), endX - curve.getControlX());
        double size = 0.02 * Math.hypot(endX - startX, endY - startY);
        double halfAngle = 0.5;

        double x1 = endX - size * Math.cos(angle - halfAngle);
        double y1 = endY - size * Math.sin(angle - halfAngle);
        double x2 = endX - size * Math.cos(angle + halfAngle);
        double y2 = endY - size * Math.sin(angle + halfAngle);
        
        arrow1.setStartX(endX);
        arrow1.setStartY(endY);
        arrow1.setEndX(x1);
        arrow1.setEndY(y1);
        
        arrow2.setStartX(endX);
        arrow2.setStartY(endY);
        arrow2.setEndX(x2);
        arrow2.setEndY(y2);
    }

    public void update(double startX, double startY, double endX, double endY){
        curve.setStartX(startX);
        curve.setStartY(startY);
        curve.setEndX(endX + 5);
        curve.setEndY(endY + 5);

        curve.setControlX((startX + endX) / 2);
        curve.setControlY(startY + (0.3 * (startX - endX)));

        updateArrow();
    }
}
