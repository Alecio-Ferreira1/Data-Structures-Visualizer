package com.data_structures_visualizer.visual.animation;

import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class NodeAnimator {
    private static final Map<Rectangle, Timeline> activeTimeLines = new HashMap<>();

    public static void startHighlight(Rectangle rect, int speedMillis, Color highlightColor){
        if(activeTimeLines.containsKey(rect)) return;

        Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(rect.strokeProperty(), highlightColor)
            ),
            new KeyFrame(
                Duration.millis(speedMillis), 
                new KeyValue(rect.strokeProperty(), Color.BLACK)
            )
        );

        timeline.setCycleCount(speedMillis);
        timeline.setAutoReverse(true);
        timeline.play();

        activeTimeLines.put(rect, timeline);
    }    

    public static void stopHighlight(Rectangle rect){
        Timeline timeline = activeTimeLines.remove(rect);

        if(timeline != null){
            timeline.stop();
            rect.setStroke(Color.BLACK);
        }
    }
}
