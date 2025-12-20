package com.data_structures_visualizer.visual.context.list;

import com.data_structures_visualizer.controllers.ListVisualizerController.ListType;
import com.data_structures_visualizer.models.entities.CircularLinkedList;
import com.data_structures_visualizer.models.entities.DoublyLinkedList;
import com.data_structures_visualizer.models.entities.SinglyLinkedList;

public final class DeleteContext extends Context {
    private final int value;
    private final boolean removeByIndex;
    private int indexToRemove;

    public DeleteContext(
        int value, ListType listType, double width, double height, int intialListSize,
        boolean removeByIndex, SinglyLinkedList<Integer> singlyLinkedList, 
        DoublyLinkedList<Integer> doublyLinkedList, CircularLinkedList<Integer> circularLinkedList
    ){
        super(listType, width, height, singlyLinkedList, doublyLinkedList, circularLinkedList);
        
        this.value = value;
        this.removeByIndex = removeByIndex;
    }

    public int getValue() {
        return value;
    }

    public boolean removeByIndex(){
        return removeByIndex;
    }

    public void setIndexToRemove(int index){
        indexToRemove = index;
    }

    public int getIndexToRemove(){
        return indexToRemove;
    }
}
