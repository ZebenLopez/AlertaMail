package org.example.examenevaluaciondos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Método para iniciar la aplicación
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml")); // Cargador FXML para cargar la vista de inicio
        Scene scene = new Scene(fxmlLoader.load()); // Crea una nueva escena con la vista de inicio
        stage.setTitle("Panel de Login"); // Establece el título del escenario
        stage.setScene(scene); // Establece la escena del escenario
        stage.setOnCloseRequest(event -> {
            System.exit(0); // Cierra la aplicación cuando se cierra el escenario
        });
        stage.show(); // Muestra el escenario
    }

    public static void main(String[] args) {
        // Método principal para lanzar la aplicación
        launch();
    }
}