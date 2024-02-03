package org.example.examenevaluaciondos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.examenevaluaciondos.services.ServicioSMTP;
import org.example.examenevaluaciondos.views.PrincipalView;

import java.io.IOException;

public class HelloController {
    public TextField mail; // Campo de texto para el correo electrónico
    public TextField contrasegna; // Campo de texto para la contraseña
    @FXML
    private Label welcomeText; // Etiqueta de texto de bienvenida

    // Declaración de las variables de instancia
    private static String username; // Nombre de usuario para el correo electrónico
    private static String password; // Contraseña para el correo electrónico

    @FXML
    protected void onHelloButtonClick() {
        // Método que se ejecuta cuando se hace clic en el botón "Hello"
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void enviar(ActionEvent actionEvent) throws IOException, InterruptedException {
        // Método que se ejecuta cuando se hace clic en el botón "Enviar"
        // Asignación de los valores de los campos de texto a las variables de instancia
        username = mail.getText();
        password = contrasegna.getText();

        // Verifica si los campos de texto están vacíos
        if (username.isEmpty() || password.isEmpty()) {
            // Muestra una alerta si los campos de texto están vacíos
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("Los campos no pueden estar vacíos.");
            alert.showAndWait();
        } else {
            // Intenta conectar al servidor SMTP
            ServicioSMTP servicioSMTP = new ServicioSMTP();
            servicioSMTP.setPropiedadesServidorSMTP(); // Asegúrate de llamar a este método antes de conectar al servidor SMTP
            boolean connectionSuccessful = servicioSMTP.conectarServidorSMTPBooleano(username, password);
            if (!connectionSuccessful) {
                // Muestra una alerta si la conexión al servidor SMTP falla
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo conectar al servidor de correo.\n" +
                        "Verifica que los datos introducidos sean correctos.");
                alert.showAndWait();
            } else {
                // Muestra una alerta si la conexión al servidor SMTP es exitosa
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("Conexión exitosa.");
                alert.showAndWait();
                ((Button) actionEvent.getSource()).getScene().getWindow().hide();
                PrincipalView.show();
            }
        }
    }
    // Métodos getter para las variables de instancia
    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}