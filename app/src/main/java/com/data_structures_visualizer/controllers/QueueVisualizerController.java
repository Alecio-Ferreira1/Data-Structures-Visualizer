package com.data_structures_visualizer.controllers;

import java.util.ArrayList;

import com.data_structures_visualizer.util.SceneManager;
import com.data_structures_visualizer.visual.ui.QueueDelimiter;
import com.data_structures_visualizer.visual.ui.StackBase;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.application.Platform;
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

    private QueueDelimiter queueDelimiter;
    private final ArrayList<VisualNode> nodes = new ArrayList<VisualNode>();
    private final double squareSize = 0.08;
    private final double spacingBetweenNodes = 0.15;

    @FXML
    public void initialize(){
        for(int i = 0; i < 13; ++i){  //SÓ PARA TESTE
            nodes.add(i, new VisualNode(625 * squareSize, 625 * squareSize, Integer.toString(i)));
            visualization_area.getChildren().add(nodes.get(i));
        } //SÓ PARA TESTE

        handleToScreenChange();

        visualization_area.layoutBoundsProperty().addListener((obs, odlVal, newVal) -> {
            if(newVal.getWidth() > 0 && newVal.getHeight() > 0){
                fixVisualizationAreaLayout(newVal.getWidth(), newVal.getHeight());
            }
        });

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

    private void fixVisualizationAreaLayout(double width, double height){
        double updatedWidth = 0.85 * width;
        double updatedHeight = 0.15 * height;
        double updatedStroke = 0.004 * width;

        if(queueDelimiter == null){
            queueDelimiter = new QueueDelimiter(updatedWidth, updatedHeight, updatedStroke);
            visualization_area.getChildren().add(queueDelimiter);
        } 

        Platform.runLater(() -> {
            queueDelimiter.update(updatedWidth, updatedHeight, updatedStroke);
            AnchorPane.setTopAnchor(queueDelimiter, (height / 2) - (queueDelimiter.getSpacingBetweenLines() / 2));
            AnchorPane.setLeftAnchor(queueDelimiter, (width / 2) - (queueDelimiter.getWidth() / 2));

            double xOffset = 0.01 * width;
            double value = height < width ? height : width;

            for(int i = 0; i < nodes.size(); ++i){
                nodes.get(i).update(value * squareSize, value * squareSize, value * 0.005);
                
                AnchorPane.setTopAnchor(
                    nodes.get(i), 
                    (height / 2) - (nodes.get(i).getRect().getHeight()) / 2
                );

                AnchorPane.setLeftAnchor(
                    nodes.get(i), 
                    xOffset + (width / 2) - (queueDelimiter.getWidth() / 2) +
                    ((1 + spacingBetweenNodes) * nodes.get(i).getRect().getHeight() * i)
                );
            }
        });
    }
}
