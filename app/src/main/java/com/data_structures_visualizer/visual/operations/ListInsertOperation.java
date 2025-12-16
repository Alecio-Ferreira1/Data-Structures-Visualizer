package com.data_structures_visualizer.visual.operations;

import java.util.ArrayList;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.controllers.ListVisualizerController.ListType;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.visual.animation.AnimationUtils;
import com.data_structures_visualizer.visual.animation.ArrowAnimator;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.animation.ArrowAnimator.DrawArrowDirection;
import com.data_structures_visualizer.visual.context.list.ListInsertContext;
import com.data_structures_visualizer.visual.context.list.ListInsertExecutionContext;
import com.data_structures_visualizer.visual.operations.common.TransverseAndHighlightOperation;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.CurvedArrow;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public final class ListInsertOperation {
    private final ListInsertContext context;
    private final ListInsertExecutionContext exec = new ListInsertExecutionContext();
    private final AnchorPane visualization_area;
    private final ArrayList<VisualNode> nodes;
    private final ArrayList<Arrow> arrows;
    private final ArrayList<Arrow> prevArrows;
    private final CurvedArrow curvedArrow;
    private VisualNode newNode;

    public ListInsertOperation(
        ListInsertContext context, AnchorPane visualization_area, ArrayList<VisualNode> nodes,
        ArrayList<Arrow> arrows, ArrayList<Arrow> prevArrows, CurvedArrow curvedArrow
    ){
        this.context = context;
        this.visualization_area = visualization_area;
        this.nodes = nodes;
        this.arrows = arrows;
        this.prevArrows = prevArrows;
        this.curvedArrow = curvedArrow;
    }

    public void build(AnimationTimeLine timeLine){
        addTransversalStep(timeLine);
        addRemoveArrowSteps(timeLine);
        addCreateNodeStep(timeLine);
        addTranslateNodeStep(timeLine);
        addStepToConnectPrevArrow(timeLine);
        addStepToConnectNextArrow(timeLine);
    }
    
    private Animation repositionNodes(int startIndex, double xOffset){
        ArrayList<Node> nodes = new ArrayList<Node>();

        if(context.getListType() == ListType.CIRCULAR){
            nodes.add(curvedArrow);
        }

        for(int i = startIndex; i < nodes.size(); ++i){
            nodes.add(this.nodes.get(i));

            if(i < (nodes.size()) - 1){
                nodes.add(arrows.get(i));

                if(context.getListType() == ListType.DOUBLY){
                    nodes.add(prevArrows.get(i));
                }
            }
        }

        return AnimationUtils.displacementEffect(nodes, ListVisualizerConfig.translateDuration, xOffset);
    }

    private Animation createNode(double size, int value, int pos){
        newNode = new VisualNode(size, size, String.valueOf(value));
        final double startHeight = 0.2 * visualization_area.getHeight();
        final double width = visualization_area.getWidth();
        
        newNode.setOpacity(0);
        newNode.setScaleX(0);
        newNode.setScaleY(0);

        visualization_area.getChildren().add(newNode);
        exec.getCreatedNodes().push(newNode);

        AnchorPane.setTopAnchor(newNode, startHeight);
        AnchorPane.setLeftAnchor(
            newNode, (ListVisualizerConfig.xOffsetForNodes * width) + 
            (size * ListVisualizerConfig.spacingBetweenNodes * pos * width)
        );

        return new SequentialTransition(
            NodeAnimator.emergeEffect(newNode, 3, true),
            NodeAnimator.startHighlight(newNode.getRect(), pos, Color.GOLD)
        );
    }

    private Animation undoCreateNode(){
        VisualNode node = exec.getCreatedNodes().pop();

        SequentialTransition animation = new SequentialTransition(
            NodeAnimator.stopHighlight(node.getRect()),
            NodeAnimator.emergeEffect(node, 3, false)
        );

        animation.setOnFinished(e -> visualization_area.getChildren().remove(node));
        return animation;
    }

    private Animation createArrows(double lenght, int pos){
        final Arrow arrow = Arrow.createInvisble(lenght);
        final double height = visualization_area.getHeight();
        final double width = visualization_area.getWidth();
        
        arrows.add(context.getPos() - 1, arrow);
        visualization_area.getChildren().add(arrow);
        exec.getCreatedArrows().push(arrow);

        double leftAnchorVal = ((ListVisualizerConfig.xOffsetForNodes * width) + context.getNodeWidth() + 
                               ((1 + ListVisualizerConfig.spacingBetweenNodes) * context.getNodeWidth()) * context.getPos());

        if(context.getListType() == ListType.DOUBLY){
            Arrow prevArrow = new Arrow(lenght);
            prevArrow.setRotate(180);
            prevArrow.setVisible(false);

            prevArrows.add(context.getPos() - 1, prevArrow);
            visualization_area.getChildren().add(prevArrow);
            exec.getCreatedPrevArrows().push(prevArrow);

            AnchorPane.setTopAnchor(prevArrow, (height / 2) - (prevArrow.getBoundsInParent().getHeight() * 1.25));
            AnchorPane.setLeftAnchor(prevArrow, leftAnchorVal);

            AnchorPane.setTopAnchor(arrow, (height / 2) + (arrow.getBoundsInParent().getHeight() / 2));
            AnchorPane.setLeftAnchor(arrow, leftAnchorVal);

            return new SequentialTransition(
                ArrowAnimator.animateIn(arrow, 1, DrawArrowDirection.FORWARD),
                ArrowAnimator.animateIn(prevArrow, 1, DrawArrowDirection.BACKWARD)
            );
        }

        AnchorPane.setTopAnchor(arrow, (height / 2) - (arrow.getBoundsInParent().getHeight() / 2));
        AnchorPane.setLeftAnchor(arrow, leftAnchorVal);

        return ArrowAnimator.animateIn(arrow, 1, DrawArrowDirection.FORWARD);
    }

    private Animation undoCreateArrows(){
        Arrow arrow = exec.getCreatedArrows().pop();
        Arrow preArrow = exec.getCreatedPrevArrows().pop();

        if(preArrow != null){
            Animation removeArrows = new SequentialTransition(
                ArrowAnimator.animateOut(arrow, 1, DrawArrowDirection.BACKWARD),
                ArrowAnimator.animateOut(preArrow, 1, DrawArrowDirection.FORWARD)
            );

            removeArrows.setOnFinished(e -> {
                visualization_area.getChildren().remove(arrow);
                visualization_area.getChildren().remove(preArrow);
            });
        } 

        Animation removeArrowNext = ArrowAnimator.animateOut(arrow, 1, DrawArrowDirection.BACKWARD);
        removeArrowNext.setOnFinished(e -> { visualization_area.getChildren().remove(arrow); });

        return removeArrowNext;
    }

    private void addTransversalStep(AnimationTimeLine timeLine){
        if(context.getPos() > 0){
            new TransverseAndHighlightOperation(nodes, timeLine).build(context.getPos() - 1);
        }
    }

    private void addRemoveArrowSteps(AnimationTimeLine timeLine){
        if(context.getPos() >= context.getInitialListSize()) return;
        
        Animation removeArrowsFromPrevNode;
        Animation undoRemoveArrowsFromPrevNode;

        if(context.getListType() == ListType.DOUBLY){
            removeArrowsFromPrevNode = new SequentialTransition(
                ArrowAnimator.animateIn(arrows.get(context.getPos() - 1), 1, DrawArrowDirection.FORWARD),
                ArrowAnimator.animateIn(prevArrows.get(context.getPos() - 1), 1, DrawArrowDirection.BACKWARD)
            );

            undoRemoveArrowsFromPrevNode = new SequentialTransition(
                ArrowAnimator.animateOut(arrows.get(context.getPos() - 1), 1, DrawArrowDirection.BACKWARD),
                ArrowAnimator.animateOut(prevArrows.get(context.getPos() - 1), 1, DrawArrowDirection.FORWARD)
            );
        }

        else{
            removeArrowsFromPrevNode = ArrowAnimator.animateIn(
                arrows.get(context.getPos() - 1), 1, DrawArrowDirection.FORWARD
            );

            undoRemoveArrowsFromPrevNode = ArrowAnimator.animateOut(
                arrows.get(context.getPos() - 1), 1, DrawArrowDirection.BACKWARD
            );
        }

        timeLine.addStep(new Step(
            () -> removeArrowsFromPrevNode,
            () -> undoRemoveArrowsFromPrevNode
        ));
    }

    private void addCreateNodeStep(AnimationTimeLine timeLine){
        timeLine.addStep(new Step(
            () -> new SequentialTransition(
                repositionNodes(context.getPos(), context.getxOffset()),
                createNode(context.getNodeWidth(), context.getValue(), context.getPos())
            ),
            () -> new SequentialTransition(
                repositionNodes(context.getPos(), -context.getxOffset()),
                undoCreateNode()
            )
        ));
    }

    private void addTranslateNodeStep(AnimationTimeLine timeLine){
        final double height = visualization_area.getHeight();

        timeLine.addStep(new Step(
            () -> NodeAnimator.animateMove(
                newNode, 0, (height / 2) - (context.getNodeWidth() / 2) - (0.2 * height), 
                ListVisualizerConfig.translateDuration
            ),
            () -> NodeAnimator.animateMove(
                newNode, 0, -((height / 2) - (context.getNodeWidth() / 2) - (0.2 * height)),
                ListVisualizerConfig.translateDuration
            )
        ));
    }

    private void addStepToConnectPrevArrow(AnimationTimeLine timeLine){
        if(context.getPos() == 0) return;
        
        timeLine.addStep(new Step(
            () -> createArrows(context.getNodeWidth() * (1 + ListVisualizerConfig.spacingBetweenNodes), context.getPos() - 1),
            () -> undoCreateArrows()
        ));   
    }

     private void addStepToConnectNextArrow(AnimationTimeLine timeLine){
        if(context.getPos() >= context.getInitialListSize()) return;

        timeLine.addStep(new Step(
            () -> createArrows(context.getNodeWidth() * (1 + ListVisualizerConfig.spacingBetweenNodes), context.getPos()),
            () -> undoCreateArrows()
        ));
    }
}
