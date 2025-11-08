/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.electrovalvula3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author miria
 */
public class Electrovalvula3 {

    public static void main(String[] args) {
        InetAddress IPServidor;
        PrintWriter pw;
        Socket cliente = null;
        HiloValvula sensorV3 = null;

        while (true) {
            try {
                if (cliente == null || cliente.isClosed() || (sensorV3 != null && !sensorV3.isAlive())) {
                    if (cliente != null) {
                        try { cliente.close(); } catch (IOException ignored) {}
                    }
                    IPServidor = InetAddress.getByName("127.0.0.1");
                    cliente = new Socket(IPServidor, 20000);
                    pw = new PrintWriter(cliente.getOutputStream());
                    pw.println("electroValvula3");
                    pw.flush();
                    sensorV3 = new HiloValvula(cliente, 3);
                    sensorV3.start();
                    System.out.println("✅ Sensor conectado en el puerto 20000.");
                }
                if (sensorV3 != null) {
                    sensorV3.join();
                    System.out.println("⚠️ Hilo de sensado terminó. Reintentando conexión...");
                    try {
                        Thread.sleep(15000);
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
                sensorV3 = null;
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
}
