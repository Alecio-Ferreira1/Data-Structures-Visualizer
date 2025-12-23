package com.data_structures_visualizer.visual.operations.list.common;

import java.util.ArrayList;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.visual.animation.AnimationUtils;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.scene.paint.Color;

public final class TransverseAndHighlightOperation {
    private final ArrayList<VisualNode> nodes;
    private final AnimationTimeLine animationTimeLine;

    public TransverseAndHighlightOperation(ArrayList<VisualNode> nodes, AnimationTimeLine animationTimeLine){
        this.nodes = nodes;
        this.animationTimeLine = animationTimeLine;
    }

    public void build(int index){
        int speed = (int) ListVisualizerConfig.speedVisualization;

        for(int i = 0; i < index; ++i){
            final int stepIndex = i;

            animationTimeLine.addStep(new Step(
                () -> NodeAnimator.highlight(nodes.get(stepIndex).getRect(), 400 * speed, Color.GOLD),
                () -> {
                    if(stepIndex - 1 >= 0){
                        return NodeAnimator.highlight(nodes.get(stepIndex - 1).getRect(), 400 * speed, Color.GOLD);
                    }
                    
                    return AnimationUtils.emptyAnimation();
                }
            ));
        }
    }
}
