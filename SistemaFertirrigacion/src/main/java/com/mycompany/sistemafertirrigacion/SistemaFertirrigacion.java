// Archivo: com.mycompany.sistemafertirrigacion/SistemaFertirrigacion.java
package com.mycompany.sistemafertirrigacion;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
/**
 *
 * @author dgera
 */

public class SistemaFertirrigacion {

    public static void main(String[] args) {
        InetAddress IPServidor;
        PrintWriter pw;
        Socket cliente = null;
        HiloFertirrigacion sensorV1 = null;

        while (true) {
            try {
                if (cliente == null || cliente.isClosed() || (sensorV1 != null && !sensorV1.isAlive())) {
                    if (cliente != null) {
                        try { cliente.close(); } catch (IOException ignored) {}
                    }
                    IPServidor = InetAddress.getByName("127.0.0.1");
                    cliente = new Socket(IPServidor, 20000);
                    pw = new PrintWriter(cliente.getOutputStream());
                    pw.println("electroValvulaFertirrigacion");
                    pw.flush();
                    sensorV1 = new HiloFertirrigacion(cliente);
                    sensorV1.start();
                    System.out.println("✅ Sensor conectado en el puerto 20000.");
                }
                if (sensorV1 != null) {
                    sensorV1.join();
                    System.out.println("⚠️ Hilo de sensado terminó. Reintentando conexión...");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            } catch (InterruptedException e) {
                System.err.println("Hilo principal interrumpido.");
                Thread.currentThread().interrupt();
                break;

            } catch (IOException ex) {
                System.err.println("❌ ERROR: No se pudo conectar con el servidor central. Reintentando en 5s...");
                cliente = null;
                sensorV1 = null;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}