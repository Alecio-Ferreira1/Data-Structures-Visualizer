package com.data_structures_visualizer.models.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  
public final class ExplanationRepository {
    private final Map<Integer, List<ExplanationText>> explanations = new HashMap<>();

    public void addExplanation(int timelineIndex, ExplanationText text) {
        explanations.computeIfAbsent(
            timelineIndex, k -> new ArrayList<>()
        ).add(text);
    }

    public List<ExplanationText> get(int timelineIndex) {
        return explanations.getOrDefault(timelineIndex, List.of());
    }

    public void clear(){
        explanations.clear();
    }
}