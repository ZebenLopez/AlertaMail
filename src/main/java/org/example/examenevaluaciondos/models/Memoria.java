package org.example.examenevaluaciondos.models;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Memoria {
    private static Thread monitorThread; // Hilo para monitorear el uso de la memoria

    public static double obtenerInfo() {
        // Método para obtener la información de uso de la memoria
        SystemInfo systemInfo = new SystemInfo(); // Crea una nueva instancia de SystemInfo
        HardwareAbstractionLayer hardware = systemInfo.getHardware(); // Obtiene la capa de abstracción de hardware

        GlobalMemory memory = hardware.getMemory(); // Obtiene la memoria global
        long availableMemory = memory.getAvailable(); // Obtiene la memoria disponible
        long totalMemory = memory.getTotal(); // Obtiene la memoria total

        double memoryUsage = (1 - (double) availableMemory / totalMemory) * 100; // Calcula el uso de la memoria
        memoryUsage = BigDecimal.valueOf(memoryUsage).setScale(2, RoundingMode.HALF_UP).doubleValue(); // Redondea el uso de la memoria a dos decimales
        return memoryUsage; // Devuelve el uso de la memoria
    }

    public static double monitoreoMemoria() {
        // Método para monitorear el uso de la memoria
        Thread thread = new Thread(() -> {
            // Crea un nuevo hilo
            while (true) {
                // Bucle infinito para monitorear continuamente el uso de la memoria
                try {
                    Thread.sleep(1000); // Espera 1 segundo antes de la próxima comprobación
                    obtenerInfo(); // Obtiene la información de uso de la memoria
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Imprime la traza de la pila si el hilo es interrumpido
                }
            }
        });
        return obtenerInfo(); // Devuelve la información de uso de la memoria
    }
}