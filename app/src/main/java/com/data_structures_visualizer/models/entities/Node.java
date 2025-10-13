package com.data_structures_visualizer.models.entities;

public class Node<T> {
  private T value;
  private Node<T> next;
  private Node<T> prev;

  public Node(T value){
    this.value = value;
  }    

  public void setValue(T value){
    this.value = value;
  }

  public void setNext(Node<T> node){
    this.next = node;
  }

  public void setPrev(Node<T> node){
    this.prev = node;
  }

  public T getValue(){
    return value;
  }

  public Node<T> getNext(){
    return next;
  }

  public Node<T> getPrev(){
    return prev;
  }
}