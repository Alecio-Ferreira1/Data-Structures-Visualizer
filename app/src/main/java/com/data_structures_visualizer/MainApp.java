package com.data_structures_visualizer;

import com.data_structures_visualizer.util.SceneManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Visualizador de Estruturas de Dados");
            
        SceneManager.init(primaryStage);
        SceneManager.changeScene("/fxml/ListVisualizerScreen.fxml");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}   