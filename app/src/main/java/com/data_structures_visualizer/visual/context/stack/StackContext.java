package com.data_structures_visualizer.visual.context.stack;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.models.entities.Stack;

public final class StackContext {
    private final Stack<Integer> stack;
    private final int valueToPush;
    private final double nodeWidth;
    private final double startX; 
    private final double startY; 
    private final double targetX;
    private final double targetY;
    private final double arcHeight;

    public StackContext(
        Stack<Integer> stack, int valueToPush, double height, double startX, 
        double startY, double targetX, double targetY, double arcHeight
    ){
        this.stack = stack;
        this.valueToPush = valueToPush;
        this.nodeWidth = ListVisualizerConfig.squareSize * height;
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.arcHeight = arcHeight;
    }

    public Stack<Integer> getStack() {
        return stack;
    }

    public int getValueToPush() {
        return valueToPush;
    }

    public double getNodeWidth() {
        return nodeWidth;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public double getArcHeight() {
        return arcHeight;
    }
}
