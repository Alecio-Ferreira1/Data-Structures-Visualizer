package com.data_structures_visualizer.models.animation;

import java.util.function.Supplier;

import javafx.animation.Animation;

public class Step {
    private final Supplier<Animation> forward;
    private final Supplier<Animation> backward;

    public Step(Supplier<Animation> forward, Supplier<Animation> backward){
        this.forward = forward;
        this.backward = backward;
    }

    public Animation play(){ return forward.get(); }
    public Animation undo() { return backward.get(); }
}