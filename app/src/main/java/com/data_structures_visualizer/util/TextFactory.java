package com.data_structures_visualizer.util;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public final class TextFactory {
    private static final Font DEFAULT_FONT = Font.font(18);

    private TextFactory() {}

    public static Text colored(String value, Color color) {
        Text t = new Text(value);
        
        t.setFill(color);
        t.setFont(DEFAULT_FONT);

        return t;
    }

    public static Text styledText(String type, String content) {
        switch (type) {
            case "value" ->  { return colored(content, Color.BLUE); }
            case "node"  ->  { return colored(content, Color.GOLD); }
            case "pos"   ->  { return colored(content, Color.ORANGE); }
            default      ->  { return colored(content, Color.BLACK); }
        }
    }
}