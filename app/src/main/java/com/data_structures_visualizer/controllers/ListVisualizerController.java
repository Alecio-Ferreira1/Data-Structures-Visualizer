package com.data_structures_visualizer.controllers;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.entities.CircularLinkedList;
import com.data_structures_visualizer.models.entities.DoublyLikedList;
import com.data_structures_visualizer.models.entities.SinglyLinkedList;
import com.data_structures_visualizer.util.DialogFactory;
import com.data_structures_visualizer.util.SceneManager;
import com.data_structures_visualizer.util.Util;
import com.data_structures_visualizer.visual.context.list.ListInsertContext;
import com.data_structures_visualizer.visual.operations.ListInsertOperation;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.ArrowLabel;
import com.data_structures_visualizer.visual.ui.CurvedArrow;
import com.data_structures_visualizer.visual.ui.VisualNode;
import com.data_structures_visualizer.visual.ui.ArrowLabel.ArrowPosition;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    @FXML
    private Button create_btn;
    @FXML 
    private Button insert_node_btn;
    @FXML 
    private Button delete_node_btn;
    @FXML 
    private Button search_value_btn;
    @FXML     
    private Button clear_btn;

    private final ArrayList<VisualNode> nodes = new ArrayList<VisualNode>();
    private final ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    private final ArrayList<Arrow> prevArrows = new ArrayList<Arrow>();
    private CurvedArrow curvedArrow;  
    private Button selectedButton;
    private ArrowLabel headLabel;
    private ArrowLabel tailLabel;
    private final SinglyLinkedList<Integer> singlyLinkedList = new SinglyLinkedList<Integer>(null);
    private final DoublyLikedList<Integer> doublyLikedList = new DoublyLikedList<Integer>(null);
    private final CircularLinkedList<Integer> circularLinkedList = new CircularLinkedList<Integer>(null);
    private AnimationTimeLine animationTimeLine;

    public enum ListType{
        SINGLY, DOUBLY, CIRCULAR
    }

    private static class SelectionWindowDialog{
        public static void show(String message, Runnable opt1, Runnable opt2, Runnable opt3){
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            Label msg = new Label(message);
            msg.setFont(Font.font(16));
            msg.setTextFill(Color.BLACK);

            Button option1 = new Button("Inserir no início");
            option1.setFont(Font.font(14));
            option1.setTextFill(Color.BLACK);
            option1.getStyleClass().add("standard-dark-gray-button");
         
            Button option2 = new Button("Inserir no fim");
            option2.setFont(Font.font(14));
            option2.setTextFill(Color.BLACK);
            option2.getStyleClass().add("standard-dark-gray-button");

            Button option3 = new Button("Inserir numa posição específica");
            option3.setFont(Font.font(14));
            option3.setTextFill(Color.BLACK);
            option3.getStyleClass().add("standard-dark-gray-button");

            option1.setOnAction(e -> {
                opt1.run();
                stage.close();
            });

            option2.setOnAction(e -> {
                opt2.run();
                stage.close();
            });

            option3.setOnAction(e -> {
                opt3.run();
                stage.close();
            });

            VBox layout = new VBox(20, msg, option1, option2, option3);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(30));

            Scene scene = new Scene(layout);
            scene.getStylesheets().add(ListVisualizerController.class.getResource("/css/style.css").toExternalForm());

            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    @FXML
    public void initialize(){
        createtHeadAndTailLabels();
        putStartExample();
        selectButton(singly_linked_list_btn);
        handleToSelectionListType();
        handleToScreenChange();
        setupOperations();

        visualization_area.layoutBoundsProperty().addListener((obs, odlVal, newVal) -> {
            fixVisualizationAreaLayout(newVal.getWidth(), newVal.getHeight());
        });

        speed_visualization_slider.valueProperty().addListener((obs, oldValue, newVal) -> {
            speed_visualization_label.setText(String.format("%.1f", 1 + newVal.doubleValue() / 100) + "x");
        });
    }

    private void putStartExample(){
        for(int i = 0; i < 10; ++i){ 
            nodes.add(i, new VisualNode(625 * ListVisualizerConfig.squareSize, 
                625 * ListVisualizerConfig.squareSize, Integer.toString(i))
            );

            visualization_area.getChildren().add(nodes.get(i));

            if(i < 9){
                arrows.add(i, new Arrow(ListVisualizerConfig.spacingBetweenNodes * ListVisualizerConfig.squareSize));
                visualization_area.getChildren().add(arrows.get(i));
            }
        } 
    }

    private void createtHeadAndTailLabels(){
        headLabel = new ArrowLabel(
            ListVisualizerConfig.spacingBetweenNodes * ListVisualizerConfig.squareSize * 625, 
            "CABEÇA", 15
        );

        headLabel.setArrowPosition(ArrowPosition.BELOW);

        tailLabel = new ArrowLabel(
            ListVisualizerConfig.spacingBetweenNodes * ListVisualizerConfig.squareSize * 625, 
            "CAUDA", 15
        );

        tailLabel.setArrowPosition(ArrowPosition.BELOW);
    }

    private ListType getListType(){
        switch(selectedButton.getId()) {
            case "singly_linked_list_btn": return ListType.SINGLY;
            case "doubly_linked_list_btn": return ListType.DOUBLY;
            default: return ListType.CIRCULAR;
        }
    }

    private void fixVisualizationAreaLayout(double width, double height){
        switch(getListType()) {
            case ListType.SINGLY:
                singlyListVisualization(width, height);
                break;
            
            case ListType.DOUBLY:
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

    private void removeCurvedArrow(){
        if(curvedArrow != null){
            visualization_area.getChildren().remove(curvedArrow);
            curvedArrow = null;
        }
    }

    private void singlyListVisualization(double width, double height){
        removeCurvedArrow();

        for(Arrow arrow : prevArrows){
            visualization_area.getChildren().remove(arrow);
        }

        prevArrows.clear();

        final double value = height < width ? height : width;
        final double nodeWidth = value * ListVisualizerConfig.squareSize;
        final double arrowLenght = ListVisualizerConfig.spacingBetweenNodes * nodeWidth;

        anchorArrowLabels(
            ListVisualizerConfig.squareSize * width * 0.6, 
            ListVisualizerConfig.squareSize * height / 3, width, height
        );
        
        for(int i = 0; i < nodes.size(); ++i){
           nodes.get(i).update(nodeWidth, nodeWidth, value * 0.005);
                
            anchorNode(nodes.get(i), width, height, i);

            if(i < arrows.size()){
                resizeArrow(arrows.get(i), arrowLenght, width, height);
                    
                AnchorPane.setTopAnchor(
                    arrows.get(i), 
                    (height / 2) - (arrows.get(i).getBoundsInParent().getHeight() / 2)
                );

                AnchorPane.setLeftAnchor(
                    arrows.get(i), 
                    ((ListVisualizerConfig.xOffsetForNodes * width) + nodeWidth + 
                    ((1 + ListVisualizerConfig.spacingBetweenNodes) * nodeWidth) * i)
                );
            }
        }
    }

    private void doublyListVisualization(double width, double height){
        removeCurvedArrow();

        double value = height < width ? height : width;
        double nodeWidth = ListVisualizerConfig.squareSize * value;
        double arrowLenght = ListVisualizerConfig.spacingBetweenNodes * nodeWidth;

        anchorArrowLabels(
            ListVisualizerConfig.squareSize * width * 0.6, 
            ListVisualizerConfig.squareSize * height / 3, width, height
        );

        for(int i = 0; i < nodes.size(); ++i){
            nodes.get(i).update(
                value * ListVisualizerConfig.squareSize, 
                value * ListVisualizerConfig.squareSize, value * 0.005
            );
                
            anchorNode(nodes.get(i), width, height, i);

            if(i < arrows.size()){
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
                    ((ListVisualizerConfig.xOffsetForNodes * width) + nodeWidth + 
                    ((1 + ListVisualizerConfig.spacingBetweenNodes) * nodeWidth) * i)
                );

                AnchorPane.setTopAnchor(
                    prevArrows.get(i), 
                    (height / 2) + (prevArrows.get(i).getBoundsInParent().getHeight() / 2)
                );

                AnchorPane.setLeftAnchor(
                    prevArrows.get(i), 
                    ((ListVisualizerConfig.xOffsetForNodes * width) + nodeWidth + 
                    ((1 + ListVisualizerConfig.spacingBetweenNodes) * nodeWidth) * i)
                );
            }
        }
    }

    private void circularListVisualization(double width, double height){
        singlyListVisualization(width, height);

        if(nodes.size() > 0){
            Platform.runLater(() -> {
                visualization_area.applyCss();
                visualization_area.layout();

                int lastNodeIndex = nodes.size() - 1;
                VisualNode last = nodes.get(lastNodeIndex);
                VisualNode first = nodes.get(0);
                double startX = last.getLayoutX() + (last.getRect().getWidth() / 2);
                double startY = last.getLayoutY() + last.getRect().getHeight();
                double endX = first.getLayoutX() + (first.getRect().getWidth() / 2);
                double endY = startY;
                double value = Math.min(width, height);

                if(curvedArrow == null){
                    curvedArrow = new CurvedArrow(startX, startY, endX, endY, 0.02 * value);
                    visualization_area.getChildren().add(curvedArrow);
                }

                if(nodes.size() == 1){
                    startX = last.getLayoutX() + last.getRect().getWidth();
                    startY = last.getLayoutY() + (last.getRect().getHeight() / 2);
                    curvedArrow.update(startX, startY, endX, endY, 0.02 * value, true);
                    return;
                }
                
                curvedArrow.update(startX, startY, endX, endY, 0.02 * value, false);
            });
        }
    }
    
    private void anchorNode(VisualNode node, double width, double height, int pos){
        AnchorPane.setTopAnchor(node, (height / 2) - (node.getRect().getHeight() / 2)); 
        AnchorPane.setLeftAnchor(node, 
            (ListVisualizerConfig.xOffsetForNodes * width) + 
            (((1 + ListVisualizerConfig.spacingBetweenNodes) * node.getRect().getWidth() * pos))
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

    private void setupOperations(){
        create_btn.setOnAction(e -> {
            DialogFactory.showInputDialog(
                "Insira o tamanho da lista: ", null, (Integer lenght, Integer v) -> {
                createList(lenght);
                fixVisualizationAreaLayout(visualization_area.getWidth(), visualization_area.getHeight());
            });
        });

        insert_node_btn.setOnAction(e -> {
            SelectionWindowDialog.show(
                "Selecione o tipo de inserção: ", 
                () -> {
                    DialogFactory.showInputDialog(
                        "Insira o valor para inserir na lista:", null,
                        (Integer value, Integer v) -> { insertNode(value, 0); }
                    );
                }, 
                () -> {
                    DialogFactory.showInputDialog(
                        "Insira o valor para inserir na lista:", null,
                        (Integer value, Integer v) -> { insertNode(value, nodes.size()); }
                    );
                },
                () -> {
                    DialogFactory.showInputDialog(
                        "Insira o valor para inserir na lista:", 
                        "Insira a posição: para inserir na lista",
                        (Integer value, Integer pos) -> { insertNode(value, pos); }
                    );
                }
            );

            fixVisualizationAreaLayout(visualization_area.getWidth(), visualization_area.getHeight());
        });
     
        delete_node_btn.setOnAction(e -> {
            fixVisualizationAreaLayout(visualization_area.getWidth(), visualization_area.getHeight());
        });
     
        search_value_btn.setOnAction(e -> {
            
        });
         
        clear_btn.setOnAction(e -> {
            DialogFactory.ConfirmDialog.show(
                "Tem certeza que deseja limpar a área de visualização?", () -> {
                clearVisualization();
            });
        });
    }

    private void createList(int lenght){
        if(lenght > ListVisualizerConfig.listMaxLimit){
            Util.showAlert(
                "Não foi possível criar a lista.",
                String.format("Tamanho máximo permitido: %d", ListVisualizerConfig.listMaxLimit),
                AlertType.CONFIRMATION
            );

            return;
        }

       clearVisualization();

        for(int i = 0; i < lenght; ++i){
            Integer randInt = ThreadLocalRandom.current().nextInt(0, 9999);

            singlyLinkedList.pushBack(randInt);
            doublyLikedList.pushBack(randInt);
            circularLinkedList.pushBack(randInt);
            
            nodes.add(new VisualNode(
                625 * ListVisualizerConfig.squareSize, 625 * ListVisualizerConfig.squareSize, 
                String.valueOf(randInt)
            ));

            visualization_area.getChildren().add(nodes.get(i));

            if(i < (lenght - 1)){
                arrows.add(new Arrow(ListVisualizerConfig.spacingBetweenNodes * ListVisualizerConfig.squareSize));
                visualization_area.getChildren().add(arrows.get(i));
            }
        }
    }

    private void clearVisualization(){
        visualization_area.getChildren().clear();
        nodes.clear();
        arrows.clear();
        curvedArrow = null;
        prevArrows.clear();
    }

    private void anchorArrowLabels(double arrowLenght, double fontSize, double width, double height){
        if(nodes.isEmpty()){
            visualization_area.getChildren().remove(headLabel);
            visualization_area.getChildren().remove(tailLabel);
            return;
        }

        if(!visualization_area.getChildren().contains(headLabel)){
            visualization_area.getChildren().add(headLabel);
        }

        if(!visualization_area.getChildren().contains(tailLabel)){
            visualization_area.getChildren().add(tailLabel);
        }

        final double value = height < width ? height : width;
        final double nodeWidth = ListVisualizerConfig.squareSize * value; 
        final double xOffset = 0.01 * value;
        final double labelsYoffset = 1.7;

        headLabel.setText("CABEÇA");
        headLabel.update(arrowLenght, headLabel.getText(), fontSize);

        if(nodes.size() == 1){
            visualization_area.getChildren().remove(tailLabel);
            headLabel.setText("CABEÇA\nCAUDA");
        }

        if(visualization_area.getChildren().contains(tailLabel)){
            tailLabel.update(arrowLenght, "CAUDA", fontSize);

            AnchorPane.setTopAnchor(
                tailLabel, ((height / 2) - (nodeWidth / 2)) - ((1 + labelsYoffset) * arrowLenght)
            );

            AnchorPane.setLeftAnchor(
                tailLabel, ListVisualizerConfig.xOffsetForNodes + (nodeWidth / 2) + 
                ((1 + ListVisualizerConfig.spacingBetweenNodes) * nodeWidth * (nodes.size() - 1)) - xOffset
            );
        }
            
        AnchorPane.setTopAnchor(
            headLabel, ((height / 2) - (nodeWidth / 2)) - ((1 + labelsYoffset) * arrowLenght)
        );

        AnchorPane.setLeftAnchor(
            headLabel, ListVisualizerConfig.xOffsetForNodes + (nodeWidth / 2) - xOffset
        );
    }   

    private boolean validadeInsertion(int pos){
        if((nodes.size() + 1) > ListVisualizerConfig.listMaxLimit){
            Util.showAlert(
                "Lista cheia!",
                String.format("A lista atingiu o valor máximo: %d", ListVisualizerConfig.listMaxLimit),
                AlertType.CONFIRMATION
            );

            return false;
        }

        if(pos >= nodes.size()){
            Util.showAlert(
                "Indíce inválido!",
                String.format("O valor do índice deve estar entre 0 e %d.", nodes.size()),
                AlertType.CONFIRMATION
            );

            return false;
        }

        return true;
    }

    private ListInsertContext buildInsertContext(int value, int pos){
        return new ListInsertContext(
            pos, value, getListType(), visualization_area.getWidth(), 
            visualization_area.getHeight(), nodes.size()
        );
    }

    private void insertNode(int value, int pos){
        if(!validadeInsertion(pos)) return;

        ListInsertContext insertContext = buildInsertContext(value, pos);

        animationTimeLine = new AnimationTimeLine();

        ListInsertOperation op = new ListInsertOperation(
            insertContext, visualization_area, nodes, arrows, prevArrows, curvedArrow
        );

        op.build(animationTimeLine);
    }
}