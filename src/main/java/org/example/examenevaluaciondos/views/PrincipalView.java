package org.example.examenevaluaciondos.views;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.examenevaluaciondos.HelloApplication;
import org.example.examenevaluaciondos.controller.HelloController;
import org.example.examenevaluaciondos.controller.PrincipalController;
import org.example.examenevaluaciondos.utils.EmailSender;

import java.io.IOException;

public class PrincipalView{
    private static String username = HelloController.getUsername(); // Nombre de usuario obtenido del controlador HelloController
    private static String password = HelloController.getPassword(); // Contraseña obtenida del controlador HelloController
    private static Stage principalStage = new Stage(); // Escenario principal de la aplicación
    private static Thread emailThread; // Hilo para enviar correos electrónicos

    public static void show() throws IOException, InterruptedException {
        // Método para mostrar la vista principal
        // Oculta el Stage actual.
        principalStage.hide();

        // Crea un nuevo Stage con el nuevo estilo.
        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("principal-view.fxml")); // Cargador FXML para cargar la vista principal
        Scene scene = new Scene(fxmlLoader.load()); // Crea una nueva escena con la vista principal
        newStage.setTitle("Panel de control"); // Establece el título del nuevo escenario
        newStage.setScene(scene); // Establece la escena del nuevo escenario
        newStage.setResizable(false); // Hace que el nuevo escenario no sea redimensionable
        newStage.setOnCloseRequest(event -> {
            System.exit(0); // Cierra la aplicación cuando se cierra el nuevo escenario
        });
        newStage.show(); // Muestra el nuevo escenario

        PrincipalController principalController = fxmlLoader.getController(); // Obtiene el controlador de la vista principal

        emailThread = new Thread(() -> EmailSender.sendEmail(username, password, principalController)); // Crea un nuevo hilo para enviar correos electrónicos
        Platform.runLater(emailThread); // Ejecuta el hilo para enviar correos electrónicos
    }
}