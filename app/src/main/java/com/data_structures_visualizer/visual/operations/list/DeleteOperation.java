package com.data_structures_visualizer.visual.operations.list;

import java.util.ArrayList;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.controllers.ListVisualizerController.ListType;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.visual.animation.AnimationUtils;
import com.data_structures_visualizer.visual.animation.ArrowAnimator;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.animation.ArrowAnimator.DrawArrowDirection;
import com.data_structures_visualizer.visual.context.list.DeleteContext;
import com.data_structures_visualizer.visual.context.list.DeleteExecutionContext;
import com.data_structures_visualizer.visual.operations.common.FixArrowLabelsPosOperation;
import com.data_structures_visualizer.visual.operations.common.FixCurvedArrowPosOperation;
import com.data_structures_visualizer.visual.operations.common.RepositionNodesOperation;
import com.data_structures_visualizer.visual.operations.common.TransverseAndHighlightOperation;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.ArrowLabel;
import com.data_structures_visualizer.visual.ui.CurvedArrow;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
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
        addDesconnectNodeStep(timeLine);
        addRemoveNodeStep(timeLine);
        addEstablishConnectionsStep(timeLine);
        addFixLayoutStep(timeLine);
    }

    private void addTransversalStep(AnimationTimeLine timeLine){
        int index = context.getSinglyLinkedList().indexOf(context.getValue());
         
        if(context.removeByIndex()) index = context.getValue();
        
        new TransverseAndHighlightOperation(nodes, timeLine).build(index == -1 ? nodes.size() : index);

        context.setIndexToRemove(index);

        if(index != -1){
            timeLine.addStep(new Step(
                () -> applyHighLights(Color.ORANGE, Color.GOLD, Color.BLUE),  
                () -> applyHighLights(Color.BLACK, Color.BLACK, Color.BLACK)
            ));
        }
    }

    private Animation applyHighLights(Color prevRectColor, Color targetRectColor, Color nextRectColor){
        final int index = context.getIndexToRemove();
        final Rectangle targetRect = nodes.get(index).getRect();
        Rectangle prevRect = nodes.get(index - 1 >= 0 ? index - 1 : 0).getRect();
        Rectangle nextRect = nodes.get(index + 1 < nodes.size() ? index + 1 : 0).getRect();
        double speed = ListVisualizerConfig.speedVisualization;
        
        return new SequentialTransition(
            NodeAnimator.animateStroke(
                targetRect, (Color) targetRect.getStroke(), targetRectColor, (int) (700 * speed), false
            ),
            index - 1 >= 0 ? NodeAnimator.animateStroke(
                prevRect, (Color) prevRect.getStroke(), prevRectColor, (int) (700 * speed), false
            ) : AnimationUtils.emptyAnimation(),
            index + 1 < nodes.size() ? NodeAnimator.animateStroke(
                nextRect, (Color) nextRect.getStroke(), nextRectColor, (int) (700 * speed), false
            ) : AnimationUtils.emptyAnimation()
        );
    }

    private void addDesconnectNodeStep(AnimationTimeLine timeLine){
        if(context.getIndexToRemove() == -1) return;

        desconnectPrevArrows(timeLine);
        desconnectNextArrows(timeLine);
    }

    private void desconnectPrevArrows(AnimationTimeLine timeLine){
        int index = context.getIndexToRemove();
        double speed = ListVisualizerConfig.speedVisualization;

        if(index - 1 < 0) return;

        timeLine.addStep(new Step(
            () -> new SequentialTransition(
                ArrowAnimator.animateOut(arrows.get(index - 1), speed * 1, DrawArrowDirection.BACKWARD), 
                context.getListType() == ListType.DOUBLY ?
                ArrowAnimator.animateOut(prevArrows.get(index - 1), speed * 1, DrawArrowDirection.FORWARD) :
                AnimationUtils.emptyAnimation()
            ),
            () -> new SequentialTransition(
                context.getListType() == ListType.DOUBLY ?
                ArrowAnimator.animateIn(prevArrows.get(index - 1), speed * 1, DrawArrowDirection.BACKWARD) :
                AnimationUtils.emptyAnimation(),
                ArrowAnimator.animateIn(arrows.get(index - 1), speed * 1, DrawArrowDirection.FORWARD)
            )
        ));
    }

    private void desconnectNextArrows(AnimationTimeLine timeLine){
        int index = context.getIndexToRemove();
        double speed = ListVisualizerConfig.speedVisualization;

        if(index >= arrows.size()) return;

        timeLine.addStep(new Step(
            () -> {
                Animation removeNextArrows = AnimationUtils.emptyAnimation();

                removeNextArrows = new SequentialTransition(
                    ArrowAnimator.animateOut(arrows.get(index), speed * 1, DrawArrowDirection.BACKWARD), 
                    context.getListType() == ListType.DOUBLY ?
                    ArrowAnimator.animateOut(prevArrows.get(index), speed * 1, DrawArrowDirection.BACKWARD) :
                    AnimationUtils.emptyAnimation()
                );

                exec.getRemovedArrows().push(arrows.get(index));

                if(context.getListType() == ListType.DOUBLY){
                    exec.getRemovedPrevArrows().push(prevArrows.get(index));
                }

                removeNextArrows.setOnFinished(e -> {
                    visualization_area.getChildren().remove(arrows.get(index));
                    arrows.remove(index);
        
                    if(context.getListType() == ListType.DOUBLY){
                        visualization_area.getChildren().remove(prevArrows.get(index));
                        prevArrows.remove(index);
                    }
                });

                return removeNextArrows;
            },
            () -> {
                Arrow arrow = exec.getRemovedArrows().pop();
                Arrow prevArrow = exec.getRemovedPrevArrows().pop();

                Animation animation = new SequentialTransition(
                    ArrowAnimator.animateIn(arrows.get(index), speed * 1, DrawArrowDirection.FORWARD), 
                    context.getListType() == ListType.DOUBLY ?
                    ArrowAnimator.animateIn(prevArrows.get(index), speed * 1, DrawArrowDirection.BACKWARD) :
                    AnimationUtils.emptyAnimation()
                );

                animation.setOnFinished(e -> {
                    if(prevArrow != null){
                        prevArrows.add(index, prevArrow);
                        visualization_area.getChildren().add(prevArrow);
                    }

                    if(arrow != null){
                        arrows.add(index, arrow);
                        visualization_area.getChildren().add(arrow);
                    }
                });
                
                return animation;
            }
        ));
    }

    private void addRemoveNodeStep(AnimationTimeLine timeLine){
        double speed = ListVisualizerConfig.speedVisualization;

        timeLine.addStep(new Step(
            () -> {
                VisualNode node = nodes.get(context.getIndexToRemove());
                Animation animation = NodeAnimator.emergeEffect(node, 3 * speed, false);

                exec.getRemovedNodes().push(node);

                animation.setOnFinished(e -> {
                    nodes.remove(node);
                    visualization_area.getChildren().remove(node);
                });

                return animation;
            }, 
            () ->{
                VisualNode node = exec.getRemovedNodes().pop();
                Animation animation = NodeAnimator.emergeEffect(node, 3 * speed, true);

                animation.setOnFinished(e -> {
                    nodes.add(context.getIndexToRemove(), node); 
                });

                return animation;
            }
        ));
    }

    private void addEstablishConnectionsStep(AnimationTimeLine timeLine){
        int index = context.getIndexToRemove();

        if(index == 0 || index == nodes.size()) return;

        double speed = ListVisualizerConfig.speedVisualization;
        
        timeLine.addStep(new Step(
            () -> {
                RepositionNodesOperation repositionNodes = new RepositionNodesOperation(
                    nodes, arrows, prevArrows, headLabel, tailLabel, visualization_area, 
                    context.getIndexToRemove(), nodes.size(), context.getListType()
                );

                if(index < arrows.size()){
                    return new SequentialTransition(
                        repositionNodes.build(index, -context.getxOffset()),
                        ArrowAnimator.animateIn(arrows.get(index), 1 * speed, DrawArrowDirection.FORWARD),
                        context.getListType() == ListType.DOUBLY ? 
                        ArrowAnimator.animateIn(prevArrows.get(index), 1 * speed, DrawArrowDirection.FORWARD) 
                        : AnimationUtils.emptyAnimation(),
                        applyHighLights(Color.BLACK, Color.BLACK, Color.BLACK)
                    );
                }

                return new SequentialTransition(
                    repositionNodes.build(index, -context.getxOffset()),
                    applyHighLights(Color.BLACK, Color.BLACK, Color.BLACK)
                );
            },
            () -> {
                RepositionNodesOperation repositionNodes = new RepositionNodesOperation(
                    nodes, arrows, prevArrows, headLabel, tailLabel, visualization_area, 
                    context.getIndexToRemove(), nodes.size(), context.getListType()
                );
                
                if(index < arrows.size()){
                    return new SequentialTransition(
                        applyHighLights(Color.ORANGE, Color.GOLD, Color.BLUE),
                        context.getListType() == ListType.DOUBLY ? 
                        ArrowAnimator.animateOut(prevArrows.get(index), 1 * speed, DrawArrowDirection.BACKWARD) 
                        : AnimationUtils.emptyAnimation(),
                        ArrowAnimator.animateOut(arrows.get(index), 1 * speed, DrawArrowDirection.BACKWARD),
                        repositionNodes.build(index, context.getxOffset())
                    );
                }

                return new SequentialTransition(
                    applyHighLights(Color.ORANGE, Color.GOLD, Color.BLUE),
                    repositionNodes.build(index, context.getxOffset())
                );
            }
        ));
    }

    private void addFixLayoutStep(AnimationTimeLine timeLine){
        FixArrowLabelsPosOperation fixLayout = new FixArrowLabelsPosOperation(
            headLabel, tailLabel, context.getxOffset(), context.getIndexToRemove(), nodes.size()
        );

        fixLayout.build(timeLine, false);

        FixCurvedArrowPosOperation fixCurvedArrow = new FixCurvedArrowPosOperation(
            nodes, curvedArrow, visualization_area, context.getxOffset(), context.getListType()
        );

        fixCurvedArrow.build(timeLine);
    }
}