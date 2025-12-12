package com.data_structures_visualizer.util;

import javafx.scene.control.Alert;

public final class Util {
    public static void showAlertForExceedingValue(int value){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Tamanho máximo excedido.");
        alert.setContentText(String.format("Valor máximo permitido: %d", value));
        alert.showAndWait();
    }
}
