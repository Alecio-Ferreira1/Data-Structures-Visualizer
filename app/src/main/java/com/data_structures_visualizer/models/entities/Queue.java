package com.data_structures_visualizer.models.entities;

public class Queue<T> {
  private Node<T> first;
  private Node<T> last;
  private int lenght = 0;

  public Queue(T startValue){
    if(startValue == null) return;

    Node<T> node = new Node<T>(startValue);
    createQueue(node);
  }

  private void createQueue(Node<T> node){
    first = node;
    last = node;
    lenght = 1;
  }

  public void enqueue(T value){
    if(value == null) return;

    Node<T> node = new Node<T>(value);

    if(isEmpty()){
      createQueue(node);
      return;
    }

    last.setNext(node);
    last = node;
    ++lenght;
  }

  public T dequeue(){
    if(first == null) return null;

    T removed = first.getValue();

    first = first.getNext();
    --lenght;

    return removed;
  }

  public T peek(){
    return first == null ? null : first.getValue();
  }

  public boolean isEmpty(){
    return lenght == 0;
  }

  public void clear(){
    first = null;
    last = null;
    lenght = 0;
  }
}