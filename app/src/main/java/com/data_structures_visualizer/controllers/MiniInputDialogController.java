package com.data_structures_visualizer.controllers;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MiniInputDialogController {
    @FXML
    private Label message_label;
    @FXML
    private TextField input_text_field;
    @FXML
    private Button send_button;
    @FXML
    private Button cancel_button;
    
    private String message;
    private final int maxDigits = 4;
    private final String warningMessage = String.format("A entrada deve ser numérica com o máximo de %d dígitos.", maxDigits);
    private Stage stage;
    private IntConsumer onConfirm;

    @FXML
    public void initialize(){
        cancel_button.setOnAction(e -> {
            stage.close();
        });

        send_button.setOnAction(e -> {
            handleWithInputValidation();
        });
    }

    public void setMessage(String message){
        this.message = message;
        message_label.setText(message);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setOnconfirm(IntConsumer onConfirm){
        this.onConfirm = onConfirm;
    }

    public Boolean validateInput(String text){
        return (text.length() <= maxDigits && text.matches("\\d+"));
    }

    public void handleWithInputValidation(){
        String inputText = input_text_field.getText();

        if(validateInput(inputText)){
            onConfirm.accept(Integer.parseInt(inputText));
            stage.close();
        }

        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.setHeaderText("Valor inválido!");
            alert.setContentText(warningMessage);
            alert.showAndWait();
        }
    }
}
