package com.data_structures_visualizer.controllers;

import com.data_structures_visualizer.util.SceneManager;
import com.data_structures_visualizer.visual.ui.StackBase;

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

    private final StackBase stackBase = new StackBase();

    @FXML
    public void initialize(){
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
        
    }
}
