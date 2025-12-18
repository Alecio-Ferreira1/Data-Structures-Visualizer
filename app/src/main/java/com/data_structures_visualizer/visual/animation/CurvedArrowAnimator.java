package com.data_structures_visualizer.visual.animation;

import com.data_structures_visualizer.visual.ui.CurvedArrow;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.util.Duration;

public final class CurvedArrowAnimator {
    public static Animation animateEndPoints(
        double fromStartX, double fromStartY, double fromEndX, double fromEndy,
        double toStartX, double toStarty, double toEndX, double toEndy,
        double seconds, double arrowSize, CurvedArrow curve, boolean loop
    ){

        if(curve == null) return new SequentialTransition();
        
        Transition t = new Transition() {
            {   
                setInterpolator(Interpolator.EASE_BOTH); 
                setCycleDuration(Duration.seconds(seconds));
            }

            @Override
            protected void interpolate(double frac){
                double sx = fromStartX + frac * (toStartX - fromStartX);
                double sy = fromStartY + frac * (toStarty - fromStartY);
                double ex = fromEndX + frac * (toEndX - fromEndX);
                double ey = fromEndy + frac * (toEndy - fromEndy);

                curve.update(sx, sy, ex, ey, arrowSize, loop);
            }
        };

        return t;
    }
}
