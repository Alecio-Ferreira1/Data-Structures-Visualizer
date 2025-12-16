package com.data_structures_visualizer.models.animation;

import com.data_structures_visualizer.models.entities.DoublyLikedList;

import javafx.animation.Animation;

public final class AnimationTimeLine {
    private final DoublyLikedList<Step> steps = new DoublyLikedList<Step>(null);
    private int index = -1;
    private Animation currentAnimation;

    public void addStep(Step step){
        steps.pushBack(step);
    }

    public void playNext(){
        if(index < (steps.lenght())){
            index++;
            steps.get(index).play();
        }
    }

    public void playPrevious(){
        if(index >= 0){
            steps.get(index).undo();
            index--;
        }
    }

    public void reset(){
        while(index >= 0){
            playPrevious();
        }
    }

    public void play(){
        playFrom(index + 1);
    }

    private void playFrom(int index){
        if(index >= steps.lenght()) return;

        this.index = index;
    
        Animation animation = steps.get(index).play();
        
        animation.setOnFinished(e -> playFrom(index + 1));
        playStep(animation);
    }

    private void playStep(Animation animation){
        if(this.currentAnimation != null){
            currentAnimation.stop();
        }

        currentAnimation = animation;
        currentAnimation.play();
    }
}
