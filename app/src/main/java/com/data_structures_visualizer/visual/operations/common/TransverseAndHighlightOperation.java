package com.data_structures_visualizer.visual.operations.common;

import java.util.ArrayList;

import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class TransverseAndHighlightOperation {
    private final ArrayList<VisualNode> nodes;
    private final AnimationTimeLine animationTimeLine;

    public TransverseAndHighlightOperation(ArrayList<VisualNode> nodes, AnimationTimeLine animationTimeLine){
        this.nodes = nodes;
        this.animationTimeLine = animationTimeLine;
    }

    public void build(int index){
        for(int i = 0; i < index; ++i){
            final int forwardIndex = i;
            final int reverseIndex = index - i; 

            animationTimeLine.addStep(new Step(
                () -> new SequentialTransition(
                    NodeAnimator.startHighlight(nodes.get(forwardIndex).getRect(), 700, Color.YELLOWGREEN),
                    NodeAnimator.stopHighlight(nodes.get(forwardIndex).getRect())
                ),
                () -> new SequentialTransition(
                    NodeAnimator.startHighlight(nodes.get(reverseIndex).getRect(), 700, Color.YELLOWGREEN),
                    NodeAnimator.stopHighlight(nodes.get(reverseIndex).getRect())
                )
            ));
        }

        Rectangle targetRect = nodes.get(index).getRect();

        animationTimeLine.addStep(new Step(
            () -> NodeAnimator.animateStroke(
                targetRect, (Color) targetRect.getStroke(), Color.ORANGE, 300, false
            ),
            () -> NodeAnimator.animateStroke(
                targetRect, (Color) targetRect.getStroke(), Color.BLACK, 300, false
            )
        ));
    }
}
