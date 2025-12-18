package com.data_structures_visualizer.models.entities;

public interface List<T> {
  void createList(Node<T> node);
  void pushFront(T value);
  void pushBack(T value);
  T get(int index);
  void print();
  void insertOnPos(T value, int pos);
  int lenght();
  void removeItem(int pos);
  void removeItem(T value);
  boolean isEmpty();
  void clear();
  int indexOf(T value);
}