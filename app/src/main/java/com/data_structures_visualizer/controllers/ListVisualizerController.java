package com.data_structures_visualizer.controllers;

import java.util.ArrayList;

import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    private Button singly_linked_list_btn;
    @FXML
    private Button doubly_linked_list_btn;
    @FXML
    private Button circular_linked_list_btn;

    private ArrayList<VisualNode> nodes = new ArrayList<VisualNode>();
    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    // private ArrayList<Arrow> prevArrows;
    // private Arc arc;  
    private double initiaWidthForNode = 20;
    private double squareSize = 50.0;
    private Button selectedButton;

    @FXML
    public void initialize(){
        // prevArrows = new ArrayList<Arrow>();

        for(int i = 0; i < 10; ++i){  //SÓ PARA TESTE
            nodes.add(i, new VisualNode(squareSize, squareSize, Integer.toString(i)));
            visualization_area.getChildren().add(nodes.get(i));

            if(i < 9){
                arrows.add(i, new Arrow(squareSize * 0.6));
                visualization_area.getChildren().add(arrows.get(i));
            }
        }

        selectButton(singly_linked_list_btn);
        handleToSelectionListType();

        visualization_area.layoutBoundsProperty().addListener((obs, odlVal, newVal) -> {
            fixVisualizationAreaLayout(newVal.getWidth(), newVal.getHeight());
        });

        speed_visualization_slider.valueProperty().addListener((obs, oldValue, newVal) -> {
            speed_visualization_label.setText(String.format("%.1f", 1 + newVal.doubleValue() / 100) + "x");
        });
    }

    private void fixVisualizationAreaLayout(double width, double height){
        switch(selectedButton.getId()) {
            case "singly_linked_list_btn":
                singlyListVisualization(width, height);
                break;
            
            case "doubly_linked_list_btn":
                doublyListVisualization(width, height);
                break;
        
            default:
                circularListVisualization(width, height);
                break;
        }
    }

    private void handleToSelectionListType(){
        singly_linked_list_btn.setOnAction(e -> {
            selectButton(singly_linked_list_btn);
            fixVisualizationAreaLayout(
                visualization_area.getWidth(), 
                visualization_area.getHeight()
            );
        });

        doubly_linked_list_btn.setOnAction(e -> {
            selectButton(doubly_linked_list_btn);
            fixVisualizationAreaLayout(
                visualization_area.getWidth(), 
                visualization_area.getHeight()
            );
        });

        circular_linked_list_btn.setOnAction(e -> {
            selectButton(circular_linked_list_btn);
            fixVisualizationAreaLayout(
                visualization_area.getWidth(), 
                visualization_area.getHeight()
            );
        });
    }

    private void selectButton(Button btn){
        if(selectedButton != null){
            selectedButton.setTextFill(Color.BLACK);
        }

        btn.setTextFill(Color.WHITE);
        selectedButton = btn;
    }

    private void singlyListVisualization(double width, double height){
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
    }

    private void doublyListVisualization(double width, double height){
        double value = height < width ? height : width;


    }

    private void circularListVisualization(double width, double height){
        
    }
}