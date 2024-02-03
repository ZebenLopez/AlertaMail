package org.example.examenevaluaciondos.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.example.examenevaluaciondos.controller.PrincipalController;
import org.example.examenevaluaciondos.models.Memoria;
import org.example.examenevaluaciondos.services.ServicioSMTP;

import javax.mail.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class EmailSender {
    static Message[] messages = new Message[0]; // Array para almacenar los mensajes de correo electrónico

    public static void sendEmail(String username, String password, PrincipalController principalController) {
        // Método para enviar un correo electrónico
        Thread emailThread = new Thread(() -> {
            // Crea un nuevo hilo para enviar el correo electrónico
            while (true) {
                // Bucle infinito para comprobar continuamente el uso de la memoria
                if (Memoria.monitoreoMemoria() > principalController.getSliderValue()) {
                    // Si el uso de la memoria es mayor que el valor del slider, muestra una alerta y envía un correo electrónico
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Alerta de uso de Memoria");
                        alert.setHeaderText(null);
                        alert.setContentText("El uso de la memoria es de " + Memoria.monitoreoMemoria() + "%, se enviará un correo.");
                        alert.showAndWait();
                    });
                    try {
                        ServicioSMTP gestorEmail = new ServicioSMTP();
                        if (new File("src/main/resources/Ficheros/notas.txt").exists()) {
                            // Si el archivo "notas.txt" existe, envía un correo electrónico con el archivo adjunto
                            gestorEmail.enviarMensajeConAdjunto(username,
                                    username, "Alerta de uso de Memoria",
                                    "El uso de ram es de " + Memoria.monitoreoMemoria() + "%", username, password,
                                    "src/main/resources/Ficheros/notas.txt");
                        } else {
                            // Si el archivo "notas.txt" no existe, envía un correo electrónico sin el archivo adjunto
                            gestorEmail.enviarMensajeTexto(username,
                                    username, "Alerta de uso de Memoria",
                                    "El uso de ram es de " + Memoria.monitoreoMemoria() + "%", username, password);
                        }
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Información");
                            alert.setHeaderText(null);
                            alert.setContentText("Correo enviado.");
                            alert.showAndWait();
                        });
                    } catch (MessagingException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(20000); // Espera 20 segundos antes de la próxima comprobación
                } catch (InterruptedException e) {
                    // El hilo ha sido interrumpido, termina el bucle
                    Thread.currentThread().interrupt();
                }
            }
        });
        emailThread.start(); // Inicia el hilo
    }

    public static void readEmails(String username, String password, PrincipalController principalController) {
        // Método para leer los correos electrónicos
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("Se procede a cargar informes.\n" +
                    "Esto puede tardar unos segundos.");
            alert.showAndWait();
        });
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            messages = emailFolder.getMessages();

            List<String> messageInfo = new ArrayList<>();
            int filteredMessageCount = 0; // Contador para los mensajes filtrados
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                if (message.getSubject().equals("Alerta de uso de Memoria")) {
                    filteredMessageCount++; // Incrementa el contador si el mensaje coincide con el filtro
                    messageInfo.add("Email Number " + (i + 1) + "\n");
                    messageInfo.add("Asunto: " + message.getSubject() + "\n");
                    messageInfo.add("From: " + message.getFrom()[0] + "\n");
                    messageInfo.add("Texto: " + message.getContent());
                    messageInfo.add("Fecha: " + message.getSentDate() + "\n");
                    messageInfo.add("---------------------------------\n");
                }
            }

            emailFolder.close(false);
            store.close();

            Platform.runLater(() -> {
                principalController.textArea.clear(); // Limpia el textArea
                for (String info : messageInfo) {
                    principalController.textArea.appendText(info);
                }
            });
            saveEmailsToPdf(messageInfo, "emails.pdf", filteredMessageCount); // Pasa el contador al método saveEmailsToPdf
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo conectar al servidor SMTP.");
            alert.showAndWait();
        }
    }

    public static void saveEmailsToPdf(List<String> messageInfo, String filePath, int filteredMessageCount) {
        // Método para guardar los correos electrónicos en un archivo PDF
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Agrega un título al documento
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Reporte de emails de ALERTA por uso de memoria", titleFont);
            Paragraph subTitle = new Paragraph("Número de mails: " + filteredMessageCount, titleFont); // Usa el contador de mensajes filtrados
            title.setAlignment(Paragraph.ALIGN_CENTER);
            subTitle.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(subTitle);

            // Agrega una línea en blanco después del título
            document.add(new Paragraph(" "));

            // Agrega los emails
            for (String info : messageInfo) {
                document.add(new Paragraph(info));
            }

            document.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}