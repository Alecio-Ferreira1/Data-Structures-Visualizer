package com.data_structures_visualizer.visual.context;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.controllers.ListVisualizerController.ListType;
public class ListInsertContext{
    private final int pos;
    private final int value;
    private final ListType listType;
    private final double nodeWidth;
    private final double xOffset;
    private final int intialListSize;

    public ListInsertContext(int pos, int value, ListType listType, double width, double height, int intialListSize){
        final double size = Math.min(width, height);

        this.value = value;
        this.pos = pos;
        this.listType = listType;
        this.nodeWidth = ListVisualizerConfig.squareSize * size;
        this.xOffset = ListVisualizerConfig.squareSize * size * (1 + ListVisualizerConfig.spacingBetweenNodes);
        this.intialListSize = intialListSize;
    }

    public int getPos() {
        return pos;
    }

    public int getValue() {
        return value;
    }

    public ListType getListType() {
        return listType;
    }

    public double getNodeWidth() {
        return nodeWidth;
    }

    public double getxOffset() {
        return xOffset;
    } 

    public int getInitialListSize(){
        return intialListSize;
    }
}