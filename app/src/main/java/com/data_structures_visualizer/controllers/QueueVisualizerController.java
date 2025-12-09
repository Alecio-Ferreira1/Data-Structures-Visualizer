package com.data_structures_visualizer.controllers;

import com.data_structures_visualizer.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public final class QueueVisualizerController {
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

    @FXML
    public void initialize(){
        handleToScreenChange();

        speed_visualization_slider.valueProperty().addListener((obs, oldValue, newVal) -> {
            speed_visualization_label.setText(String.format("%.1f", 1 + newVal.doubleValue() / 100) + "x");
        });
    }

    private void handleToScreenChange(){
        list_btn.setOnAction(e -> {
            SceneManager.changeScene("/fxml/ListVisualizerScreen.fxml");
        });

        stack_btn.setOnAction(e -> {
            SceneManager.changeScene("/fxml/StackVisualizerScreen.fxml");
        });
    }
}
