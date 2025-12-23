package com.data_structures_visualizer.visual.animation;

import com.data_structures_visualizer.visual.ui.VisualNode;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public final class NodeAnimator {
    public static Animation highlight(Rectangle rect, int speedMillis, Color highlightColor){
        Color base = (Color) rect.getStroke();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(rect.strokeProperty(), base) 
            ),
            new KeyFrame(Duration.millis(speedMillis),
                new KeyValue(rect.strokeProperty(), highlightColor) 
            ),
            new KeyFrame(Duration.millis(speedMillis * 2),
                new KeyValue(rect.strokeProperty(), base) 
            )
        );

        timeline.setCycleCount(1);
        return timeline;
    }    

    public static Animation animateMove(VisualNode node, double x, double y, double seconds){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(seconds), node);
        tt.setToX(x);
        tt.setToY(y);
        return tt;
    }

    public static ParallelTransition emergeEffect(VisualNode node, double seconds, boolean spawn){
        ScaleTransition scaleTransition = new ScaleTransition(
            Duration.seconds(seconds), node
        );

        double minScale = 0.0001;
        double from = spawn ? minScale : 1;
        double to = spawn ? 1 : minScale;

        scaleTransition.setFromX(from);
        scaleTransition.setFromY(from);
        scaleTransition.setToX(to);
        scaleTransition.setToY(to);
        scaleTransition.setInterpolator(spawn ? Interpolator.EASE_OUT : Interpolator.EASE_IN);

        FadeTransition fadeTransition = AnimationUtils.fadeTransition(node, seconds, !spawn);

        return new ParallelTransition(scaleTransition, fadeTransition);
    }

    public static Animation animateStroke(Rectangle rectangle, Color fromColor, Color toColor, int durationMillis, boolean repeat){
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(rectangle.strokeProperty(), fromColor)),
            new KeyFrame(Duration.millis(durationMillis), new KeyValue(rectangle.strokeProperty(), toColor))
        );

        if(repeat){
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(true);    
        }

        else{
            timeline.setCycleCount(1);
        }

        return timeline;
    }

    public static Animation pulseHighlight(Rectangle rect,Color highlightColor, int pulseMillis, int totalMillis){
        Color base = (Color) rect.getStroke();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(rect.strokeProperty(), base)
            ),
            new KeyFrame(Duration.millis(pulseMillis),
                new KeyValue(rect.strokeProperty(), highlightColor)
            )
        );

        timeline.setAutoReverse(true);

        int cycles = Math.max(1, totalMillis / (pulseMillis * 2));

        if(cycles % 2 != 0)
            cycles++;

        timeline.setCycleCount(cycles);

        timeline.setOnFinished(e -> rect.setStroke(base));

        return timeline;
    }
}
