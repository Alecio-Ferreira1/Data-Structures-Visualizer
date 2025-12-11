package com.data_structures_visualizer.controllers;

import java.util.ArrayList;

import com.data_structures_visualizer.util.SceneManager;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.StackBase;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public final class StackVisualizerController {
    @FXML
    private AnchorPane visualization_area;
    @FXML
    private Slider speed_visualization_slider;
    @FXML
    private Label speed_visualization_label;
    @FXML 
    private Button list_btn;
    @FXML
    private Button stack_btn; 
    @FXML
    private Button queue_btn;

    private StackBase stackBase;
    private final ArrayList<VisualNode> nodes = new ArrayList<VisualNode>();
    private final double squareSize = 0.08;
    private final double spacingBetweenNodes = 0.15;

    @FXML
    public void initialize(){
        for(int i = 0; i < 9; ++i){  //SÓ PARA TESTE
            nodes.add(i, new VisualNode(625 * squareSize, 625 * squareSize, Integer.toString(i)));
            visualization_area.getChildren().add(nodes.get(i));
        } //SÓ PARA TESTE

        handleToScreenChange();

        visualization_area.layoutBoundsProperty().addListener((obs, odlVal, newVal) -> {
            fixVisualizationAreaLayout(newVal.getWidth(), newVal.getHeight());
        });

        speed_visualization_slider.valueProperty().addListener((obs, oldValue, newVal) -> {
            speed_visualization_label.setText(String.format("%.1f", 1 + newVal.doubleValue() / 100) + "x");
        });
    }

    private void handleToScreenChange(){
        list_btn.setOnAction(e -> {
            SceneManager.changeScene("/fxml/ListVisualizerScreen.fxml");
        });

        queue_btn.setOnAction(e -> {
            SceneManager.changeScene("/fxml/QueueVisualizerScreen.fxml");
        });
    }

    private void fixVisualizationAreaLayout(double width, double height){
        double updatedWidth = 0.15 * width;
        double updatedHeight = 0.85 * height;
        double updatedStroke = 0.004 * width;

        if(stackBase == null){
            stackBase = new StackBase(updatedWidth, updatedHeight, updatedStroke);
            visualization_area.getChildren().add(stackBase);
        } 

        stackBase.update(updatedWidth, updatedHeight, updatedStroke);
        AnchorPane.setTopAnchor(stackBase, (height / 2) - (stackBase.getHeight() / 2));
        AnchorPane.setLeftAnchor(stackBase, (width / 2) - (stackBase.getWidth() / 2));

        double yOffset = 0.015 * height;
        double initialHeight = updatedHeight - yOffset;
        
        for(int i = 0; i < nodes.size(); ++i){
            nodes.get(i).update(height * squareSize, height * squareSize, height * 0.005);
            
            AnchorPane.setTopAnchor(
                nodes.get(i), 
                initialHeight - ((1 + spacingBetweenNodes) * nodes.get(i).getRect().getHeight() * i)
            );

            AnchorPane.setLeftAnchor(
                nodes.get(i), 
                (width / 2) - (nodes.get(i).getRect().getWidth()) / 2
            );
        }
    }
}
