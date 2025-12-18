package com.data_structures_visualizer.models.animation;

import com.data_structures_visualizer.models.entities.DoublyLikedList;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public final class AnimationTimeLine {
    private final DoublyLikedList<Step> steps = new DoublyLikedList<Step>(null);
    private int index = -1;
    private Animation currentAnimation;
    private DoubleProperty progress = new SimpleDoubleProperty(0);
    private Runnable onFinished;

    public DoublyLikedList<Step> getSteps(){
        return steps;
    }

    public ReadOnlyDoubleProperty progressProperty(){
        return progress;
    }

    public void addStep(Step step){
        steps.pushBack(step);
    }

    private void updateProgress(){
        if(steps.isEmpty()){
            progress.set(0);
        }

        else{
            progress.set(((index + 1) / (double) steps.lenght()));
        }
    }

    public void playNext(){
        if((index + 1) < ( steps.lenght())){
            index++;
            updateProgress();
            playStep(steps.get(index).play());
        }
    }

    public void playPrevious(double rate){
        if(index >= 0){
            Animation animation = steps.get(index).undo();
            animation.setRate(rate);

            index--;
            updateProgress();
            playStep(animation);
        }

        else{
            currentAnimation = null;
        }
    }

    private void fastBackward(){
        while(index >= 0) {
            playPrevious(5.0);
        }
    }

    public void reset(){
        fastBackward();
        updateProgress();
    }

    public void play(){
        playFrom(index + 1, 1.0);
    }

    public void playFast(){
        playFrom(index + 1, 5.0);
    }

    private void playFrom(int index, double rate){
        if(steps.isEmpty() || index >= steps.lenght()){
            currentAnimation = null;

            if(onFinished != null){
                Platform.runLater(onFinished);
            }

            return;
        }

        this.index = index;
        updateProgress();
    
        Animation animation = steps.get(index).play();
        animation.setRate(rate);
        
        animation.setOnFinished(e -> playFrom(index + 1, rate));
        playStep(animation);
    }

    private void playStep(Animation animation){
        if(this.currentAnimation != null){
            currentAnimation.stop();
        }

        currentAnimation = animation;
        currentAnimation.play();
    }

    public void pause(){
        if(currentAnimation != null){
            currentAnimation.pause();
        }   
    }

    public boolean isPlaying(){
        return currentAnimation != null && 
               currentAnimation.getStatus() == Animation.Status.RUNNING;
    } 

    public void setOnFinished(Runnable r){
        onFinished = r;
    }
}
