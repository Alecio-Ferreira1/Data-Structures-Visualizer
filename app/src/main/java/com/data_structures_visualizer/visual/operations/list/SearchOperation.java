package com.data_structures_visualizer.visual.operations.list;

import java.util.ArrayList;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.models.entities.SinglyLinkedList;
import com.data_structures_visualizer.models.text.ExplanationRepository;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.operations.list.common.TransverseAndHighlightOperation;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class SearchOperation {
    private final SinglyLinkedList<Integer> list;
    private final ArrayList<VisualNode> nodes;
    private final int value;
    private int index;
    private final ExplanationRepository explanationRepository;

    public SearchOperation(
        SinglyLinkedList<Integer> list, ArrayList<VisualNode> nodes, int value,
        ExplanationRepository explanationRepository
    ){
        this.list = list;
        this.nodes = nodes;
        this.value = value;
        this.explanationRepository = explanationRepository;
    }

    public void build(AnimationTimeLine timeLine){
        addTransversalStep(timeLine);   
        addHighlightResultsStep(timeLine);
    }

    private void addTransversalStep(AnimationTimeLine timeLine){
        index = list.indexOf(value);
        index = index != -1 ? index : nodes.size();

        new TransverseAndHighlightOperation(
            nodes, timeLine, explanationRepository
        ).build(index, value);
    }

    private void addHighlightResultsStep(AnimationTimeLine timeLine){
        if(index != -1){
            VisualNode node = nodes.get(index);
            Rectangle rect = node.getRect();
            int speed = (int) ListVisualizerConfig.speedVisualization * 300;

            timeLine.addStep(new Step(
                () -> NodeAnimator.pulseHighlight(rect, Color.ORANGE, speed + 200, 7000),
                () -> NodeAnimator.animateStroke(rect, (Color) rect.getStroke(), Color.BLACK, speed, false)
            )); 
        }
    }
}
