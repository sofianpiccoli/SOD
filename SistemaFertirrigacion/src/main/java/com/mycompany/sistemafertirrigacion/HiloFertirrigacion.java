// Archivo: com.mycompany.sistemafertirrigacion/HiloValvulaFertirrigacion.java
package com.mycompany.sistemafertirrigacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dgera
 */

public class HiloFertirrigacion extends Thread {

    private Socket socket;
    private BufferedReader br;

    public HiloFertirrigacion(Socket socket) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String mensaje;
            while ((mensaje = br.readLine()) != null) {
                if (mensaje.equals("ABRIR")) {
                    System.out.println("✅ [Actuador] Válvula de Fertirrigación abierta.");
                } else if (mensaje.equals("CERRAR")) {
                    System.out.println("❌ [Actuador] Válvula de Fertirrigación cerrada.");
                }
            }
        } catch (IOException ex) {
            System.err.println("❌ HiloFertirrigacion: Falla de conexión detectada. Terminando hilo.");
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ignored) {}
        }
    }
}