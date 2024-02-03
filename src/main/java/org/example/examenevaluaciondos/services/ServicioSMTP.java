package org.example.examenevaluaciondos.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ServicioSMTP {
    private Properties propiedades; // Propiedades para la configuración del servidor SMTP
    private Session sesion; // Sesión de correo

    public void setPropiedadesServidorSMTP() {
        // Método para establecer las propiedades del servidor SMTP
        propiedades = System.getProperties();
        propiedades.put("mail.smtp.auth", "true"); // Habilita la autenticación
        propiedades.put("mail.smtp.host", "smtp.gmail.com"); // Establece el host del servidor SMTP
        propiedades.put("mail.smtp.port", "587"); // Establece el puerto del servidor SMTP
        propiedades.put("mail.smtp.starttls.enable", "true"); // Habilita el inicio de TLS
        sesion = Session.getInstance(propiedades, null); // Crea una nueva sesión de correo con las propiedades establecidas
    }

    private Transport conectarServidorSMTP(String direccionEmail, String password)
            throws NoSuchProviderException, MessagingException {
        // Método para conectar al servidor SMTP
        Transport t = (Transport) sesion.getTransport("smtp"); // Obtiene el transporte de la sesión
        t.connect(propiedades.getProperty("mail.smtp.host"), direccionEmail, password); // Conecta al servidor SMTP
        return t; // Devuelve el transporte
    }

    public boolean conectarServidorSMTPBooleano(String direccionEmail, String password) {
        // Método para conectar al servidor SMTP y devolver un booleano indicando si la conexión fue exitosa
        try {
            Transport t = (Transport) sesion.getTransport("smtp"); // Obtiene el transporte de la sesión
            t.connect(propiedades.getProperty("mail.smtp.host"), direccionEmail, password); // Conecta al servidor SMTP
            return true; // Devuelve true si la conexión fue exitosa
        } catch (MessagingException e) {
            return false; // Devuelve false si hubo un error
        }
    }

    private Message crearNucleoMensaje(String emisor, String destinatario, String asunto)
            throws AddressException, MessagingException {
        // Método para crear el núcleo de un mensaje de correo electrónico
        Message mensaje = new MimeMessage(sesion); // Crea un nuevo mensaje Mime
        mensaje.setFrom(new InternetAddress(emisor)); // Establece el emisor del mensaje
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario)); // Añade un destinatario al mensaje
        mensaje.setSubject(asunto); // Establece el asunto del mensaje
        return mensaje; // Devuelve el mensaje
    }

    private Message crearMensajeTexto(String emisor, String destinatario, String asunto, String textoMensaje)
            throws MessagingException, AddressException, IOException {
        // Método para crear un mensaje de correo electrónico con texto
        Message mensaje = crearNucleoMensaje(emisor, destinatario, asunto); // Crea el núcleo del mensaje
        mensaje.setText(textoMensaje); // Establece el texto del mensaje
        return mensaje; // Devuelve el mensaje
    }

    private Message crearMensajeConAdjunto(String emisor, String destinatario, String asunto,
                                           String textoMensaje, String pathFichero)
            throws MessagingException, AddressException, IOException {
        // Método para crear un mensaje de correo electrónico con un archivo adjunto
        Message mensaje = crearNucleoMensaje(emisor, destinatario, asunto); // Crea el núcleo del mensaje

        // Cuerpo del mensaje
        BodyPart bodyPart = new MimeBodyPart(); // Crea una nueva parte del cuerpo Mime
        bodyPart.setText(textoMensaje); // Establece el texto de la parte del cuerpo

        // Adjunto del mensaje
        MimeBodyPart mimeBodyPart = new MimeBodyPart(); // Crea una nueva parte del cuerpo Mime
        mimeBodyPart.attachFile(new File(pathFichero)); // Adjunta un archivo a la parte del cuerpo

        // Composición del mensaje (Cuerpo + Adjunto)
        Multipart multipart = new MimeMultipart(); // Crea un nuevo multipart Mime
        multipart.addBodyPart(bodyPart); // Añade la parte del cuerpo al multipart
        multipart.addBodyPart(mimeBodyPart); // Añade la parte del cuerpo con el archivo adjunto al multipart

        mensaje.setContent(multipart); // Establece el contenido del mensaje como el multipart
        return mensaje; // Devuelve el mensaje
    }

    public void enviarMensajeTexto(String emisor, String destinatario,
                                   String asunto, String textoMensaje, String direccionEmail, String password)
            throws AddressException, MessagingException, IOException {
        // Método para enviar un mensaje de correo electrónico con texto
        setPropiedadesServidorSMTP(); // Establece las propiedades del servidor SMTP
        Message mensaje = crearMensajeTexto(emisor, destinatario, asunto, textoMensaje); // Crea el mensaje
        Transport t = conectarServidorSMTP(direccionEmail, password); // Conecta al servidor SMTP

        t.sendMessage(mensaje, mensaje.getAllRecipients()); // Envía el mensaje
        t.close(); // Cierra el transporte
    }

    public void enviarMensajeConAdjunto(String emisor, String destinatario,
                                        String asunto, String textoMensaje, String direccionEmail,
                                        String password, String pathFichero)
            throws AddressException, MessagingException, IOException {
        // Método para enviar un mensaje de correo electrónico con un archivo adjunto
        setPropiedadesServidorSMTP(); // Establece las propiedades del servidor SMTP
        Message mensaje = crearMensajeConAdjunto(emisor, destinatario, asunto, textoMensaje, pathFichero); // Crea el mensaje
        Transport t = conectarServidorSMTP(direccionEmail, password); // Conecta al servidor SMTP
        t.sendMessage(mensaje, mensaje.getAllRecipients()); // Envía el mensaje
        t.close(); // Cierra el transporte
    }
}