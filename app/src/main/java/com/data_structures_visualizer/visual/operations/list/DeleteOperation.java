package com.data_structures_visualizer.visual.operations.list;

import java.util.ArrayList;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.context.list.DeleteContext;
import com.data_structures_visualizer.visual.context.list.DeleteExecutionContext;
import com.data_structures_visualizer.visual.operations.common.TransverseAndHighlightOperation;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.ArrowLabel;
import com.data_structures_visualizer.visual.ui.CurvedArrow;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class DeleteOperation {
    private final DeleteContext context;
    private final DeleteExecutionContext exec = new DeleteExecutionContext();
    private final AnchorPane visualization_area;
    private final ArrayList<VisualNode> nodes;
    private final ArrayList<Arrow> arrows;
    private final ArrayList<Arrow> prevArrows;
    private final CurvedArrow curvedArrow;
    private final ArrowLabel headLabel;
    private final ArrowLabel tailLabel;

    public DeleteOperation(
        DeleteContext context, AnchorPane visualization_area, ArrayList<VisualNode> nodes,
        ArrayList<Arrow> arrows, ArrayList<Arrow> prevArrows, CurvedArrow curvedArrow, 
        ArrowLabel headLabel, ArrowLabel tailLabel
    ){
        this.context = context;
        this.visualization_area = visualization_area;
        this.nodes = nodes;
        this.arrows = arrows;
        this.prevArrows = prevArrows;
        this.curvedArrow = curvedArrow;
        this.headLabel = headLabel;
        this.tailLabel = tailLabel;
    }

    public void build(AnimationTimeLine timeLine){
        addTransversalStep(timeLine);
    }

    private void addTransversalStep(AnimationTimeLine timeLine){
        int index = context.getSinglyLinkedList().indexOf(context.getValue());
         
        if(context.removeByIndex()) index = context.getValue();
        
        new TransverseAndHighlightOperation(nodes, timeLine).build(index == -1 ? nodes.size() : index);

        if(index != -1){
            Rectangle targetRect = nodes.get(index).getRect();
            double speed = ListVisualizerConfig.speedVisualization;

            timeLine.addStep(new Step(
                () -> NodeAnimator.animateStroke(
                    targetRect, (Color) targetRect.getStroke(), Color.ORANGE, (int) (500 * speed), false
                ),  
                () -> NodeAnimator.animateStroke(
                    targetRect, (Color) targetRect.getStroke(), Color.BLACK, (int) (500 * speed), false
                )
            ));
        }
    }
}
