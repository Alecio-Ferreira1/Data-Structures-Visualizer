package com.data_structures_visualizer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try{
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/ListVisualizerScreen.fxml")
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Visualizador de Estruturas de Dados");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        catch(IOException e){
            System.out.println("Erro ao carregar o arquivo fxml!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}   