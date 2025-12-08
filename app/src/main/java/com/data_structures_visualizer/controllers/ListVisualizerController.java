package com.data_structures_visualizer.controllers;

import java.util.ArrayList;

import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ListVisualizerController {
    @FXML
    private AnchorPane visualization_area;

    @FXML
    private Slider speed_visualization_slider;

    @FXML
    private Label speed_visualization_label;

    private ArrayList<VisualNode> nodes;
    private ArrayList<Arrow> arrows;
    private double initiaWidthForNode = 20;
    private double squareSize = 50.0;
    private String listType = "";

    @FXML
    public void initialize(){
        nodes = new ArrayList<VisualNode>();
        arrows = new ArrayList<Arrow>();

        for(int i = 0; i < 10; ++i){
            nodes.add(i, new VisualNode(squareSize, squareSize, Integer.toString(i)));
            visualization_area.getChildren().add(nodes.get(i));

            if(i < 9){
                arrows.add(i, new Arrow(squareSize * 0.6));
                visualization_area.getChildren().add(arrows.get(i));
            }
        }

        fixVisualizationAreaLayout();

        speed_visualization_slider.valueProperty().addListener((obs, oldValue, newVal) -> {
            speed_visualization_label.setText(String.format("%.1f", 1 + newVal.doubleValue() / 100) + "x");
        });
    }

    private void fixVisualizationAreaLayout(){
        visualization_area.layoutBoundsProperty().addListener((obs, odlVal, newVal) -> {
            double height = newVal.getHeight();
            double width = newVal.getWidth();
            double value = height < width ? height : width;
            int count = 0;

            for(int i = 0; i < nodes.size(); ++i){
                if(value != 0){ 
                    squareSize = value * 0.08;
                    nodes.get(i).getRect().setWidth(squareSize); 
                    nodes.get(i).getRect().setHeight(squareSize);
                }
                
                AnchorPane.setTopAnchor(nodes.get(i), (height / 2) - (nodes.get(i).getRect().getHeight() / 2)); 
                AnchorPane.setLeftAnchor(nodes.get(i), initiaWidthForNode + ((1.6 * nodes.get(i).getRect().getWidth() * count)));

                if(i < arrows.size()){
                    double arrowLenght = 0.6 * nodes.get(i).getRect().getWidth();

                    arrows.get(i).setStrokeWidth(height * 0.003);
                    arrows.get(i).setLenght(arrowLenght);
                    
                    AnchorPane.setTopAnchor(arrows.get(i), (height / 2) - (arrows.get(i).getBoundsInParent().getHeight() / 2)); 
                    AnchorPane.setLeftAnchor(
                        arrows.get(i), 
                        (initiaWidthForNode + nodes.get(i).getRect().getWidth() + 
                        (1.6 * nodes.get(i).getRect().getWidth()) * count)
                    );
                }

                count++;
            }
        });
    }
}