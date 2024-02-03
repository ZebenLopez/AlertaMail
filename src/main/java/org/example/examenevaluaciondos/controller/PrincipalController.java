package org.example.examenevaluaciondos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.example.examenevaluaciondos.utils.EmailSender;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrincipalController {
    @FXML
    private Slider slider; // Control deslizante para ajustar el valor de umbral de memoria
    public Label porcentaje;
    public TextArea textArea; // Área de texto para mostrar información
    private String username = HelloController.getUsername(); // Nombre de usuario obtenido del controlador HelloController
    private String password = HelloController.getPassword(); // Contraseña obtenida del controlador HelloController
    private ExecutorService executorService = Executors.newFixedThreadPool(2); // Servicio de ejecutor para manejar tareas en hilos separados


    @FXML
    public void initialize() {
        configureSlider();
    }
    private void configureSlider() {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            porcentaje.setText(String.format("%.0f%%", newValue));
        });
    }

    public PrincipalController() {
        this.slider = slider; // Inicialización del control deslizante
    }

    public double getSliderValue() {
        // Método para obtener el valor del control deslizante
        if (slider != null) {
            return slider.getValue(); // Devuelve el valor del control deslizante si no es nulo
        } else {
            // Devuelve 0.0 si el control deslizante es nulo
            return 0.0;
        }
    }

    public void recibir(ActionEvent actionEvent) {

        // Método para manejar el evento de clic en el botón "Recibir"
        // Envía una tarea al servicio de ejecutor para leer correos electrónicos
        executorService.submit(() -> EmailSender.readEmails(username, password, this));
    }

    public void pdf(ActionEvent actionEvent) {
        // Método para manejar el evento de clic en el botón "PDF"
        // Abre el archivo PDF si existe, de lo contrario muestra una alerta
        String filePath = "emails.pdf"; // La ruta del archivo PDF
        File file = new File(filePath);
        if (!Desktop.isDesktopSupported()) {
            // Muestra una alerta si el escritorio no es compatible
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se puede abrir el archivo " + filePath + " porque el escritorio no es compatible.");
            alert.showAndWait();
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            try {
                desktop.open(file); // Intenta abrir el archivo
            } catch (IOException e) {
                System.out.println("Error al abrir el archivo " + filePath + ": " + e.getMessage());
            }
        } else {
            // Muestra una alerta si el archivo no existe
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("El archivo " + filePath + " no existe, debe generar un informe primero.");
            alert.showAndWait();
        }
    }

    public void moverSlider(MouseEvent mouseEvent) {
        // Método para manejar el evento de movimiento del ratón en el control deslizante
        // Ajusta el valor del control deslizante al valor actual
        slider.setValue(slider.getValue());
    }
}