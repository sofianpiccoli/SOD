/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensorradiacion;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author miria
 */
public class SensorRadiacion {

    public static void main(String[] args) {
        InetAddress IPServidor;
        PrintWriter pw;
        Socket cliente = null;
        HiloSensado sensorR = null;

        while (true) {
            try {
                if (cliente == null || cliente.isClosed() || (sensorR != null && !sensorR.isAlive())) {
                    if (cliente != null) {
                        try { cliente.close(); } catch (IOException ignored) {}
                    }
                    IPServidor = InetAddress.getByName("127.0.0.1");
                    cliente = new Socket(IPServidor, 20000);
                    pw = new PrintWriter(cliente.getOutputStream());
                    pw.println("sensorRadiacion");
                    pw.flush();
                    sensorR = new HiloSensado(cliente, pw);
                    sensorR.start();
                    System.out.println("✅ Sensor conectado en el puerto 20000.");
                }
                if (sensorR != null) {
                    sensorR.join();
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
                sensorR = null;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
