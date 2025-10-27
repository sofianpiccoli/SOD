/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorhumedad;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread {
    
    private boolean on;
    private double humedad;
    Socket cnxServidor;
    PrintWriter pw;
    
    public HiloSensado(Socket s, PrintWriter imp){
        on = false;
        cnxServidor = s;
        pw = imp;
    }
    
    
    // Genera un valor de humedad entre 0 y 100 % (redondeado)
    public double generarHumedad(){
        return (Math.round(Math.random() * 100));
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    public double getHumedad(){
        return humedad;
    }
    
    public void run(){
        on = true;
        try {
            while (on){
                humedad = generarHumedad();

                pw.println(humedad);
                pw.flush();
                if (pw.checkError()) {
                    System.err.println("❌ HiloSensado: Error de escritura detectado. El servidor ha caído. Terminando...");
                    on = false;
                    break;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.getLogger(HiloSensado.class.getName()).log(System.Logger.Level.WARNING, "Hilo interrumpido.", ex);
                    on = false;
                }
            }
        } finally {
            try {
                if (cnxServidor != null && !cnxServidor.isClosed()) {
                    cnxServidor.close();
                    System.out.println("🔌 HiloSensado: Socket cerrado.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}