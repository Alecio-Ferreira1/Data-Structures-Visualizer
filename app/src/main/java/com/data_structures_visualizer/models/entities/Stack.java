package com.data_structures_visualizer.models.entities;

public class Stack<T> {
  private Node<T> top;
  private int lenght = 0;

  public Stack(T startValue){
    top = startValue == null ? null : new Node<T>(startValue);
    lenght = startValue == null ? 0 : 1;
  }

  public void push(T value){
    if(value == null) return;

    Node<T> node = new Node<T>(value);

    if(isEmpty()){
      top = new Node<T>(value);
      lenght = 1;
      return;
    } 

    node.setNext(top);
    top = node;
    ++lenght;
  }

  public T pop(){
    if(top == null) return null;

    Node<T> removed = top;

    top = top.getNext();
    --lenght;

    return removed.getValue();
  }

  public T peek(){
    return top == null ? null : top.getValue();
  }

  public boolean isEmpty(){
    return lenght == 0;
  }

  public void clear(){
    top = null;
    lenght = 0;
  }
}