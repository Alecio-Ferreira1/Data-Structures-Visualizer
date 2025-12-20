package com.data_structures_visualizer.visual.context.list;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.controllers.ListVisualizerController.ListType;
import com.data_structures_visualizer.models.entities.CircularLinkedList;
import com.data_structures_visualizer.models.entities.DoublyLinkedList;
import com.data_structures_visualizer.models.entities.SinglyLinkedList;

public abstract class Context {
    protected final ListType listType;
    protected final double xOffset;
    protected final SinglyLinkedList<Integer> singlyLinkedList;
    protected final DoublyLinkedList<Integer> doublyLinkedList;
    protected final CircularLinkedList<Integer> circularLinkedList;

    protected Context(
        ListType listType, double width, double height, SinglyLinkedList<Integer> singlyLinkedList,
        DoublyLinkedList<Integer> doublyLikedList, CircularLinkedList<Integer> circularLinkedList
    ){
        final double size = Math.min(width, height);

        this.listType = listType;
        this.singlyLinkedList = singlyLinkedList;
        this.doublyLinkedList = doublyLikedList;
        this.circularLinkedList = circularLinkedList;
        this.xOffset = ListVisualizerConfig.squareSize * size * (1 + ListVisualizerConfig.spacingBetweenNodes);
    }

    public ListType getListType() {
        return listType;
    }

    public double getxOffset() {
        return xOffset;
    }

    public SinglyLinkedList<Integer> getSinglyLinkedList() {
        return singlyLinkedList;
    }

    public DoublyLinkedList<Integer> getDoublyLikedList() {
        return doublyLinkedList;
    }

    public CircularLinkedList<Integer> getCircularLinkedList() {
        return circularLinkedList;
    }
}
