package com.data_structures_visualizer.controllers;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.data_structures_visualizer.models.entities.Stack;
import com.data_structures_visualizer.util.DialogFactory;
import com.data_structures_visualizer.util.SceneManager;
import com.data_structures_visualizer.util.Util;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.ArrowLabel;
import com.data_structures_visualizer.visual.ui.StackBase;
import com.data_structures_visualizer.visual.ui.VisualNode;
import com.data_structures_visualizer.visual.ui.ArrowLabel.ArrowPosition;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    @FXML
    private Button create_btn;
    @FXML 
    private Button push_btn;
    @FXML 
    private Button pop_btn;
    @FXML     
    private Button clear_btn;

    private StackBase stackBase;
    private final ArrayList<VisualNode> nodes = new ArrayList<VisualNode>();
    private final double squareSize = 0.08;
    private final double spacingBetweenNodes = 0.1;
    private final int stackMaxLimit = 9;
    private ArrowLabel topLabel;

    private final Stack<Integer> stack = new Stack<Integer>(null);

    @FXML
    public void initialize(){
        topLabel = new ArrowLabel(squareSize * 625 * 0.6, "TOPO", 15);
        topLabel.setArrowPosition(ArrowPosition.RIGHT);
        visualization_area.getChildren().add(topLabel);

        for(int i = 0; i < 9; ++i){  
            nodes.add(i, new VisualNode(625 * squareSize, 625 * squareSize, Integer.toString(i)));
            visualization_area.getChildren().add(nodes.get(i));
        } 

        handleToScreenChange();
        setupOperations();

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
        double updatedHeight = 0.8 * height;
        double updatedStroke = 0.004 * width;

        if(stackBase == null){
            stackBase = new StackBase(updatedWidth, updatedHeight, updatedStroke);
            visualization_area.getChildren().add(stackBase);
        } 

        stackBase.update(updatedWidth, updatedHeight, updatedStroke);
        AnchorPane.setTopAnchor(stackBase, (height / 2) - (stackBase.getHeight() / 2));
        AnchorPane.setLeftAnchor(stackBase, (width / 2) - (stackBase.getWidth() / 2));

        double yOffset = 0.0005 * height;
        double initialHeight = updatedHeight + yOffset;
        
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

        anchorTopLabel(width, height);
    }

    private void setupOperations(){
        create_btn.setOnAction(e -> {
            DialogFactory.showInputDialog("Insira o tamanho da pilha: ", (int lenght) -> {
                createStack(lenght);
                fixVisualizationAreaLayout(visualization_area.getWidth(), visualization_area.getHeight());
            });
        });

        push_btn.setOnAction(e -> {

        });

        pop_btn.setOnAction(e -> {

        });

        clear_btn.setOnAction(e -> {
            DialogFactory.ConfirmDialog.show("Tem certeza que deseja limpar a área de visualização?", () -> {
                clearVisualization();
            });
        });
    }

    private void createStack(int lenght){
        if(lenght > stackMaxLimit){
            Util.showAlertForExceedingValue(stackMaxLimit);
            return;
        }

        clearVisualization();

        for(int i = 0; i < lenght; ++i){
            Integer randInt = ThreadLocalRandom.current().nextInt(0, 9999);

            stack.push(randInt);
            nodes.add(new VisualNode(625 * squareSize, 625 * squareSize, String.valueOf(randInt)));
            visualization_area.getChildren().add(nodes.get(i));
        }
    }

   
    private void clearVisualization(){
        for(VisualNode node : nodes){
            visualization_area.getChildren().remove(node);
        }

        visualization_area.getChildren().remove(topLabel);
        nodes.clear();
    }

    private void anchorTopLabel(double width, double height){
        if(nodes.size() == 0){
            visualization_area.getChildren().remove(topLabel);
            return;
        }

        if(!visualization_area.getChildren().contains(topLabel)){
            visualization_area.getChildren().add(topLabel);
        }

        double yOffset = 0.0005 * height;
        double initialHeight =  0.8 * height + yOffset;
        double arrowLenght = squareSize * width * 1.02;
        double xOffset = 2.3 * arrowLenght;

        topLabel.update(arrowLenght, "TOPO", squareSize * height / 3);
        
        AnchorPane.setTopAnchor(
            topLabel,
            initialHeight - ((1 + spacingBetweenNodes) * height * squareSize * (nodes.size() - 1)) +
            (height * squareSize) / 2 - (0.01 * height)
        );
        
        AnchorPane.setLeftAnchor(
            topLabel,
            (width / 2) - (topLabel.getArrow().getBaseLenght() / 2) - xOffset
        );
    }
}
