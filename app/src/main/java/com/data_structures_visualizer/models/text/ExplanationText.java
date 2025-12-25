package com.data_structures_visualizer.models.text;

public final class ExplanationText {
    private final String text;
    private final int stepNumber;

    public ExplanationText(int stepNumber, String text){
        this.stepNumber = stepNumber;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getStepNumber() {
        return stepNumber;
    }
}