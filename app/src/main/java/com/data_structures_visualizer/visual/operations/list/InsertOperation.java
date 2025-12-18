package com.data_structures_visualizer.visual.operations.list;

import java.util.ArrayList;

import com.data_structures_visualizer.config.ListVisualizerConfig;
import com.data_structures_visualizer.controllers.ListVisualizerController.ListType;
import com.data_structures_visualizer.models.animation.AnimationTimeLine;
import com.data_structures_visualizer.models.animation.Step;
import com.data_structures_visualizer.visual.animation.AnimationUtils;
import com.data_structures_visualizer.visual.animation.ArrowAnimator;
import com.data_structures_visualizer.visual.animation.CurvedArrowAnimator;
import com.data_structures_visualizer.visual.animation.NodeAnimator;
import com.data_structures_visualizer.visual.animation.ArrowAnimator.DrawArrowDirection;
import com.data_structures_visualizer.visual.context.list.InsertContext;
import com.data_structures_visualizer.visual.context.list.InsertExecutionContext;
import com.data_structures_visualizer.visual.operations.common.TransverseAndHighlightOperation;
import com.data_structures_visualizer.visual.ui.Arrow;
import com.data_structures_visualizer.visual.ui.ArrowLabel;
import com.data_structures_visualizer.visual.ui.CurvedArrow;
import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public final class InsertOperation {
    private final InsertContext context;
    private final InsertExecutionContext exec = new InsertExecutionContext();
    private final AnchorPane visualization_area;
    private final ArrayList<VisualNode> nodes;
    private final ArrayList<Arrow> arrows;
    private final ArrayList<Arrow> prevArrows;
    private final CurvedArrow curvedArrow;
    private VisualNode newNode;
    private final ArrowLabel headLabel;
    private final ArrowLabel tailLabel;

    public InsertOperation(
        InsertContext context, AnchorPane visualization_area, ArrayList<VisualNode> nodes,
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
        addRemoveArrowSteps(timeLine);
        addCreateNodeStep(timeLine);
        addTranslateNodeStep(timeLine);
        addStepToConnectPrevArrow(timeLine);
        addStepToConnectNextArrow(timeLine);
        addStepToMoveCurvedArrowIfNeeded(timeLine);
        addFixLabelsStep(timeLine);
    }
    
    private Animation repositionNodes(int startIndex, double xOffset){
        ArrayList<Node> toMove = new ArrayList<Node>();

        for(int i = startIndex; i < nodes.size(); ++i){
            toMove.add(nodes.get(i));

            if(i < arrows.size()){
                toMove.add(arrows.get(i));

                if(context.getListType() == ListType.DOUBLY && i < prevArrows.size()){
                    toMove.add(prevArrows.get(i));
                }
            }
        }

        if(visualization_area.getChildren().contains(headLabel) && context.getPos() == 0){
            toMove.add(headLabel);
        }

        if(visualization_area.getChildren().contains(tailLabel) && context.getPos() != context.getInitialListSize()){
            toMove.add(tailLabel);
        }

        return AnimationUtils.displacementEffect(
            toMove, ListVisualizerConfig.translateDuration, xOffset
        );
    }

    private Animation createNode(double size, int value, int pos){
        newNode = new VisualNode(size, size, String.valueOf(value));
        final double startHeight = 0.2 * visualization_area.getHeight();
        final double width = visualization_area.getWidth();
        final double height = visualization_area.getHeight();

        context.getSinglyLinkedList().insertOnPos(value, pos);
        context.getDoublyLikedList().insertOnPos(value, pos);
        context.getCircularLinkedList().insertOnPos(value, pos);

        nodes.add(context.getPos(), newNode);
        newNode.getRect().setStrokeWidth(Math.min(width, height) * 0.005);
        newNode.setOpacity(0);
        newNode.setScaleX(0);
        newNode.setScaleY(0);
        
        visualization_area.getChildren().add(newNode);
        exec.getCreatedNodes().push(newNode);

        AnchorPane.setTopAnchor(newNode, startHeight);
        AnchorPane.setLeftAnchor(
            newNode, (ListVisualizerConfig.xOffsetForNodes * width) + 
            (size * (1.0 + ListVisualizerConfig.spacingBetweenNodes) * pos)
        );

        SequentialTransition st = new SequentialTransition(
            NodeAnimator.emergeEffect(newNode, 3 * ListVisualizerConfig.speedVisualization, true),
            NodeAnimator.highlight(newNode.getRect(), pos, Color.GOLD)
        );  

        st.setOnFinished(e -> { newNode.getRect().setStroke(Color.BLACK); });

        return st;
    }

    private Animation undoCreateNode(){
        VisualNode node = exec.getCreatedNodes().pop();

        nodes.remove(node);

        context.getSinglyLinkedList().removeItem(context.getPos());
        context.getDoublyLikedList().removeItem(context.getPos());
        context.getCircularLinkedList().removeItem(context.getPos());

        Animation animation = NodeAnimator.emergeEffect(
            node, 3 * ListVisualizerConfig.speedVisualization, false
        );

        animation.setOnFinished(e -> visualization_area.getChildren().remove(node));
        return animation;
    }

    private Animation createArrows(double lenght, int pos){
        final Arrow arrow = new Arrow(lenght);
        final double height = visualization_area.getHeight();
        final double width = visualization_area.getWidth();
        
        arrows.add(arrow);
        visualization_area.getChildren().add(arrow);
        exec.getCreatedArrows().push(arrow);

        double leftAnchorVal = ((ListVisualizerConfig.xOffsetForNodes * width) + context.getNodeWidth() + 
                               ((1 + ListVisualizerConfig.spacingBetweenNodes) * context.getNodeWidth()) * pos);

        if(context.getListType() == ListType.DOUBLY){
            Arrow prevArrow = new Arrow(lenght);

            prevArrow.setRotate(180);
            prevArrows.add(prevArrow);

            visualization_area.getChildren().add(prevArrow);
            exec.getCreatedPrevArrows().push(prevArrow);
            
            AnchorPane.setTopAnchor(arrow, (height / 2) - (arrow.getBoundsInParent().getHeight() * 1.25));
            AnchorPane.setLeftAnchor(arrow, leftAnchorVal);

            AnchorPane.setTopAnchor(prevArrow, (height / 2) + (arrow.getBoundsInParent().getHeight() / 2));
            AnchorPane.setLeftAnchor(prevArrow, leftAnchorVal);

            return new SequentialTransition(
                ArrowAnimator.animateIn(prevArrow, 1 * ListVisualizerConfig.speedVisualization, DrawArrowDirection.FORWARD),
                ArrowAnimator.animateIn(
                    arrow, 1 * ListVisualizerConfig.speedVisualization, DrawArrowDirection.FORWARD
                )
            );
        }

        AnchorPane.setTopAnchor(arrow, (height / 2) - (arrow.getBoundsInParent().getHeight() / 2));
        AnchorPane.setLeftAnchor(arrow, leftAnchorVal);

        return ArrowAnimator.animateIn(arrow, 1 * ListVisualizerConfig.speedVisualization, DrawArrowDirection.FORWARD);
    }

    private Animation undoCreateArrows(){
        Arrow arrow = exec.getCreatedArrows().pop();
        Arrow prevArrow = exec.getCreatedPrevArrows().pop();

        arrows.remove(arrow);
        prevArrows.remove(prevArrow);

        if(prevArrow != null){
            Animation removeArrows = new SequentialTransition(
                ArrowAnimator.animateOut(arrow, 1, DrawArrowDirection.BACKWARD),
                ArrowAnimator.animateOut(prevArrow, 1, DrawArrowDirection.FORWARD)
            );

            removeArrows.setOnFinished(e -> {
                visualization_area.getChildren().remove(arrow);
                visualization_area.getChildren().remove(prevArrow);
            });
        } 

        Animation removeArrowNext = ArrowAnimator.animateOut(arrow, 1, DrawArrowDirection.BACKWARD);
        removeArrowNext.setOnFinished(e -> { visualization_area.getChildren().remove(arrow); });

        return removeArrowNext;
    }

    private void addTransversalStep(AnimationTimeLine timeLine){
        if(context.getPos() > 0){
            new TransverseAndHighlightOperation(nodes, timeLine).build(context.getPos() - 1);

            Rectangle targetRect = nodes.get(context.getPos() - 1).getRect();
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

    private void addRemoveArrowSteps(AnimationTimeLine timeLine){
        if(context.getPos() == 0 || context.getPos() == context.getInitialListSize()) 
            return;

        timeLine.addStep(new Step(
            () -> {
                double speed = ListVisualizerConfig.speedVisualization;

                if(context.getListType() == ListType.DOUBLY){
                    return new SequentialTransition(
                        ArrowAnimator.animateOut(arrows.get(context.getPos() - 1), 1 * speed, DrawArrowDirection.BACKWARD),
                        ArrowAnimator.animateOut(prevArrows.get(context.getPos() - 1), 1 * speed, DrawArrowDirection.FORWARD)
                    );
                }

                return ArrowAnimator.animateOut(
                    arrows.get(context.getPos() - 1), 1 * speed, DrawArrowDirection.FORWARD
                );
            },
            () -> {
                double speed = ListVisualizerConfig.speedVisualization;

                if(context.getListType() == ListType.DOUBLY){
                    return new SequentialTransition(
                        ArrowAnimator.animateIn(arrows.get(context.getPos() - 1), 1 * speed, DrawArrowDirection.FORWARD),
                        ArrowAnimator.animateIn(prevArrows.get(context.getPos() - 1), 1 * speed, DrawArrowDirection.BACKWARD)
                    );
                }

                return ArrowAnimator.animateIn(
                    arrows.get(context.getPos() - 1), 1 * speed, DrawArrowDirection.BACKWARD
                );
            }
        ));
    }

    private void addCreateNodeStep(AnimationTimeLine timeLine){
        timeLine.addStep(new Step(
            () -> new SequentialTransition(
                repositionNodes(context.getPos(), context.getxOffset()),
                createNode(context.getNodeWidth(), context.getValue(), context.getPos())
            ),
            () -> new SequentialTransition(
                undoCreateNode(),
                repositionNodes(context.getPos(), -context.getxOffset())
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

        double speed = 1 * ListVisualizerConfig.speedVisualization;
        
        timeLine.addStep(new Step(
            () -> {
                Animation resetStrokeColorFomPrevNode = NodeAnimator.animateStroke(
                    nodes.get(context.getPos() - 1).getRect(), 
                    (Color) nodes.get(context.getPos() - 1).getRect().getStroke(), Color.BLACK, 
                    (int) (300 * ListVisualizerConfig.speedVisualization) , false
                );

                if(context.getPos() - 1 >= arrows.size()){
                    return new SequentialTransition(
                        createArrows(
                            context.getNodeWidth() * ListVisualizerConfig.spacingBetweenNodes,
                            context.getPos() - 1
                        ), resetStrokeColorFomPrevNode
                    );
                }

                Animation arrowAnimation = ArrowAnimator.animateIn(
                    arrows.get(context.getPos() - 1), speed, DrawArrowDirection.FORWARD
                );

                if(context.getListType() == ListType.DOUBLY){
                    return new SequentialTransition(
                        arrowAnimation,
                        ArrowAnimator.animateIn(
                            prevArrows.get(context.getPos() - 1), speed, DrawArrowDirection.FORWARD
                        ), resetStrokeColorFomPrevNode
                    );
                }

                return new SequentialTransition(arrowAnimation, resetStrokeColorFomPrevNode);
            },
            () -> {
                Animation resetStrokeColor = NodeAnimator.animateStroke(
                    nodes.get(context.getPos() - 1).getRect(), 
                    (Color) nodes.get(context.getPos() - 1).getRect().getStroke(), Color.ORANGE, 
                    (int) (300 * ListVisualizerConfig.speedVisualization) , false
                );

                if(!exec.getCreatedArrows().isEmpty()){
                    return new SequentialTransition(
                        resetStrokeColor, 
                        undoCreateArrows()
                    );
                }

                Animation arrowAnimation = ArrowAnimator.animateOut(
                    arrows.get(context.getPos() - 1), speed, DrawArrowDirection.BACKWARD
                );

                if(context.getListType() == ListType.DOUBLY){
                    return new SequentialTransition(
                        resetStrokeColor,
                        ArrowAnimator.animateOut(
                            prevArrows.get(context.getPos() - 1), speed, DrawArrowDirection.FORWARD
                        ), arrowAnimation
                    );
                }

                return new SequentialTransition(resetStrokeColor, arrowAnimation);
            }
        ));   
    }

    private void addStepToConnectNextArrow(AnimationTimeLine timeLine){
        if(context.getPos() >= context.getInitialListSize()) return;

        timeLine.addStep(new Step(
            () -> createArrows(
                    context.getNodeWidth() * ListVisualizerConfig.spacingBetweenNodes, 
                    context.getPos()
                ),
            () -> undoCreateArrows()
        ));
    }

    private void addStepToMoveCurvedArrowIfNeeded(AnimationTimeLine timeLine){
        if(context.getListType() != ListType.CIRCULAR) return;
        
        timeLine.addStep(new Step(
            () -> {
                final VisualNode first = nodes.get(0);
                final VisualNode last = nodes.get(nodes.size() - 1);

                double fromStartX = last.getLayoutX() + (last.getRect().getWidth() / 2) - context.getxOffset();
                double fromStartY = last.getLayoutY() + last.getRect().getHeight();
                double toStartX = last.getLayoutX() + (last.getRect().getWidth() / 2);
                double toStartY = last.getLayoutY() + last.getRect().getHeight();
                double fromEndX = first.getLayoutX() + (first.getRect().getWidth() / 2) - context.getxOffset();
                double fromEndY = fromStartY;
                double toEndX = first.getLayoutX() + (first.getRect().getWidth() / 2);
                double toEndY = fromEndY;
                double width = visualization_area.getWidth();
                double height = visualization_area.getHeight();
                    
                return CurvedArrowAnimator.animateEndPoints(
                    fromStartX, fromStartY, fromEndX, fromEndY, toStartX, toStartY, toEndX, toEndY, 
                    1.5 * ListVisualizerConfig.speedVisualization, 
                     Math.min(width, height) * 0.02, curvedArrow, nodes.size() == 1
                );
            }, 
            () -> {
                final VisualNode first = nodes.get(0);
                final VisualNode last = nodes.get(nodes.size() - 1);

                double fromStartX = last.getLayoutX() + (last.getRect().getWidth() / 2) - context.getxOffset();
                double fromStartY = last.getLayoutY() + last.getRect().getHeight();
                double toStartX = last.getLayoutX() + (last.getRect().getWidth() / 2);
                double toStartY = last.getLayoutY() + last.getRect().getHeight();
                double fromEndX = first.getLayoutX() + (first.getRect().getWidth() / 2) - context.getxOffset();
                double fromEndY = fromStartY;
                double toEndX = first.getLayoutX() + (first.getRect().getWidth() / 2);
                double toEndY = fromEndY;
                double width = visualization_area.getWidth();
                double height = visualization_area.getHeight();

                return CurvedArrowAnimator.animateEndPoints(
                    toStartX, toStartY, toEndX, toEndY, fromStartX, fromStartY, fromEndX, fromEndY, 
                    1.5 * ListVisualizerConfig.speedVisualization, 
                    Math.min(width, height) * 0.02, curvedArrow, nodes.size() == 1
                );
            }
        )); 
    }

    private void addFixLabelsStep(AnimationTimeLine timeLine){
        TranslateTransition translateHead = new TranslateTransition(
            Duration.seconds(ListVisualizerConfig.translateDuration / 3), headLabel  
        );

        TranslateTransition translateTail = new TranslateTransition(
            Duration.seconds(ListVisualizerConfig.translateDuration / 3), tailLabel  
        );

        TranslateTransition undoTranslateHead = new TranslateTransition(
            Duration.seconds(ListVisualizerConfig.translateDuration / 3), headLabel  
        );

        TranslateTransition undoTranslateTail = new TranslateTransition(
            Duration.seconds(ListVisualizerConfig.translateDuration / 3), tailLabel  
        );

        translateHead.setByX(-context.getxOffset());
        translateTail.setByX(context.getxOffset());
        undoTranslateHead.setByX(context.getxOffset());
        undoTranslateTail.setByX(-context.getxOffset());

        if(context.getPos() == 0){
            timeLine.addStep(new Step(
                () -> translateHead,
                () -> undoTranslateHead
            ));

            return;
        }

        if(context.getPos() == context.getInitialListSize()){
            timeLine.addStep(new Step(
                () -> translateTail,
                () -> undoTranslateTail
            ));
        }
    }
}
