package com.data_structures_visualizer.visual.animation;

import java.util.ArrayList;

import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;

public final class AnimationUtils {
    public static FadeTransition fadeTransition(Node node, double seconds, Boolean fadeAway){
        FadeTransition ft = new FadeTransition(Duration.seconds(seconds), node);
        ft.setFromValue(fadeAway ? 1 : 0);
        ft.setToValue(fadeAway ? 0 : 1);
        return ft;
    }

    public static ParallelTransition displacementEffect(ArrayList<Node> nodes, double seconds, double xOffset){
        ParallelTransition pt = new ParallelTransition();
        ArrayList<TranslateTransition> transitions = new ArrayList<TranslateTransition>();

        for(Node node : nodes){
            TranslateTransition transition = new TranslateTransition(Duration.seconds(seconds), node);
            transition.setByX(xOffset);
            transitions.add(transition);
        }

        pt.getChildren().addAll(transitions);

        return pt;
    }
}
