package com.data_structures_visualizer.visual.context.list;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.controllers.ListVisualizerController.ListType;
import com.data_structures_visualizer.models.entities.CircularLinkedList;
import com.data_structures_visualizer.models.entities.DoublyLikedList;
import com.data_structures_visualizer.models.entities.SinglyLinkedList;

public final class InsertContext{
    private final int pos;
    private final int value;
    private final ListType listType;
    private final double nodeWidth;
    private final double xOffset;
    private final int intialListSize;
    private final SinglyLinkedList<Integer> singlyLinkedList;
    private final DoublyLikedList<Integer> doublyLikedList;
    private final CircularLinkedList<Integer> circularLinkedList;

    public InsertContext(
        int pos, int value, ListType listType, double width, double height, int intialListSize,
        SinglyLinkedList<Integer> singlyLinkedList, DoublyLikedList<Integer> doublyLikedList, 
        CircularLinkedList<Integer> circularLinkedList
    ){
        final double size = Math.min(width, height);

        this.value = value;
        this.pos = pos;
        this.listType = listType;
        this.nodeWidth = ListVisualizerConfig.squareSize * size;
        this.xOffset = ListVisualizerConfig.squareSize * size * (1 + ListVisualizerConfig.spacingBetweenNodes);
        this.intialListSize = intialListSize;
        this.singlyLinkedList = singlyLinkedList;
        this.doublyLikedList = doublyLikedList;
        this.circularLinkedList = circularLinkedList;
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

    public SinglyLinkedList<Integer> getSinglyLinkedList(){
        return singlyLinkedList;
    }

    public DoublyLikedList<Integer> getDoublyLikedList(){
        return doublyLikedList;
    }

    public CircularLinkedList<Integer> getCircularLinkedList(){
        return circularLinkedList;
    }
}