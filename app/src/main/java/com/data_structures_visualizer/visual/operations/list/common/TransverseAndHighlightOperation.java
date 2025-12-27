package com.data_structures_visualizer.visual.operations.list.common;

import java.util.ArrayList;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.models.text.ExplanationRepository;
import com.data_structures_visualizer.models.text.ExplanationText;
import com.data_structures_visualizer.visual.animation.AnimationUtils;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.scene.paint.Color;

public final class TransverseAndHighlightOperation {
    private final ArrayList<VisualNode> nodes;
    private final AnimationTimeLine animationTimeLine;
    private final ExplanationRepository explanationRepository;

    public TransverseAndHighlightOperation(
        ArrayList<VisualNode> nodes, AnimationTimeLine animationTimeLine, 
        ExplanationRepository explanationRepository
    ){
        this.nodes = nodes;
        this.animationTimeLine = animationTimeLine;
        this.explanationRepository = explanationRepository;
    }

    public void build(int index, int targetValue){
        int speed = (int) ListVisualizerConfig.speedVisualization;

        for(int i = 0; i < index; ++i){
            final int stepIndex = i;

            explanationRepository.addExplanation(animationTimeLine.size(),
                new ExplanationText(    
                    animationTimeLine.size(), 
                    "Valor do nó atual: {node:" + String.valueOf(nodes.get(stepIndex).getText()) +"}\n"
                    + "Índice: {node:" + String.valueOf(i) + "}\n"
                )
            );

            animationTimeLine.addStep(new Step(
                () -> NodeAnimator.highlight(nodes.get(stepIndex).getRect(), 700 * speed, Color.YELLOW),
                () -> {
                    if(stepIndex - 1 >= 0){
                        return NodeAnimator.highlight(nodes.get(stepIndex - 1).getRect(), 700 * speed, Color.YELLOW);
                    }
                    
                    return AnimationUtils.emptyAnimation();
                }
            ));
        }
    }
}
