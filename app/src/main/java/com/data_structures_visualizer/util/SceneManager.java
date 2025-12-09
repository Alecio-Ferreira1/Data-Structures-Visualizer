package com.data_structures_visualizer.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SceneManager {
    private static Stage stage;

    public static void init(Stage primaryStage){
        stage = primaryStage;
    }

    public static void changeScene(String fxmlPath){
        try{
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            stage.setScene(new Scene(root));
        }

        catch(IOException e){
            e.printStackTrace();
        }
    }
}
