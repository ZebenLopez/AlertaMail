module org.example.examenevaluaciondos {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.oshi;
    requires java.mail;
    requires itextpdf;
    requires java.desktop;


    opens org.example.examenevaluaciondos to javafx.fxml;
    exports org.example.examenevaluaciondos;
    exports org.example.examenevaluaciondos.controller;
    opens org.example.examenevaluaciondos.controller to javafx.fxml;
}