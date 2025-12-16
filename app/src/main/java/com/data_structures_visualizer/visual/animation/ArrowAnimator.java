package com.data_structures_visualizer.visual.animation;

import com.data_structures_visualizer.visual.ui.Arrow;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public final class ArrowAnimator {
    public enum DrawArrowDirection { 
        FORWARD, 
        BACKWARD 
    }

    public static Animation animateIn(Arrow arrow, double seconds, DrawArrowDirection direction){
        Line line = arrow.getLine();
        double lenght = line.getBoundsInParent().getWidth() + line.getBoundsInParent().getHeight();
        double startOffset = direction == DrawArrowDirection.FORWARD ? lenght : -lenght; 

        line.getStrokeDashArray().setAll(lenght, lenght);
        line.setStrokeDashOffset(startOffset);

        arrow.getHeadPolyline().setOpacity(0);

        Timeline lineAnimation = new Timeline(
            new KeyFrame(
                Duration.seconds(seconds),
                new KeyValue(
                    arrow.getLine().strokeDashOffsetProperty(), 0, Interpolator.EASE_OUT
                )
            )
        );

        FadeTransition headFade = AnimationUtils.fadeTransition(arrow.getHeadPolyline(), seconds / 3, false);

        return new SequentialTransition(lineAnimation, headFade);
    }

    public static Animation animateOut(Arrow arrow, double seconds, DrawArrowDirection direction){
        Line line = arrow.getLine();
        double lenght = line.getBoundsInParent().getWidth() + line.getBoundsInParent().getHeight();
        double startOffset = direction == DrawArrowDirection.FORWARD ? -lenght : lenght; 

        line.getStrokeDashArray().setAll(lenght, lenght);
        line.setStrokeDashOffset(startOffset);

        Timeline lineAnimation = new Timeline(
            new KeyFrame(
                Duration.seconds(seconds),
                new KeyValue(
                    arrow.getLine().strokeDashOffsetProperty(), 0, Interpolator.EASE_IN
                )
            )
        );

        FadeTransition headFade = AnimationUtils.fadeTransition(arrow.getHeadPolyline(), seconds / 3, true);

        return new SequentialTransition(headFade, lineAnimation);
    }
}
