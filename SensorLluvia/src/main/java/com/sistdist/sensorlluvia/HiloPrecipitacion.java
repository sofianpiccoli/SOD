/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorlluvia;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloPrecipitacion extends Thread {
    
    private boolean on;
    private boolean lluvia;
    Socket cnxServidor;
    PrintWriter pw;
    
    public HiloPrecipitacion(Socket s, PrintWriter imp){
        on = false;
        cnxServidor = s;
        pw = imp;
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    // Genera lluvia con probabilidad del 30%
    public boolean generarLluvia(){
        boolean L = false;
        if (Math.random() < 0.3) { // 30% de chance de que "llueva"
            L = true;
        }
        return L; 
    }
    
    public void run(){
        on=true;
        try {
            while (on){
                lluvia = generarLluvia();

                pw.println(lluvia);
                pw.flush();
                if (pw.checkError()) {
                    System.err.println("❌ HiloPrecipitacion: Error de escritura detectado. El servidor ha caído. Terminando...");
                    on = false;
                    break;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.getLogger(HiloPrecipitacion.class.getName()).log(System.Logger.Level.WARNING, "Hilo interrumpido.", ex);
                    on = false;
                }
            }
        } finally {
            try {
                if (cnxServidor != null && !cnxServidor.isClosed()) {
                    cnxServidor.close();
                    System.out.println("🔌 HiloPrecipitacion: Socket cerrado.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}
