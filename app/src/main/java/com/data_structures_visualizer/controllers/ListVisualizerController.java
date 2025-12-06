package com.data_structures_visualizer.controllers;

import java.util.ArrayList;

import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public class ListVisualizerController {
    @FXML
    private AnchorPane visualization_area;

    @FXML
    private Slider speed_visualization_slider;

    @FXML
    private Label speed_visualization_label;

    private ArrayList<VisualNode> nodes;
    private double startWidth = 20;
    private double startRecSize = 50.0;

    @FXML
    public void initialize(){
        nodes = new ArrayList<VisualNode>();

        for(int i = 0; i < 10; ++i){
            nodes.add(i, new VisualNode(startRecSize, startRecSize, Integer.toString(i)));
            visualization_area.getChildren().add(nodes.get(i));
        }

        fixNodeLayouts();
    }

    private void fixNodeLayouts(){
        visualization_area.layoutBoundsProperty().addListener((obs, odlVal, newVal) -> {
            int count = 0;

            for(VisualNode node : nodes){ 
                double height = newVal.getHeight();
                double width = newVal.getWidth();
                double value = height > width ? height : width;

                if(value != 0){ 
                    node.getRect().setWidth(value * 0.05); 
                    node.getRect().setHeight(value * 0.05); 
                }

                double recSize = node.getRect().getWidth();

                AnchorPane.setTopAnchor(node, (height / 2) - (node.getRect().getHeight() / 2)); 
                AnchorPane.setLeftAnchor(node, startWidth + ((1.6 * recSize * count)));

                count++;
            } 
        });
    }
}