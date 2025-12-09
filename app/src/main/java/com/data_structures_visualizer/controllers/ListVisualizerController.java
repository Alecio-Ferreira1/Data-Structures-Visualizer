package com.data_structures_visualizer.controllers;

import java.util.ArrayList;

import com.data_structures_visualizer.util.SceneManager;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.CurvedArrow;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public final class ListVisualizerController {
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
    @FXML 
    private Button list_btn;
    @FXML
    private Button stack_btn; 
    @FXML
    private Button queue_btn;

    private final ArrayList<VisualNode> nodes = new ArrayList<VisualNode>();
    private final ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    private ArrayList<Arrow> prevArrows;
    private CurvedArrow curvedArrow;  
    private final double initiaWidthForNode = 20;
    private final double squareSize = 0.08;
    private final double spacingBetweenNodes = 0.6;
    private Button selectedButton;

    @FXML
    public void initialize(){
        for(int i = 0; i < 10; ++i){  //SÓ PARA TESTE
            nodes.add(i, new VisualNode(625 * squareSize, 625 * squareSize, Integer.toString(i)));
            visualization_area.getChildren().add(nodes.get(i));

            if(i < 9){
                arrows.add(i, new Arrow(spacingBetweenNodes * squareSize));
                visualization_area.getChildren().add(arrows.get(i));
            }
        } //SÓ PARA TESTE

        selectButton(singly_linked_list_btn);
        handleToSelectionListType();
        handleToScreenChange();

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
        if(prevArrows != null){
            for(Arrow arrow : prevArrows){
                visualization_area.getChildren().remove(arrow);
                prevArrows = null;
            }
        }

        if(curvedArrow != null){
            visualization_area.getChildren().remove(curvedArrow);
            curvedArrow = null;
        }
        
        double value = height < width ? height : width;
        
        for(int i = 0; i < nodes.size(); ++i){
            if(value != 0) nodes.get(i).update(value * squareSize, value * squareSize, value * 0.005);
                
            anchorNode(nodes.get(i), width, height, i);

            if(i < arrows.size()){
                double arrowLenght = spacingBetweenNodes * nodes.get(i).getRect().getWidth();

                resizeArrow(arrows.get(i), arrowLenght, width, height);
                    
                AnchorPane.setTopAnchor(
                    arrows.get(i), 
                    (height / 2) - (arrows.get(i).getBoundsInParent().getHeight() / 2)
                );

                AnchorPane.setLeftAnchor(
                    arrows.get(i), 
                    (initiaWidthForNode + nodes.get(i).getRect().getWidth() + 
                    ((1 + spacingBetweenNodes) * nodes.get(i).getRect().getWidth()) * i)
                );
            }
        }
    }

    private void doublyListVisualization(double width, double height){
        if(curvedArrow != null){
            visualization_area.getChildren().remove(curvedArrow);
            curvedArrow = null;
        }

        if(prevArrows == null){
            prevArrows = new ArrayList<Arrow>();
        }

        double value = height < width ? height : width;
        
        for(int i = 0; i < nodes.size(); ++i){
            if(value != 0) nodes.get(i).update(value * squareSize, value * squareSize, value * 0.005);
                
            anchorNode(nodes.get(i), width, height, i);

            if(i < arrows.size()){
                double arrowLenght = spacingBetweenNodes * nodes.get(i).getRect().getWidth();

                resizeArrow(arrows.get(i), arrowLenght, width, height);

                if(i >= prevArrows.size()){
                    prevArrows.add(i, new Arrow(arrows.get(i)));
                    prevArrows.get(i).setRotate(180);
                    visualization_area.getChildren().add(prevArrows.get(i));
                }

                resizeArrow(prevArrows.get(i), arrowLenght, width, height);
                    
                AnchorPane.setTopAnchor(
                    arrows.get(i), 
                    (height / 2) - (arrows.get(i).getBoundsInParent().getHeight() * 1.25)
                );

                AnchorPane.setLeftAnchor(
                    arrows.get(i), 
                    (initiaWidthForNode + nodes.get(i).getRect().getWidth() + 
                    ((1 + spacingBetweenNodes) * nodes.get(i).getRect().getWidth()) * i)
                );

                AnchorPane.setTopAnchor(
                    prevArrows.get(i), 
                    (height / 2) + (prevArrows.get(i).getBoundsInParent().getHeight() / 2)
                );

                AnchorPane.setLeftAnchor(
                    prevArrows.get(i), 
                    (initiaWidthForNode + nodes.get(i).getRect().getWidth() + 
                    ((1 + spacingBetweenNodes) * nodes.get(i).getRect().getWidth()) * i)
                );
            }
        }
    }

    private void circularListVisualization(double width, double height){
        singlyListVisualization(width, height);

        if(nodes.size() > 0){
            Platform.runLater(() -> {
                int lastNodeIndex = nodes.size() - 1;
                VisualNode last = nodes.get(lastNodeIndex);
                VisualNode first = nodes.get(0);
                double startX = last.getLayoutX() + (last.getRect().getWidth() / 2);
                double startY = last.getLayoutY() + last.getRect().getHeight();
                double endX = first.getLayoutX() + (first.getRect().getWidth() / 2);
                double endY = startY;

                if(curvedArrow == null){
                    curvedArrow = new CurvedArrow(startX, startY, endX, endY);
                    visualization_area.getChildren().add(curvedArrow);
                }
                
                curvedArrow.update(startX, startY, endX, endY);
            });
        }
    }
    
    private void anchorNode(VisualNode node, double width, double height, int pos){
        AnchorPane.setTopAnchor(node, (height / 2) - (node.getRect().getHeight() / 2)); 
        AnchorPane.setLeftAnchor(
            node, 
            initiaWidthForNode + (((1 + spacingBetweenNodes) * node.getRect().getWidth() * pos))
        );
    }

    private void resizeArrow(Arrow arrow, double lenght, double width, double height){
        arrow.setStrokeWidth(height * 0.003);
        arrow.setLenght(lenght);
    }

    private void handleToScreenChange(){
        stack_btn.setOnAction(e -> {
            SceneManager.changeScene("/fxml/StackVisualizerScreen.fxml");
        });

        queue_btn.setOnAction(e -> {
            SceneManager.changeScene("/fxml/QueueVisualizerScreen.fxml");
        });
    }
}