package com.data_structures_visualizer;

import com.data_structures_visualizer.models.entities.CircularLinkedList;

public class Main {
  public static void main(String[] args) {
    // System.out.println("Hello, world!\n");  
    
    CircularLinkedList<String> list = new CircularLinkedList<String>("mamao");

    list.pushFront("uva");
    list.pushFront("melancia");
    list.pushFront("laranja");
    list.pushFront("acerola");

    list.removeItem("uva");
    list.print();
  }   
}   