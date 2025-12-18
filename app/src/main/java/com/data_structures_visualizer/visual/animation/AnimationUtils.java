package com.data_structures_visualizer.visual.animation;

import java.util.ArrayList;

import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public final class AnimationUtils {
    public static FadeTransition fadeTransition(Node node, double seconds, Boolean fadeAway){
        FadeTransition ft = new FadeTransition(Duration.seconds(seconds), node);
        ft.setFromValue(fadeAway ? 1 : 0);
        ft.setToValue(fadeAway ? 0 : 1);
        return ft;
    }

    public static ParallelTransition displacementEffect(ArrayList<Node> nodes, double seconds, double xOffset){
        ParallelTransition pt = new ParallelTransition();

        for(Node node : nodes){
            Double currentAnchor = AnchorPane.getLeftAnchor(node);
            double startX = currentAnchor != null ? currentAnchor : node.getLayoutX();
            double targetX = startX + xOffset;

            WritableValue<Double> anchorWrapper = new WritableValue<Double>() {
                @Override
                public Double getValue() {
                    return AnchorPane.getLeftAnchor(node);
                }

                @Override
                public void setValue(Double value) {
                   AnchorPane.setLeftAnchor(node, value);
                }
            };

            Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(seconds),
                new KeyValue(anchorWrapper, targetX)
            ));

            pt.getChildren().add(timeline);
        }

        return pt;
    }

    public static Animation emptyAnimation(){
        return new PauseTransition(Duration.ZERO);
    }
}
