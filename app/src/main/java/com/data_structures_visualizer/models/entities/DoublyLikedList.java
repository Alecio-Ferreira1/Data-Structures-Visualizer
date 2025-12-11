package com.data_structures_visualizer.models.entities;

public class DoublyLikedList<T> implements List<T> {
  private Node<T> head;
  private Node<T> tail;
  private int lenght = 0;

  public DoublyLikedList(T startValue){
    if(startValue == null) return;

    createList(new Node<T>(startValue));
  }

  @Override
  public void createList(Node<T> node){
    head = node;
    tail = node;
    lenght = 1;
  }

  @Override
  public void pushFront(T value){
    if(value == null) return;

    Node<T> node = new Node<T>(value);

    if(head == null){
      createList(node);
      return;
    }

    head.setPrev(node);
    node.setNext(head);
    head = node;
    ++lenght;
  }

  @Override
  public void pushBack(T value){
    if(value == null) return;

    Node<T> node = new Node<T>(value);

    if(head == null){
      createList(node);
      return;
    }

    node.setPrev(tail);
    tail.setNext(node);
    tail = node;
    ++lenght;
  }

  @Override
  public void print(){
    if(head == null) return;

    System.out.println();

    for(Node<T> node = head; node != null; node = node.getNext()){
      System.out.printf("%s -> ", node.getValue() != null ? node.getValue().toString() : "null");
    }

    System.out.println("NULL");
  }

  public void printReverse(){
    System.out.println();

    for(Node<T> node = tail; node != null; node = node.getPrev()){
      System.out.printf("%s -> ", node.getValue() != null ? node.getValue().toString() : "null");
    }

    System.out.println("NULL");
  }

  @Override
  public void insertOnPos(T value, int pos){
    if(value == null) return;

    Node<T> node = new Node<T>(value);

    if(head == null){
      createList(node);
      return;
    }

    if(pos == 0){
      node.setNext(head);
      head.setPrev(node);
      head = node;
      ++lenght;
      return;
    }

    Node<T> tmp = head;

    for(int i = 0; i < pos && tmp.getNext() != null; ++i){
      tmp = tmp.getNext();
    }

    Node<T> tmpPrev = tmp.getPrev();

    if(pos >= lenght){
      tmpPrev.setNext(node);
      node.setPrev(tmpPrev);
      tail = node;
    }

    else{
      node.setNext(tmp);
      node.setPrev(tmpPrev);
      tmpPrev.setNext(node);
    }

    ++lenght;
  }

  @Override
  public int lenght(){
    return lenght;
  }

  @Override
  public void removeItem(int pos){
    if(head == null) return;

    if(pos == 0){
      head = head.getNext();
      head.setPrev(null);
      --lenght;
      return;
    }

    Node<T> tmp = head;
  
    for(int i = 0; i < pos && tmp.getNext() != null; ++i){
      tmp = tmp.getNext();
    }

    if(pos >= lenght) return;

    Node<T> tmpPrev = tmp.getPrev(); 
    Node<T> tmpNext = tmp.getNext();

    if(tmpNext != null){
      tmpNext.setPrev(tmpPrev); 
    }  

    else{
      tail = tmpPrev; 
    }

    tmpPrev.setNext(tmpNext);
    --lenght;
  }

  @Override
  public void removeItem(T value){
    if(head == null) return;

    if(head.getValue().equals(value)){
      head = head.getNext();
      head.setPrev(null);
      --lenght;
      return;
    }

    Node<T> tmp = head;
  
    while(tmp != null && !tmp.getValue().equals(value)){
      tmp = tmp.getNext();
      --lenght;
    }

    if(tmp == null) return;

    Node<T> tmpPrev = tmp.getPrev(); 
    Node<T> tmpNext = tmp.getNext();

    if(tmpNext != null){
      tmpNext.setPrev(tmpPrev); 
    }  

    else{
      tail = tmpPrev; 
    }

    tmpPrev.setNext(tmpNext);
    --lenght;
  }

  @Override
  public T get(int index) {
    Node<T> target = head;
    int i = index;

    while(i > 0 && target != null){
      target = target.getNext();
      --i;
    }

    return i != 0 ? null : target.getValue();
  }

  @Override
  public boolean isEmpty() {
    return lenght == 0;
  }

  @Override
  public void clear() {
    head = null;
    tail = null;
    lenght = 0;
  }
}