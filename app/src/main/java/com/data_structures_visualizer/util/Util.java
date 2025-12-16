package com.data_structures_visualizer.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class Util {
    public static void showAlert(String headerMessage, String message, AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
